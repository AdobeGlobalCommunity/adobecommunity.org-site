package org.adobecommunity.site.internal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.cms.usergenerated.UGCBucketConfig;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.APPROVE_ACTION;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.CONTENT_TYPE;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/sharearticle",
		"sling.servlet.selectors=allowpost" })
public class ShareArticleServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(ShareArticleServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private UserGeneratedContentService ugcService;

	private static final UGCBucketConfig BUCKET_CONFIG = new UGCBucketConfig();
	static {
		BUCKET_CONFIG.setAction(APPROVE_ACTION.MOVE);
		BUCKET_CONFIG.setBucket("agc/adobecommunity-org/articles");
		BUCKET_CONFIG.setContentType(CONTENT_TYPE.BLOG_POST);
		BUCKET_CONFIG.setPathDepth(0);
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		String referer = request.getHeader("referer");
		if (referer.contains("?")) {
			referer = referer.substring(0, referer.indexOf("?"));
		}

		String title = request.getParameter("title");
		String summary = request.getParameter("summary");
		String link = request.getParameter("link");
		String thumbnail = request.getParameter("thumbnail");
		String[] tags = request.getParameterValues("tags");

		if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(summary) && StringUtils.isNotBlank(link)
				&& StringUtils.isNotBlank(thumbnail)) {

			try {

				Resource container = ugcService.createUGCContainer(request, BUCKET_CONFIG, title + "\n\n" + summary,
						"/content/agc/adobecommunity-org/learn/articles/"
								+ (new SimpleDateFormat("yyyy/MM").format(new Date())));

				JackrabbitSession session = (JackrabbitSession) request.getResourceResolver().adaptTo(Session.class);
				final UserManager userManager = session.getUserManager();

				User user = (User) userManager.getAuthorizable(request.getResourceResolver().getUserID());

				Map<String, Object> pageProperties = new HashMap<String, Object>() {
					private static final long serialVersionUID = 1L;
					{
						put(JcrConstants.JCR_PRIMARYTYPE, "sling:Page");
					}
				};

				String path = container.getPath() + "/" + toName(title);

				log.debug("Creating article page {}", path);
				ResourceUtil.getOrCreateResource(container.getResourceResolver(), path, pageProperties,
						JcrConstants.NT_UNSTRUCTURED, false);

				Map<String, Object> contentProperties = new HashMap<String, Object>();
				String name = "";
				if (user.getProperty("profile/name") != null && user.getProperty("profile/name").length > 0) {
					name = user.getProperty("profile/name")[0].getString();
				}
				contentProperties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
				contentProperties.put("author", name);
				contentProperties.put("jcr:description", summary);
				contentProperties.put("jcr:title", title);
				contentProperties.put("original", link);
				contentProperties.put("publishDate", new SimpleDateFormat("YYY-MM-dd").format(new Date()));
				contentProperties.put("published", true);
				contentProperties.put(ResourceResolver.PROPERTY_RESOURCE_TYPE,
						"adobecommunity-org/components/pages/post");
				if (tags != null) {
					contentProperties.put("sling:taxonomy", tags);
				}
				contentProperties.put("sling:template", "/conf/adobecommunity-org/site/templates/post");
				contentProperties.put("sling:thumbnail", thumbnail);
				contentProperties.put("snippet", summary);

				String contentPath = path + "/" + JcrConstants.JCR_CONTENT;
				log.debug("Creating article contents {}", contentPath);
				ResourceUtil.getOrCreateResource(container.getResourceResolver(), contentPath, contentProperties,
						JcrConstants.NT_UNSTRUCTURED, true);

				ValueMap properties = request.getResource().getValueMap();

				Map<String, Object> params = new HashMap<String, Object>();
				request.getRequestParameterList()
						.forEach(p -> params.put(p.getName(), request.getParameter(p.getName())));
				params.put("articlePath", container.getPath());

				EmailQueueConsumer.queueMessage(jobManager, properties.get(EmailQueueConsumer.TO, String.class),
						properties.get(EmailQueueConsumer.SUBJECT, String.class),
						properties.get("template", String.class), params);

				response.sendRedirect(referer + "?res=shared");
			} catch (Exception e) {
				log.debug("Unexpected exception creating article", e);
				response.sendRedirect(referer + "?err=err");
			}

		} else {
			log.debug("Not all required parameters provided");
			response.sendRedirect(referer + "?err=req");
		}

	}

	private String toName(String title) {

		final StringBuilder sb = new StringBuilder();
		char lastAdded = 0;

		title = title.toLowerCase();
		for (int i = 0; i < title.length(); i++) {
			final char c = title.charAt(i);
			char toAdd = c;

			if ("abcdefghijklmnopqrstuvwxyz0123456789_-".indexOf(c) < 0) {
				if (lastAdded == '-') {
					// do not add several _ in a row
					continue;
				}
				toAdd = '-';

			} else if (i == 0 && Character.isDigit(c)) {
				sb.append("-");
			}

			sb.append(toAdd);
			lastAdded = toAdd;
		}

		if (sb.length() == 0) {
			sb.append("-");
		}

		return sb.toString();
	}

}

package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.cms.core.usergenerated.UGCBucketConfig;
import org.apache.sling.cms.core.usergenerated.UserGeneratedContentService;
import org.apache.sling.cms.core.usergenerated.UserGeneratedContentService.APPROVE_ACTION;
import org.apache.sling.cms.core.usergenerated.UserGeneratedContentService.CONTENT_TYPE;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/shareevent",
		"sling.servlet.selectors=allowpost" })
public class ShareEventServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(ShareEventServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private UserGeneratedContentService ugcService;

	private static final UGCBucketConfig BUCKET_CONFIG = new UGCBucketConfig();
	static {
		BUCKET_CONFIG.setAction(APPROVE_ACTION.move);
		BUCKET_CONFIG.setBucket("agc/adobecommunity-org/events");
		BUCKET_CONFIG.setContentType(CONTENT_TYPE.blog_post);
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
		String type = request.getParameter("eventtype");
		String eventDate = request.getParameter("eventdate");
		String eventTime = request.getParameter("eventtime");
		final String link = request.getParameter("link");
		String presenter = request.getParameter("presenter");
		String thumbnail = request.getParameter("image");
		String[] tags = request.getParameterValues("tags");
		String location = request.getParameter("location");
		String locationLink = request.getParameter("locationlink");

		if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(summary) && StringUtils.isNotBlank(link)
				&& StringUtils.isNotBlank(thumbnail) && StringUtils.isNotBlank(type)
				&& StringUtils.isNotBlank(eventDate) && StringUtils.isNotBlank(eventTime)
				&& StringUtils.isNotBlank(location)) {

			try {

				Resource container = ugcService.createUGCContainer(request, BUCKET_CONFIG, title + "\n\n" + summary,
						"/content/agc/adobecommunity-org/events");

				String path = container.getPath() + "/" + toName(title);

				log.debug("Creating event page {}", path);
				ResourceUtil.getOrCreateResource(container.getResourceResolver(), path, new HashMap<String, Object>() {
					private static final long serialVersionUID = 1L;
					{
						put(JcrConstants.JCR_PRIMARYTYPE, "sling:Page");
					}
				}, JcrConstants.NT_UNSTRUCTURED, false);

				Map<String, Object> contentProperties = new HashMap<String, Object>();
				contentProperties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
				contentProperties.put("jcr:description", summary);
				contentProperties.put("jcr:title", title);
				contentProperties.put("sling:resourceType", "adobecommunity-org/components/pages/event");
				if (tags != null && tags.length > 0) {
					contentProperties.put("sling:taxonomy", tags);
				}
				contentProperties.put("sling:thumbnail", thumbnail);
				contentProperties.put("sling:template", "/conf/adobecommunity-org/site/templates/post");
				contentProperties.put("eventdate", eventDate);
				contentProperties.put("eventtime", eventTime);
				contentProperties.put("featured", false);
				contentProperties.put("hideInSitemap", false);
				contentProperties.put("location", location);
				if (StringUtils.isNotBlank(locationLink)) {
					contentProperties.put("locationlink", locationLink);
				}
				if (StringUtils.isNotBlank(presenter)) {
					contentProperties.put("presenter", presenter);
				}
				contentProperties.put("published", true);

				String contentPath = path + "/" + JcrConstants.JCR_CONTENT;
				log.debug("Creating event contents {}", contentPath);
				ResourceUtil.getOrCreateResource(container.getResourceResolver(), contentPath, contentProperties,
						JcrConstants.NT_UNSTRUCTURED, false);

				ResourceUtil.getOrCreateResource(container.getResourceResolver(), contentPath + "/cta",
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
							{
								put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
								put("link", link);
								put("style", "btn btn-primary");
								put("text", "Signup");
							}
						}, JcrConstants.NT_UNSTRUCTURED, false);

				final String rteText = summary.replace("\n", "<br/>");
				ResourceUtil.getOrCreateResource(container.getResourceResolver(), contentPath + "/container/text",
						new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
							{
								put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
								put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "sling-cms/components/general/richtext");
								put("text", rteText);
							}
						}, JcrConstants.NT_UNSTRUCTURED, true);

				ValueMap properties = request.getResource().getValueMap();

				Map<String, Object> params = new HashMap<String, Object>();
				request.getRequestParameterList()
						.forEach(p -> params.put(p.getName(), request.getParameter(p.getName())));
				params.put("eventPath", container.getPath());

				EmailQueueConsumer.queueMessage(jobManager, properties.get(EmailQueueConsumer.TO, String.class),
						properties.get(EmailQueueConsumer.SUBJECT, String.class),
						properties.get("template", String.class), params);

				response.sendRedirect(referer + "?res=shared");
			} catch (Exception e) {
				log.debug("Unexpected exception creating event", e);
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

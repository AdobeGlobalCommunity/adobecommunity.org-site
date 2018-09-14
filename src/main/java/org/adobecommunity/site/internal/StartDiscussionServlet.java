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
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/startdiscussion",
		"sling.servlet.selectors=allowpost" })
public class StartDiscussionServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 5199626895605062465L;

	private static final Logger log = LoggerFactory.getLogger(StartDiscussionServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private UserGeneratedContentService ugcService;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		String referer = request.getHeader("referer");
		if (referer.contains("?")) {
			referer = referer.substring(0, referer.indexOf("?"));
		}

		String title = request.getParameter("title");
		String summary = request.getParameter("summary");

		if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(summary)) {
			try {

				ValueMap properties = request.getResource().getValueMap();

				UGCBucketConfig bucketConfig = new UGCBucketConfig();
				bucketConfig.setAction(APPROVE_ACTION.PUBLISH);
				bucketConfig.setBucket(properties.get("ugcBucket", String.class));
				bucketConfig.setContentType(CONTENT_TYPE.FORUM_POST);
				bucketConfig.setPathDepth(2);
				Resource container = ugcService.createUGCContainer(request, bucketConfig, title + "\n\n" + summary,
						null);

				String path = container.getPath() + "/jcr:content";

				JackrabbitSession session = (JackrabbitSession) request.getResourceResolver().adaptTo(Session.class);
				final UserManager userManager = session.getUserManager();

				User user = (User) userManager.getAuthorizable(request.getResourceResolver().getUserID());

				log.debug("Creating discussion page {}", path);

				Map<String, Object> contentProperties = new HashMap<String, Object>();
				contentProperties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
				contentProperties.put("jcr:description", summary);
				contentProperties.put("jcr:title", title);
				contentProperties.put("sling:resourceType", "adobecommunity-org/components/pages/discussion");
				contentProperties.put("publishDate", new SimpleDateFormat("YYY-MM-dd").format(new Date()));
				contentProperties.put("published", true);

				String name = request.getResourceResolver().getUserID();
				if (user.getProperty("profile/name") != null && user.getProperty("profile/name").length > 0) {
					name = user.getProperty("profile/name")[0].getString();
				}
				contentProperties.put("username", name);

				ResourceUtil.getOrCreateResource(container.getResourceResolver(), path, contentProperties,
						JcrConstants.NT_UNSTRUCTURED, true);

				Map<String, Object> params = new HashMap<String, Object>();
				request.getRequestParameterList()
						.forEach(p -> params.put(p.getName(), request.getParameter(p.getName())));
				params.put("path", container.getPath());

				EmailQueueConsumer.queueMessage(jobManager, properties.get(EmailQueueConsumer.TO, String.class),
						properties.get(EmailQueueConsumer.SUBJECT, String.class),
						properties.get("template", String.class), params);

				response.sendRedirect(referer + "?res=started");
			} catch (Exception e) {
				log.warn("Unexpected exception creating discussion", e);
				response.sendRedirect(referer + "?err=err");
			}
		} else {
			log.debug("Not all required parameters provided");
			response.sendRedirect(referer + "?err=req");
		}

	}
}

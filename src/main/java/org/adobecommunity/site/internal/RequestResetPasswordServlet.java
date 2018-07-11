package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/requestresetpassword",
		"sling.servlet.selectors=allowpost" })
public class RequestResetPasswordServlet extends SlingAllMethodsServlet {

	public static final String PN_RESETTOKEN = "resettoken";

	public static final String PN_EMAIL = "email";

	public static final String PN_RESETDEADLINE = "resetdeadline";

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(RequestResetPasswordServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private ResourceResolverFactory factory;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> serviceParams = new HashMap<String, Object>();
		serviceParams.put(ResourceResolverFactory.SUBSERVICE, "usermanager");

		String referer = request.getHeader("referer");
		if (referer.contains("?")) {
			referer = referer.substring(0, referer.indexOf("?"));
		}

		ResourceResolver adminResolver = null;
		try {

			adminResolver = factory.getServiceResourceResolver(serviceParams);
			JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
			final UserManager userManager = session.getUserManager();

			String email = StringUtils.trim(request.getParameter("email"));

			if (userManager.getAuthorizable(email) != null) {

				User user = (User) userManager.getAuthorizable(email);

				String resetToken = UUID.randomUUID().toString();
				Calendar deadline = Calendar.getInstance();
				deadline.add(Calendar.HOUR, 24);

				ValueFactory vf = session.getValueFactory();

				user.setProperty(PN_RESETTOKEN, vf.createValue(resetToken));
				user.setProperty(PN_RESETDEADLINE, vf.createValue(deadline));

				log.debug("Saving changes!");
				adminResolver.commit();

				ValueMap properties = request.getResource().getValueMap();

				EmailQueueConsumer.queueMessage(jobManager, properties.get("emailsender", String.class),
						request.getParameter(PN_EMAIL), properties.get("resetsubject", String.class),
						properties.get("resetmessage", String.class), new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
							{
								put(PN_RESETTOKEN, resetToken);
								put(PN_EMAIL, request.getParameter(PN_EMAIL));
							}
						});

				response.sendRedirect(referer + "?res=passwordreset");

			} else {
				log.warn("Unable to find user {}", email);
				response.sendRedirect(referer + "?res=passwordreset");
			}
		} catch (Exception e) {
			log.debug("Unexpected exception requesting password reset", e);
			response.sendRedirect(referer + "?res=passwordreset");
		} finally {
			if (adminResolver != null) {
				adminResolver.close();
			}
		}

	}

}

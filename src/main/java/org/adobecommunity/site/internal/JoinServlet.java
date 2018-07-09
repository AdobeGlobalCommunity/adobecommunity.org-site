package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.adobecommunity.site.models.InitialUserProfile;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
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
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/join", "sling.servlet.selectors=allowpost" })
public class JoinServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(JoinServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private ResourceResolverFactory factory;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		InitialUserProfile profile = request.adaptTo(InitialUserProfile.class);

		if (profile != null) {
			Map<String, Object> serviceParams = new HashMap<String, Object>();
			serviceParams.put(ResourceResolverFactory.SUBSERVICE, "usermanager");

			ResourceResolver adminResolver;
			try {
				adminResolver = factory.getServiceResourceResolver(serviceParams);

				JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
				final UserManager userManager = session.getUserManager();

				if (userManager.getAuthorizable(profile.getEmail()) == null) {

					log.debug("Creating user {}", profile.getEmail());
					User user = userManager.createUser(profile.getEmail(), profile.getPassword());

					log.debug("Updating profile for {}", profile.getEmail());
					profile.updateUser(user, adminResolver.adaptTo(Session.class));

					log.debug("Adding user {} to members group", profile.getEmail());
					Group members = (Group) userManager.getAuthorizable("members");
					members.addMember(user);

					log.debug("Saving changes!");
					adminResolver.commit();

					ValueMap properties = request.getResource().getValueMap();
					log.debug("Sending confirmation email");
					EmailQueueConsumer.queueMessage(jobManager, properties.get("confirmationsender", String.class),
							profile.getEmail(), properties.get("confirmationsubject", String.class),
							properties.get("confirmationmessage", String.class), profile.toMap());
					
					log.debug("Sending info email");
					EmailQueueConsumer.queueMessage(jobManager, properties.get("confirmationsender", String.class),
							properties.get("confirmationsender", String.class), properties.get("infosubject", String.class),
							properties.get("infosubject", String.class), profile.toMap());

					response.sendRedirect(
							request.getResourceResolver().map(request, properties.get("memberpage", String.class))
									+ ".html");

				} else {
					log.debug("A user with the name {} already exists!", profile.getEmail());
					response.sendRedirect(request.getHeader("referer") + "?err=exists");
				}
			} catch (LoginException | RepositoryException | IllegalArgumentException | IllegalAccessException e) {
				log.debug("Unexpected exception creating user", e);
				response.sendRedirect(request.getHeader("referer") + "?err=err");
			}
		} else {
			log.debug("Unable to adapt request to user profile");
			response.sendRedirect(request.getHeader("referer") + "?err=req");
		}

	}

}

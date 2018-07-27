package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.StripeIntegration;
import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.adobecommunity.site.models.InitialUserProfile;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
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
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/join", "sling.servlet.selectors=allowpost" })
public class JoinServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(JoinServlet.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private ResourceResolverFactory factory;

	@Reference
	private StripeIntegration stripe;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		InitialUserProfile profile = request.adaptTo(InitialUserProfile.class);

		String referer = request.getHeader("referer");
		if (referer.contains("?")) {
			referer = referer.substring(0, referer.indexOf("?"));
		}

		String stripeToken = request.getParameter("stripeToken");

		if (profile != null) {
			Map<String, Object> serviceParams = new HashMap<String, Object>();
			serviceParams.put(ResourceResolverFactory.SUBSERVICE, "usermanager");

			ResourceResolver adminResolver = null;
			try {
				adminResolver = factory.getServiceResourceResolver(serviceParams);

				JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
				final UserManager userManager = session.getUserManager();

				if (userManager.getAuthorizable(profile.getEmail()) == null) {

					log.debug("Creating user {}", profile.getEmail());
					User user = userManager.createUser(profile.getEmail(), profile.getPassword());

					log.debug("Updating profile for {}", profile.getEmail());
					profile.updateUser(user, session);

					log.debug("Adding user {} to members group", profile.getEmail());
					Group members = (Group) userManager.getAuthorizable("members");
					members.addMember(user);

					ValueMap properties = request.getResource().getValueMap();
					String confirmationSender = properties.get("confirmationsender", String.class);
					try {
						if (!"Free".equals(profile.getLevel())) {
							ValueFactory vf = session.getValueFactory();
							user.setProperty("payment/stripeToken", vf.createValue(stripeToken));
							String customerId = stripe.createSubscription(stripeToken, profile);
							user.setProperty("payment/customerId", vf.createValue(customerId));
						}
					} catch (Exception e) {
						log.warn("Unable to set user " + profile.getEmail() + " up with subscription", e);
						EmailQueueConsumer.queueMessage(jobManager, confirmationSender, "Failed to setup subscription",
								"Failed to setup subscription ${level} for ${email}", profile.toMap());
					}

					log.debug("Saving changes!");
					adminResolver.commit();

					log.debug("Sending confirmation email");
					EmailQueueConsumer.queueMessage(jobManager, profile.getEmail(),
							properties.get("confirmationsubject", String.class),
							properties.get("confirmationmessage", String.class), profile.toMap());

					log.debug("Sending info email");
					EmailQueueConsumer.queueMessage(jobManager, confirmationSender,
							properties.get("infosubject", String.class), properties.get("infomessage", String.class),
							profile.toMap());

					response.sendRedirect(
							request.getResourceResolver().map(request, properties.get("memberpage", String.class))
									+ ".html?res=created");

				} else {
					log.debug("A user with the name {} already exists!", profile.getEmail());
					response.sendRedirect(request.getHeader("referer") + "?err=exists");
				}
			} catch (Exception e) {
				log.debug("Unexpected exception creating user", e);
				response.sendRedirect(referer + "?err=err");
			} finally {
				if (adminResolver != null) {
					adminResolver.close();
				}
			}
		} else {
			log.debug("Unable to adapt request to user profile");
			response.sendRedirect(referer + "?err=req");
		}

	}

}

package org.adobecommunity.site.internal;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.models.UserProfile;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/updateprofile",
		"sling.servlet.selectors=allowpost" })
public class UpdateProfileServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -1265699330904067650L;

	private static final Logger log = LoggerFactory.getLogger(JoinServlet.class);

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		UserProfile profile = request.adaptTo(UserProfile.class);

		String referer = request.getHeader("referer");
		if (referer.contains("?")) {
			referer = referer.substring(0, referer.indexOf("?"));
		}

		if (profile != null) {

			try {

				JackrabbitSession session = (JackrabbitSession) request.getResourceResolver().adaptTo(Session.class);
				final UserManager userManager = session.getUserManager();

				if (userManager.getAuthorizable(request.getResourceResolver().getUserID()) != null) {

					User user = (User) userManager.getAuthorizable(request.getResourceResolver().getUserID());
					log.debug("Updating profile for {}", request.getResourceResolver().getUserID());
					profile.updateUser(user, session);

					log.debug("Saving changes!");
					request.getResourceResolver().commit();

					response.sendRedirect(referer + "?res=profileupd");
				} else {
					log.warn("A user with the name {} already exists!", request.getResourceResolver().getUserID());
					response.sendRedirect(referer + "?err=dne");
				}
			} catch (Exception e) {
				log.debug("Unexpected exception creating user", e);
				response.sendRedirect(referer + "?err=err");
			}
		} else {
			log.debug("Unable to adapt request to user profile");
			response.sendRedirect(referer + "?err=req");
		}

	}

}

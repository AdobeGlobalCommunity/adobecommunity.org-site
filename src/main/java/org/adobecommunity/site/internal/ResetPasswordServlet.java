package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/resetpassword",
		"sling.servlet.selectors=allowpost" })
public class ResetPasswordServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -7394015241801119859L;

	private static final Logger log = LoggerFactory.getLogger(JoinServlet.class);

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
		ResourceResolver resolver = null;
		try {

			resolver = factory.getServiceResourceResolver(serviceParams);
			JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
			final UserManager userManager = session.getUserManager();

			if (userManager.getAuthorizable(request.getParameter("email")) != null) {

				User user = (User) userManager.getAuthorizable(request.getParameter("email"));

				String resetToken = getValue(user.getProperty(RequestResetPasswordServlet.PN_RESETTOKEN), String.class);
				Calendar resetDeadline = getValue(user.getProperty(RequestResetPasswordServlet.PN_RESETDEADLINE),
						Calendar.class);

				if (resetToken.equals(request.getParameter(RequestResetPasswordServlet.PN_RESETTOKEN))
						&& Calendar.getInstance().before(resetDeadline)) {
					String password = request.getParameter("password");
					user.changePassword(password);

					log.debug("Saving changes!");
					resolver.commit();

					response.sendRedirect(referer + "?res=resetpasswd");
				} else {
					if (!resetToken.equals(request.getParameter(RequestResetPasswordServlet.PN_RESETTOKEN))) {
						log.debug("Provided token {} does match stored token {}",
								request.getParameter(RequestResetPasswordServlet.PN_RESETTOKEN), resetToken);
					}
					if (!resetDeadline.after(Calendar.getInstance())) {
						log.debug("Reset deadline {} has expired vs current time {}", resetDeadline,
								Calendar.getInstance());
					}
					response.sendRedirect(referer + "?err=invalid");
				}
			} else {
				log.warn("Unable to find user {}", request.getResourceResolver().getUserID());
				response.sendRedirect(referer + "?err=invalid");
			}
		} catch (Exception e) {
			log.debug("Unexpected exception requesting password reset", e);
			response.sendRedirect(referer + "?err=unknown");
		} finally {
			if (resolver != null) {
				resolver.close();
			}
		}

	}

	private <E> E getValue(Value[] property, Class<E> clazz)
			throws ValueFormatException, IllegalStateException, RepositoryException {
		if (property != null && property.length > 0) {
			Value v = property[0];
			if (clazz.isAssignableFrom(String.class)) {
				return clazz.cast(v.getString());
			}
			if (clazz.isAssignableFrom(Calendar.class)) {
				return clazz.cast(v.getDate());
			}
		}
		return null;
	}

}

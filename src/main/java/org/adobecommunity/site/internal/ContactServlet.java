package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
		"sling.servlet.resourceTypes=adobecommunity-org/components/forms/contact",
		"sling.servlet.selectors=allowpost" })
public class ContactServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 6077223846541287324L;

	@Reference
	private JobManager jobManager;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		if (StringUtils.isNotEmpty(email)) {

			String thankyoupage = request.getResource().getValueMap().get("thankyoupage", String.class);

			queueContactEmail(request, email);

			queueConfirmationEmail(request, email);

			response.sendRedirect(request.getResourceResolver().map(request, thankyoupage) + ".html");
		} else {
			response.sendError(400, "No email specified");
			throw new ServletException("No email specified");
		}

	}

	private void queueConfirmationEmail(SlingHttpServletRequest request, String email) {
		ValueMap properties = request.getResource().getValueMap();
		EmailQueueConsumer.queueMessage(jobManager, properties.get(EmailQueueConsumer.TO, String.class), email,
				properties.get("confirmationsubject", String.class),
				properties.get("confirmationmessage", String.class), new HashMap<String, Object>());

	}

	private void queueContactEmail(SlingHttpServletRequest request, String email) {
		ValueMap properties = request.getResource().getValueMap();

		Map<String, Object> params = new HashMap<String, Object>();
		request.getRequestParameterList().forEach(p -> params.put(p.getName(), request.getParameter(p.getName())));

		EmailQueueConsumer.queueMessage(jobManager, email, properties.get(EmailQueueConsumer.TO, String.class),
				properties.get(EmailQueueConsumer.SUBJECT, String.class), properties.get("template", String.class),
				params);
	}
}

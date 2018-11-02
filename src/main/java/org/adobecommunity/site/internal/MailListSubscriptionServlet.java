package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.SendGridIntegration;
import org.adobecommunity.site.SendGridResponse;
import org.adobecommunity.site.impl.jobs.EmailQueueConsumer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.methods=POST",
        "sling.servlet.resourceTypes=adobecommunity-org/components/forms/maillistsubscribe",
        "sling.servlet.selectors=allowpost" })
public class MailListSubscriptionServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -2405349798845262763L;
    private static final String PN_EMAIL = "email";
    private static final String PN_LIST_ID = "listId";
    private static final Logger log = LoggerFactory.getLogger(MailListSubscriptionServlet.class);

    @Reference
    private transient JobManager jobManager;

    @Reference
    private transient SendGridIntegration sendGrid;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String referer = request.getHeader("referer");
        if (referer.contains("?")) {
            referer = referer.substring(0, referer.indexOf('?'));
        }

        String email = request.getParameter(PN_EMAIL);
        try {

            SendGridResponse apiResponse = sendGrid.createUser(email);

            ValueMap properties = request.getResource().getValueMap();
            Map<String, Object> emailData = new HashMap<>();
            emailData.put(PN_EMAIL, email);
            String responseStr = apiResponse.toString();

            int listId = properties.get(PN_LIST_ID, -1);
            if (listId != -1) {
                responseStr += "\n\n" + sendGrid.addToList(apiResponse.getId(), listId);
            }
            emailData.put("apiresponse", responseStr);

            log.debug("Sending confirmation email");
            EmailQueueConsumer.queueMessage(jobManager, email, properties.get("confirmationsubject", String.class),
                    properties.get("confirmationmessage", String.class), emailData);

            log.debug("Sending info email");
            EmailQueueConsumer.queueMessage(jobManager, properties.get("inforecipient", String.class),
                    properties.get("infosubject", String.class), properties.get("infomessage", String.class),
                    emailData);

        } catch (Exception e) {
            log.error("Exception subscribing user: " + email, e);
        }

        response.sendRedirect(referer + "?res=subscribed");
    }

}

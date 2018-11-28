package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.AEMDispatcherScanConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.cms.PageManager;
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
        "sling.servlet.resourceTypes=adobecommunity-org/components/forms/dispatchersecurityscanner",
        "sling.servlet.selectors=allowpost" })
public class DispatcherSecurityScanServlet extends SlingAllMethodsServlet {

    public static final String PARAM_PAGEPATH = "pagepath";

    public static final String PARAM_DOMAIN = "domain";

    public static final String PARAM_PROTOCOL = "protocol";

    public static final String PARAM_USER = "user";

    private static final long serialVersionUID = -1265699330904067650L;

    private static final Logger log = LoggerFactory.getLogger(DispatcherSecurityScanServlet.class);

    @Reference
    private transient JobManager jobManager;

    @Reference
    private transient UserGeneratedContentService ugcService;

    private static final UGCBucketConfig BUCKET_CONFIG = new UGCBucketConfig();
    static {
        BUCKET_CONFIG.setAction(APPROVE_ACTION.PUBLISH);
        BUCKET_CONFIG.setBucket("agc/adobecommunity-org/dispatcherscan");
        BUCKET_CONFIG.setContentType(CONTENT_TYPE.OTHER);
        BUCKET_CONFIG.setPathDepth(2);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String mappedPath = request.getResourceResolver().map(request,
                request.getResource().adaptTo(PageManager.class).getPage().getPath()) + ".html";

        String protocol = request.getParameter(PARAM_PROTOCOL);
        String domain = request.getParameter(PARAM_DOMAIN);
        String pagepath = request.getParameter(PARAM_PAGEPATH);

        if (StringUtils.isNotBlank(protocol) && StringUtils.isNotBlank(domain) && StringUtils.isNotBlank(pagepath)) {
            Resource container = ugcService.createUGCContainer(request, BUCKET_CONFIG, domain, null);
            ResourceResolver resolver = container.getResourceResolver();
            resolver.create(container, JcrConstants.JCR_CONTENT, new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put(PARAM_PROTOCOL, protocol);
                    put(PARAM_DOMAIN, domain);
                    put(PARAM_PAGEPATH, pagepath);
                    put("status", "Queued");
                    put("requested", Calendar.getInstance());
                    put(PARAM_USER, request.getResourceResolver().getUserID());
                }
            });
            resolver.commit();

            jobManager.addJob(AEMDispatcherScanConsumer.TOPIC, new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put(AEMDispatcherScanConsumer.PARAM_CONFIG, container.getPath());
                    put(AEMDispatcherScanConsumer.PARAM_FORM, request.getResource().getPath());
                }
            });

            response.sendRedirect(mappedPath + container.getPath() + "?res=success");
        } else {
            log.debug("Not all required parameters provided");
            response.sendRedirect(mappedPath + "?err=req");
        }

    }

}

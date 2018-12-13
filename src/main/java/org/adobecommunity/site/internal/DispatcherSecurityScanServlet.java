package org.adobecommunity.site.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.adobecommunity.site.impl.jobs.AEMDispatcherScanConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
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
            if (isUnderLimit(request.getResourceResolver())) {
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
                response.sendRedirect(mappedPath + "?err=limit");
            }
        } else {
            log.debug("Not all required parameters provided");
            response.sendRedirect(mappedPath + "?err=req");
        }

    }

    private boolean isUnderLimit(ResourceResolver resourceResolver) {
        JackrabbitSession session = (JackrabbitSession) resourceResolver.adaptTo(Session.class);
        try {
            final UserManager userManager = session.getUserManager();
            User user = (User) userManager.getAuthorizable(resourceResolver.getUserID());
            log.debug("Using user {}", user);
            Iterator<Group> groups = user.memberOf();
            while (groups.hasNext()) {
                Group group = groups.next();
                if ("administrators".equals(group.getID()) || "leadership".equals(group.getID())) {
                    log.trace("User is administrator or leadership memeber");
                    return true;
                }
            }
            String level = "Free";
            Value[] lProp = user.getProperty("profile/level");
            if (lProp != null && lProp.length > 0) {
                level = lProp[0].getString();
                log.trace("Loaded level {}", level);

            }
            if ("Free".equals(level)) {
                Iterator<Resource> checks = resourceResolver.findResources(
                        "SELECT * FROM [sling:UGC] WHERE ISDESCENDANTNODE([/etc/usergenerated/agc/adobecommunity-org/dispatcherscan]) AND [jcr:content/user]='"
                                + resourceResolver.getUserID() + "'",
                        Query.JCR_SQL2);
                for (int i = 0; checks.hasNext(); i++) {
                    checks.next();
                    if (i >= 4) {
                        log.trace("User is at / over limit!");
                        return false;
                    }
                }
                log.trace("User is below limit");
                return true;
            } else {
                log.trace("User is paid member");
                return true;
            }
        } catch (RepositoryException e) {
            log.error("Failed to check user limits due to unexpected exception", e);
            return true;
        }
    }

}

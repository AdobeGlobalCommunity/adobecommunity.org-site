package org.adobecommunity.site.models;

import java.util.Collections;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class UserProfileRequest {

    private static final Logger log = LoggerFactory.getLogger(UserProfileRequest.class);

    private SlingHttpServletRequest request;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver serviceResolver;

    public UserProfileRequest(SlingHttpServletRequest request) {
        this.request = request;
    }

    public Profile getJobProfile() throws LoginException {
        serviceResolver = resolverFactory
                .getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "usersearch"));
        Resource profileResource = serviceResolver
                .getResource("/home/users" + request.getRequestPathInfo().getSuffix());
        log.debug("Retrieved profile resource {} for suffix {}", profileResource,
                request.getRequestPathInfo().getSuffix());
        if (profileResource != null) {
            return profileResource.getChild("jobprofile").adaptTo(Profile.class);
        } else {
            return null;
        }
    }

    public Profile getCompanyProfile() throws LoginException {
        serviceResolver = resolverFactory
                .getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "usersearch"));
        Resource profileResource = serviceResolver
                .getResource("/home/users" + request.getRequestPathInfo().getSuffix());
        log.debug("Retrieved profile resource {} for suffix {}", profileResource,
                request.getRequestPathInfo().getSuffix());
        if (profileResource != null) {
            return profileResource.getChild("companyprofile").adaptTo(Profile.class);
        } else {
            return null;
        }
    }

    public boolean isClosed() {
        serviceResolver.close();
        return true;
    }
}

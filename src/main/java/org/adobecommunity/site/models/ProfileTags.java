package org.adobecommunity.site.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class)
public class ProfileTags {

    @ValueMapValue
    public String profileproperty;

    private SlingHttpServletRequest request;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    @ValueMapValue
    public String tagpath;

    public ProfileTags(SlingHttpServletRequest request) {
        this.request = request;
    }

    public List<String> getTags() throws LoginException {
        ResourceResolver serviceResolver = resolverFactory
                .getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "usersearch"));
        Resource profile = serviceResolver.getResource("/home/users" + request.getRequestPathInfo().getSuffix());
        List<String> tags = null;
        if (profile != null) {
            tags = Arrays.stream(profile.getValueMap().get(profileproperty, new String[0]))
                    .map(t -> getTagTitle(tagpath, t)).collect(Collectors.toList());
        }
        serviceResolver.close();
        return tags;

    }

    private String getTagTitle(String parent, String tagId) {
        return java.util.Optional.ofNullable(request.getResourceResolver().getResource(parent + "/" + tagId))
                .map(r -> r.getValueMap().get(CMSConstants.PN_TITLE, "")).orElse(null);
    }
}

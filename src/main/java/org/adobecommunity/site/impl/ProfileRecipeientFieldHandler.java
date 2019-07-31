package org.adobecommunity.site.impl;

import java.util.Collections;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.reference.forms.FieldHandler;
import org.apache.sling.cms.reference.forms.FormException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FieldHandler.class)
public class ProfileRecipeientFieldHandler implements FieldHandler {

    private static final Logger log = LoggerFactory.getLogger(ProfileRecipeientFieldHandler.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public boolean handles(Resource fieldResource) {
        return "adobecommunity-org/components/forms/profilerecipient".equals(fieldResource.getResourceType());
    }

    @Override
    public void handleField(SlingHttpServletRequest request, Resource fieldResource, Map<String, Object> formData)
            throws FormException {
        try {
            ResourceResolver serviceResolver = resolverFactory.getServiceResourceResolver(
                    Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "usersearch"));
            Resource profileResource = serviceResolver
                    .getResource("/home/users" + request.getParameter("profilerecipient"));
            log.debug("Got profile resource: {}", profileResource);
            formData.put("recipient", profileResource.getValueMap().get("rep:principalName", ""));
            log.debug("Set recipient to: {}", formData.get("recipient"));
        } catch (NullPointerException | LoginException e) {
            throw new FormException("Failed to populate profile recipient", e);
        }
    }

}

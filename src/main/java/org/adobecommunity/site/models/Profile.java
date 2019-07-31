package org.adobecommunity.site.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class Profile {

    @ValueMapValue
    @Optional
    private String about = "";

    @ValueMapValue
    @Optional
    private String availability = "";

    @ValueMapValue
    @Optional
    private String[] certifications = new String[0];

    @ValueMapValue
    @Optional
    private String[] experience = new String[0];

    @ValueMapValue
    @Optional
    private String interested = "";

    @ValueMapValue
    @Optional
    private String name = "";

    @ValueMapValue
    @Optional
    private String period = "";

    private Resource resource;

    @ValueMapValue
    @Optional
    private String[] roles = new String[0];

    @ValueMapValue
    @Optional
    private String[] specializations = new String[0];

    @ValueMapValue
    @Optional
    private String status = "";

    public Profile(Resource resource) {
        this.resource = resource;
    }

    private String generateSkills() {
        if (isCompany()) {
            return Arrays.stream(experience).collect(Collectors.joining(",")) + ","
                    + Arrays.stream(specializations).collect(Collectors.joining(","));
        } else {
            return Arrays.stream(experience).collect(Collectors.joining(",")) + ","
                    + Arrays.stream(certifications).collect(Collectors.joining(","));
        }
    }

    public String getAbout() {
        return about;
    }

    public String getAvailability() {
        return getTagTitle("/etc/taxonomy/form/availability", availability);
    }

    public String getId() {
        return resource.getParent().getPath().replace("/home/users", "");
    }

    public String getInterested() {
        return this.getTagTitle("/etc/taxonomy/form/job-type", interested);
    }

    public String getName() {
        if (isCompany()) {
            return name;
        } else {
            return resource.getParent().getChild("profile").getValueMap().get("name", "");
        }
    }

    public List<String> getProductLogos() {
        String skills = generateSkills();
        List<String> productImages = new ArrayList<>();
        if (skills.contains("adobe-ad-cloud")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/aac.png");
        }
        if (skills.contains("adobe-analytics")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/aa.png");
        }
        if (skills.contains("adobe-campaign")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/ac.png");
        }
        if (skills.contains("adobe-experience-manager")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/aem.png");
        }
        if (skills.contains("adobe-target")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/at.png");
        }
        if (skills.contains("magento")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/magento.png");
        }
        if (skills.contains("marketo")) {
            productImages.add("/static/clientlibs/adobecommunity-org/images/logos/marketo.png");
        }
        return productImages;
    }

    public String getPeriod() {
        return period;
    }

    public Resource getResource() {
        return resource;
    }

    public String getStatus() {
        return status;
    }

    private String getTagTitle(String parent, String tagId) {
        return java.util.Optional.ofNullable(resource.getResourceResolver().getResource(parent + "/" + tagId))
                .map(r -> r.getValueMap().get(CMSConstants.PN_TITLE, "")).orElse(null);
    }

    public boolean isCompany() {
        return "companyprofile".equals(resource.getName());
    }

    public boolean isFeatured() {
        if (isCompany()) {
            return specializations.length > 0;
        } else {
            return certifications.length > 0;
        }
    }
}

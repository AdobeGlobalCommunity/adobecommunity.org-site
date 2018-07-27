package org.adobecommunity.site.models;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class })
public class LocationItem {

	@ValueMapValue
	@Named("jcr:content/image")
	private String image;

	@ValueMapValue
	@Named("jcr:content/lat")
	private String lat;

	@ValueMapValue
	@Named("jcr:content/lng")
	private String lng;

	@ValueMapValue
	@Named("jcr:content/location")
	private String location;

	@ValueMapValue
	@Named("jcr:content/jcr:description")
	private String summary;

	@ValueMapValue
	@Named("jcr:content/jcr:title")
	private String title;

	@Self
	private Resource resource;

	public String getImage() {
		return image.replaceAll("/content/agc/adobecommunity-org", "");
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}

	public String getLocation() {
		return location;
	}

	public String getSummary() {
		return summary;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return resource.getPath().replaceAll("/content/agc/adobecommunity-org", "") + ".html";
	}
}

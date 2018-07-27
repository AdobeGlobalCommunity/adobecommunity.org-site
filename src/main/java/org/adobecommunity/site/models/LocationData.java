package org.adobecommunity.site.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { SlingHttpServletRequest.class }, resourceType = {
		"adobecommunity-org/components/general/locationmap" })
@Exporter(name = "jackson", extensions = "json")
public class LocationData {

	@SlingObject
	private ResourceResolver resolver;

	@ValueMapValue
	private String path;

	public List<LocationItem> getData() {
		List<LocationItem> data = new ArrayList<LocationItem>();
		Resource resource = resolver.getResource(path);
		for (Resource child : resource.getChildren()) {
			LocationItem item = child.adaptTo(LocationItem.class);
			if (item != null) {
				data.add(item);
			}
		}
		return data;
	}
}

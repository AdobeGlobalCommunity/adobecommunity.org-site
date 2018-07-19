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
		"adobecommunity-org/components/general/eventtable", "adobecommunity-org/components/general/eventcarousel",
		"adobecommunity-org/components/home/eventfeature" })
@Exporter(name = "jackson", extensions = "json")
public class EventData {

	@SlingObject
	private ResourceResolver resolver;

	@ValueMapValue
	private String[] paths;

	public List<EventItem> getData() {
		List<EventItem> data = new ArrayList<EventItem>();
		for (String path : paths) {
			Resource resource = resolver.getResource(path);
			for (Resource child : resource.getChildren()) {
				EventItem item = child.adaptTo(EventItem.class);
				if (item != null) {
					data.add(item);
				}
			}
		}
		return data;
	}
}

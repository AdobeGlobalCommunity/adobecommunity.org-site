package org.adobecommunity.site.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
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

	@ValueMapValue
	@Optional
	private String tag;

	public List<EventItem> getData() {
		List<EventItem> data = new ArrayList<>();
		for (String path : paths) {
			Resource resource = resolver.getResource(path);
			for (Resource child : resource.getChildren()) {
				if (child.getChild(JcrConstants.JCR_CONTENT) != null
						&& (StringUtils.isEmpty(tag) || ArrayUtils.contains(child.getChild(JcrConstants.JCR_CONTENT)
								.getValueMap().get(CMSConstants.PN_TAXONOMY, String[].class), tag))) {
					EventItem item = child.adaptTo(EventItem.class);
					if (item != null) {
						data.add(item);
					}
				}
			}
		}
		Collections.sort(data, new Comparator<EventItem>() {

			@Override
			public int compare(EventItem o1, EventItem o2) {
				return o1.getEventDate().compareTo(o2.getEventDate());
			}
		});
		return data;
	}

}

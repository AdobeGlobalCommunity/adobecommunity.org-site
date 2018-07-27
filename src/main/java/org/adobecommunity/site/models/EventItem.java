package org.adobecommunity.site.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class })
public class EventItem {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@ValueMapValue
	@Named("jcr:content/cta/text")
	private String cta;

	@ValueMapValue
	@Named("jcr:content/eventdate")
	private String eventDate;

	@ValueMapValue
	@Named("jcr:content/eventtime")
	private String eventTime;

	@ValueMapValue
	@Named("jcr:content/location")
	private String location;

	@ValueMapValue
	@Named("jcr:content/locationlink")
	private String locationLink;

	@ValueMapValue
	@Named("jcr:content/featured")
	@Optional
	private Boolean featured = new Boolean(false);

	@ValueMapValue
	@Named("jcr:content/sling:thumbnail")
	private String image;

	@ValueMapValue
	@Named("jcr:content/cta/link")
	private String link;

	@ValueMapValue
	@Named("jcr:content/presenter")
	private String presenter;

	@ValueMapValue
	@Named("jcr:content/jcr:description")
	private String summary;

	@ValueMapValue
	@Named("jcr:content/jcr:title")
	private String title;

	@ValueMapValue
	@Named("jcr:content/eventtype")
	private String type;

	private Calendar cal;

	@Self
	private Resource resource;

	private Calendar getCal() throws ParseException {
		if (cal == null) {
			cal = Calendar.getInstance();
			cal.setTime(DATE_FORMAT.parse(eventDate));
		}
		return cal;
	}

	public String getCta() {
		return cta;
	}

	public String getDayOfMonth() throws ParseException {
		return String.valueOf(getCal().get(Calendar.DAY_OF_MONTH));
	}

	public String getDayOfWeek() throws ParseException {
		return new SimpleDateFormat("EEEE").format(getCal().getTime());
	}

	public String getEventDate() {
		return eventDate;
	}

	public String getImage() {
		return image.replaceAll("/content/agc/adobecommunity-org", "");
	}

	public String getLink() {
		return link.replaceAll("/content/agc/adobecommunity-org", "");
	}

	public String getLocation() {
		return location;
	}

	public String getLocationLink() {
		return locationLink.replaceAll("/content/agc/adobecommunity-org", "");
	}

	public String getMonth() throws ParseException {
		return new SimpleDateFormat("MMM").format(cal.getTime());
	}

	public String getPresenter() {
		return presenter;
	}

	public String getSummary() {
		return summary;
	}

	public String getTime() {
		return eventTime;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getYear() throws ParseException {
		return String.valueOf(getCal().get(Calendar.YEAR));
	}

	public boolean isFeatured() {
		return featured;
	}

	public String getUrl() {
		return resource.getPath().replaceAll("/content/agc/adobecommunity-org", "") + ".html";
	}
}

package org.adobecommunity.site.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { SlingHttpServletRequest.class }, resourceType = { "sling:Page" })
@Exporter(name = "jackson", extensions = "json")
public class UserData {

	private static final Logger log = LoggerFactory.getLogger(UserData.class);

	@SlingObject
	private ResourceResolver resolver;

	public String getUserID() {
		return resolver.getUserID();
	}

	public boolean isLoggedIn() {
		return !"anonymous".equals(resolver.getUserID());
	}

	public Map<?, ?> getProfile() {
		Map<String, Object> profile = new HashMap<String, Object>();
		try {
			JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);

			Authorizable user = session.getUserManager().getAuthorizable(resolver.getUserID());
			Iterator<String> props = user.getPropertyNames("profile");

			while (props.hasNext()) {
				String key = props.next();
				Value[] val = user.getProperty(UserProfile.SUBPATH + key);
				if (val.length > 1) {
					String[] value = new String[val.length];
					for (int i = 0; i < val.length; i++) {
						value[i] = val[i].getString();
					}
					profile.put(key, value);
				} else if (val.length != 0) {
					profile.put(key, val[0].getString());
				}
			}
		} catch (Exception e) {
			log.debug("Exception reading profile", e);
		}
		return profile;

	}
}

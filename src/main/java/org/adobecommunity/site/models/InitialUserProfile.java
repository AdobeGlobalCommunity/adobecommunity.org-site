package org.adobecommunity.site.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;

@Model(adaptables = { SlingHttpServletRequest.class })
public class InitialUserProfile extends UserProfile {

	@Required
	private String email;

	@Required
	private String password;

	public InitialUserProfile(SlingHttpServletRequest request) throws IllegalArgumentException, IllegalAccessException {
		super(request);
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
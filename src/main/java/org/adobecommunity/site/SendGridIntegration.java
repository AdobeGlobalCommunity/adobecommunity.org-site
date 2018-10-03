package org.adobecommunity.site;

import javax.json.JsonObject;

import org.adobecommunity.site.models.InitialUserProfile;

public interface SendGridIntegration {

	JsonObject createUser(String email);
	
	JsonObject createUser(InitialUserProfile profile);
}

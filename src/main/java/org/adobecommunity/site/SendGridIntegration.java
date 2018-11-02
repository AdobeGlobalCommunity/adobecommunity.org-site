package org.adobecommunity.site;

import org.adobecommunity.site.models.InitialUserProfile;

public interface SendGridIntegration {

	SendGridResponse createUser(String email);
	
	SendGridResponse createUser(InitialUserProfile profile);
	
	String addToList(String userId, int listId);
}

package org.adobecommunity.site;

import org.adobecommunity.site.models.InitialUserProfile;

public interface StripeIntegration {

	String createSubscription(String stripeToken, InitialUserProfile profile) throws Exception;
}

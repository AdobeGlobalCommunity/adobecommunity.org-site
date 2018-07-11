package org.adobecommunity.site.impl;

import java.util.HashMap;
import java.util.Map;

import org.adobecommunity.site.StripeIntegration;
import org.adobecommunity.site.impl.StripeIntegrationImpl.StripeConfiguration;
import org.adobecommunity.site.models.InitialUserProfile;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

@Component(service = StripeIntegration.class)
@Designate(ocd = StripeConfiguration.class)
public class StripeIntegrationImpl implements StripeIntegration {

	private static final Logger log = LoggerFactory.getLogger(StripeIntegrationImpl.class);

	@ObjectClassDefinition(name = "Stripe Configuration")
	public @interface StripeConfiguration {

		@AttributeDefinition(name = "API Public Key")
		String apiPublicKey();

		@AttributeDefinition(name = "API Secret Key")
		String apiSecretKey();

		@AttributeDefinition(name = "Expert Plan ID")
		String expertPlanId();

		@AttributeDefinition(name = "Pro Plan ID")
		String proPlanId();
	}

	private StripeConfiguration config;

	@Activate
	public void activate(StripeConfiguration config) {
		this.setConfig(config);
	}

	@Override
	public String createSubscription(String stripeToken, InitialUserProfile profile) throws Exception {

		log.trace("createSubscription");

		Stripe.apiKey = config.apiSecretKey();

		Map<String, Object> params = new HashMap<>();
		params.put("email", profile.getEmail());
		params.put("source", stripeToken);
		Customer customer = Customer.create(params);

		Map<String, Object> item = new HashMap<>();
		if (profile.getLevel().equals("Pro")) {
			item.put("plan", config.proPlanId());
		} else if (profile.getLevel().equals("Expert")) {
			item.put("plan", config.expertPlanId());
		} else {
			throw new Exception("Invalid level " + profile.getLevel());
		}
		Map<String, Object> items = new HashMap<>();
		items.put("0", item);
		Map<String, Object> sParams = new HashMap<>();
		sParams.put("customer", customer.getId());
		sParams.put("items", items);
		Subscription.create(sParams);

		return customer.getId();
	}

	public StripeConfiguration getConfig() {
		return config;
	}

	public void setConfig(StripeConfiguration config) {
		this.config = config;
	}

}

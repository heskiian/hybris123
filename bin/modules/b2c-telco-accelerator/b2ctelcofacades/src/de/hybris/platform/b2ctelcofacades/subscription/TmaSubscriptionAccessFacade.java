/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;

import java.util.List;


/**
 * Facade gathering {@link TmaSubscriptionAccessData} related operations.
 *
 * @since 6.6
 */
public interface TmaSubscriptionAccessFacade
{
	/**
	 * Get an existing subscription access object for user
	 *
	 * @param principalUid
	 *           the unique identifier of the object which is intended to be updated
	 * @return a {@link List} of {@link TmaSubscriptionAccessData} belonging to the identified principal
	 */
	List<TmaSubscriptionAccessData> findSubscriptionAccessesByPrincipal(final String principalUid);

	/**
	 * Returns accessType of current user's {@link TmaSubscriptionBaseModel}.
	 *
	 * @param principalUid
	 *           the unique identifier of the object which is intended to be updated
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 * @param subscriberIdentity
	 *           unique identifier of the subscriber from the billing system
	 * @return {@link TmaSubscriptionAccessModel} containing accessType related data of current user
	 */
	TmaSubscriptionAccessModel getSubscriptionAccessByPrincipalAndSubscriptionBase(String principalUid, String billingSystemId,
			String subscriberIdentity);

	/**
	 * Get List of TmaSubscriptionAccessData for given Subscriber identity and billing system id
	 *
	 * @param subscriberIdentity
	 *           unique identifier of a SubscriptionAccess.
	 * @param billingSystemId
	 *           String instance for related Billing system
	 * @return {@link LIST}<{@link TmaSubscriptionAccessData}> Subscription Access data for given subscriber identity and
	 *         Billing system
	 */
	List<TmaSubscriptionAccessData> getSubscriptionAccessesBySubscriberIdentity(final String subscriberIdentity,
			final String billingSystemId);

}

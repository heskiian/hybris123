/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;

import java.util.List;


/**
 * Data Access Object for the {@link TmaSubscriptionAccessModel}.
 *
 * @since 6.6
 */
public interface TmaSubscriptionAccessDao
{
	/**
	 * Search for the subscription access object uniquely identified by the principal unique identifier, and the billing
	 * system id and subscriber identity unique combination.
	 *
	 * @param principalUid
	 *           unique identifier of the Principal entity
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 * @param subscriberIdentity
	 *           unique identifier of the subscriber from the billing system
	 * @return {@link TmaSubscriptionAccessModel} identified by the above parameters
	 */
	TmaSubscriptionAccessModel findSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity);

	/**
	 * Search for the subscription access objects with the subscriber identity given from a given billing system id.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of the subscriber from the given billing system
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel} belonging to the same subscriber
	 */
	List<TmaSubscriptionAccessModel> findSubscriptionAccessesBySubscriberIdentity(final String subscriberIdentity,
			final String billingSystemId);

	/**
	 * Search for the subscription access objects associated with the given principal uid, filtered by the given access
	 * type.
	 *
	 * @param principalUid
	 *           uid of the principal
	 * @param accessType
	 *           access type level for subscription access
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel} belonging to the identified principal, having the
	 *         given access type
	 */
	List<TmaSubscriptionAccessModel> findSubscriptionAccessesByType(final String principalUid, final TmaAccessType accessType);

	/**
	 * Search for the subscription access objects associated with the given principal uid
	 *
	 * @param principalUid
	 *           uid of the principal
	 *
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel} belonging to the identified principal
	 */
	List<TmaSubscriptionAccessModel> findSubscriptionAccessByPrincipalUid(String principalUid);

}

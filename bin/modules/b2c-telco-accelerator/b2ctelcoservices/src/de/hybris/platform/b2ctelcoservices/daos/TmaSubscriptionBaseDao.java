/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;


import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;

import java.util.Set;


/**
 * Data Access object for operations related to the {@link TmaSubscriptionBaseModel}.
 *
 * @since 6.6
 */
public interface TmaSubscriptionBaseDao
{
	/**
	 * Retrieves the {@link TmaSubscriptionBaseModel} uniquely identified based on the given details.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of the subscriber from the billing system
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 * @return {@link TmaSubscriptionBaseModel} for the subscriber given
	 */
	TmaSubscriptionBaseModel findSubscriptionBase(final String subscriberIdentity, final String billingSystemId);

	/**
	 * Retrieves all {@link TmaSubscriptionBaseModel}s available in the system
	 *
	 * @return a {@link Set} of {@link TmaSubscriptionBaseModel}s.
	 */
	Set<TmaSubscriptionBaseModel> findAllSubscriptionBases();

	/**
	 * Retrieves {@link TmaSubscriptionBaseModel} for given subscriberIdentity.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of {@link TmaSubscriptionBaseModel}
	 * @return TmaSubscriptionBaseModel for given inputs.
	 */
	TmaSubscriptionBaseModel findSubscriptionBaseByIdentity(String subscriberIdentity);
}

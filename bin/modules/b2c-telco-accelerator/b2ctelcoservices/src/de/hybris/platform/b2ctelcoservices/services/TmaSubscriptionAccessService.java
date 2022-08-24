/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.List;


/**
 * Service for operations related to the {@link TmaSubscriptionAccessModel}.
 *
 * @since 6.6
 */
public interface TmaSubscriptionAccessService
{
	/**
	 * Create a {@link TmaSubscriptionAccessModel} for a given {@link TmaSubscriptionBaseModel}.
	 *
	 * @param principalUid
	 * 		unique identifier of the {@link PrincipalModel}
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param subscriptionBase
	 * 		subscription base for which the access type is created
	 * @param accessType
	 * 		{@link TmaAccessType} the {@link PrincipalModel} has on the {@link TmaSubscriptionBaseModel}
	 * @return the newly created {@link TmaSubscriptionAccessModel}
	 */
	TmaSubscriptionAccessModel createSubscriptionAccessModel(String principalUid, String billingSystemId,
			TmaSubscriptionBaseModel subscriptionBase, TmaAccessType accessType);

	/**
	 * Search for all {@link TmaSubscriptionAccessModel}s belonging to the principal with the given uid
	 *
	 * @param principalUid
	 * 		uid of the principal
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel} belonging to the identified principal
	 */
	List<TmaSubscriptionAccessModel> getSubscriptionAccessesByPrincipalUid(final String principalUid);

	/**
	 * Search for the subscription access object uniquely identified by the principal unique identifier, and the billing
	 * system id and subscriber identity unique combination.
	 *
	 * @param principalUid
	 * 		unique identifier of the Principal entity
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 * @return {@link TmaSubscriptionAccessModel} identified by the above parameters
	 */
	TmaSubscriptionAccessModel getSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity);

	/**
	 * Search for all {@link TmaSubscriptionAccessModel}s belonging to the subscriber with the id given from the given
	 * billing system.
	 *
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param subscriberIdentity
	 * 		id of the subscriber for which the
	 * 		{@link de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel} should be retrieved
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel}s belonging to the same subscriber
	 */
	List<TmaSubscriptionAccessModel> getSubscriptionAccessesBySubscriberIdentity(final String billingSystemId,
			final String subscriberIdentity);

	/**
	 * Search for all {@link TmaSubscriptionAccessModel}s belonging to the principal with the uid given, filtered by
	 * given access type.
	 *
	 * @param principalUid
	 * 		uid of the principal
	 * @param accessType
	 * 		access type level for subscription access
	 * @return a {@link List} of {@link TmaSubscriptionAccessModel} belonging to the identified principal, having the
	 * given access type
	 */
	List<TmaSubscriptionAccessModel> getSubscriptionAccessesByType(final String principalUid, final TmaAccessType accessType);

	/**
	 * Update the {@link TmaAccessType} of the {@link TmaSubscriptionAccessModel} with the id given.
	 *
	 * @param principalUid
	 * 		unique identifier of the {@link TmaSubscriptionAccessModel} to be updated
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 * @param accessType
	 * 		the new value of the {@link de.hybris.platform.b2ctelcoservices.enums.TmaAccessType}
	 * @return the newly updated {@link TmaSubscriptionAccessModel}
	 */
	TmaSubscriptionAccessModel updateSubscriptionAccess(final String principalUid, final String billingSystemId,
			final String subscriberIdentity, final TmaAccessType accessType);

	/**
	 * Delete the {@link TmaSubscriptionAccessModel} with the id given.
	 *
	 * @param principalUid
	 * 		the unique identifier of the {@link TmaSubscriptionAccessModel} to be deleted
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 */
	void deleteSubscriptionAccess(final String principalUid, final String billingSystemId, final String subscriberIdentity);
}

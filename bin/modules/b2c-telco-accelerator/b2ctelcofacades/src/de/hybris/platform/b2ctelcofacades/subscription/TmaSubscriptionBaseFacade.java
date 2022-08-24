/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription;


import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;

import java.util.List;
import java.util.Set;


/**
 * Facade gathering {@link TmaSubscriptionBaseData} related operations.
 *
 * @since 6.6
 */
public interface TmaSubscriptionBaseFacade
{
	/**
	 * Create new {@link TmaSubscriptionBaseData} with the given details.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of the subscriber from the given billing system.
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 * @param billingAccountId
	 *           unique identifier of the billing account
	 * @return newly created SubscriptionBase
	 */
	TmaSubscriptionBaseData createSubscriptionBase(final String subscriberIdentity, final String billingSystemId,
			final String billingAccountId);

	/**
	 * Deletes the {@link TmaSubscriptionBaseModel} associated to a given subscriber identity and a billing system id.
	 *
	 * @param subscriberIdentity
	 *           unique identifier for subscriber from the billing system
	 * @param billingSystemId
	 *           unique identifier of the billing system
	 */
	void deleteSubscriptionBase(final String subscriberIdentity, final String billingSystemId);

	/**
	 * Create new {@link TmaSubscriptionBaseData} with the given details.
	 *
	 * @param billingAccountId
	 *           unique identifier of the billing account
	 * @return newly created SubscriptionBase
	 */
	TmaSubscriptionBaseData generateSubscriptionBase(final String billingAccountId);

	/**
	 * Determines the Subscribed Product Id of the main tariffs corresponding to the each of the subscription bases
	 * given.
	 *
	 * @param subscriptionBaseList
	 *           the list of subscription bases
	 * @return the set of Subscribed Product Id's or empty list if the input data is empty
	 */
	Set<String> getMainTariffSubscribedProductIdsForSubscriptionBases(List<TmaSubscriptionBaseData> subscriptionBaseList);

	/**
	 * Determines Subscription BaseData for given subscriberIdentity and billingSystemID
	 *
	 * @param subscriberIdentity
	 *           String instance is Unique identifier for subscriber
	 * @param billingSystemID
	 *           String instance is Unique identifier for billing system
	 * @return {@link TmaSubscriptionBaseData} instance for given subscriberIdentity and billingSystemID
	 */
	TmaSubscriptionBaseData getSubscriptionBaseForSubscriberIdentity(final String subscriberIdentity,
			final String billingSystemID);

	/**
	 * Retrieves a set containing all {@link TmaSubscriptionBaseData}s present in the system.
	 *
	 * @return {@link Set} of {@link TmaSubscriptionBaseData}s
	 */
	Set<TmaSubscriptionBaseData> getAllSubscriptionBases();

	/**
	 * Retrieve {@link TmaSubscriptionBaseData} for given subscriberIdentity.
	 *
	 * @param subscriberIdentity
	 *           unique identifier of {@link TmaSubscriptionBaseData}
	 * @return {@link TmaSubscriptionBaseData} for given parameters.
	 */
	TmaSubscriptionBaseData getSubscriptionBaseBySubscriberIdentity(final String subscriberIdentity);

	/**
	 * Get an existing subscription base object for user
	 *
	 * @param principalUid
	 *           the unique identifier of the object which is intended to be updated
	 * @return A {@link List} of {@link TmaSubscriptionBaseData} belonging to the identified principal
	 */
	List<TmaDetailedSubscriptionBaseData> findSubscriptionBasesByPrincipal(final String principalUid);

	/**
	 * Checks if subscriptionBases belongs to same {@link TmaBillingAgreementModel}
	 *
	 * @param subscriptionBases
	 *           list of {@link TmaSubscriptionBaseData}
	 * @return {@link boolean} returns true if subscriptionBases belongs to same billingAgreement else false
	 */
	boolean isIdenticalBillAgremment(List<TmaSubscriptionBaseData> subscriptionBases);

	/**
	 * Check if subscription base exists
	 *
	 * @param subscriptionBaseId
	 *           unique identifier of subscription base
	 * @return {@link boolean} returns true if subscriptionBase exists otherwise false
	 */
	boolean doesSubscriptionBaseExist(String subscriptionBaseId);

	/**
	 * Checks if user has access to subscription base
	 *
	 * @param subscriptionBaseId
	 *           unique identifier of subscription base
	 * @param userId
	 *           identifier of user
	 * @return {@link boolean} true if user has either owner or beneficiary access to subscription base, otherwise false
	 */
	boolean isSubscriptionBaseAccessibleToUser(String subscriptionBaseId, String userId);
}

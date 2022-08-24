/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Service for operations related to the {@link TmaSubscriptionBaseModel}.
 *
 * @since 6.6
 */
public interface TmaSubscriptionBaseService
{
	/**
	 * Gets the all subscription bases given a customer identifier.
	 *
	 * @param customerId
	 * 		the customerId
	 * @return the list of subscriptions
	 */
	List<TmaSubscriptionBaseModel> getSubscriptionBases(String customerId);

	/**
	 * Returns the {@link TmaSubscriptionBaseModel} identified by the a given subscriber identity and a given billing
	 * system id.
	 *
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @return {@link TmaSubscriptionBaseModel} if any exists
	 */
	TmaSubscriptionBaseModel getSubscriptionBase(final String subscriberIdentity, final String billingSystemId);

	/**
	 * Retrieves all {@link TmaSubscriptionBaseModel}s
	 *
	 * @return {@link Set} of all {@link TmaSubscriptionBaseModel}s available in system.
	 */
	Set<TmaSubscriptionBaseModel> getAllSubscriptionBases();

	/**
	 * Creates a {@link TmaSubscriptionBaseModel} with given details.
	 *
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 * @param billingAccountId
	 * 		unique identifier for the billing account
	 * @return newly created {@link TmaSubscriptionBaseModel}
	 */
	TmaSubscriptionBaseModel createSubscriptionBase(final String subscriberIdentity, final String billingSystemId,
			final String billingAccountId);

	/**
	 * Deletes the {@link TmaSubscriptionBaseModel} and associated {@link TmaSubscriptionAccessModel},
	 * {@link TmaSubscribedProductModel} identified by the given details.
	 *
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber for which the {@link TmaSubscriptionBaseModel} and dependencies
	 * 		should be deleted
	 * @param billingSystemId
	 * 		unique identifier of the billing system
	 */
	void deleteSubscriptionBase(final String subscriberIdentity, final String billingSystemId);

	/**
	 * generates a {@link TmaSubscriptionBaseModel} with given details.
	 *
	 * @param billingAccountId
	 * 		unique identifier for the billing account
	 * @return newly created {@link TmaSubscriptionBaseModel}
	 */
	TmaSubscriptionBaseModel generateSubscriptionBase(final String billingAccountId);

	/**
	 * Generates a new {@link TmaSubscriptionBaseModel} and associated {@link TmaSubscriptionAccessModel} for the given
	 * {@link TmaAccessType}
	 *
	 * @param customerId
	 * 		customer identifier
	 * @param billingAccountId
	 * 		unique identifier for the billing account
	 * @param accessType
	 * 		access type modifier
	 * @return newly created {@link TmaSubscriptionBaseModel}
	 */
	TmaSubscriptionBaseModel generateSubscriptionBase(final String customerId, final String billingAccountId,
			final TmaAccessType accessType);

	/**
	 * Determines the product code of the main tariff subscribed product for the given subscription base model
	 *
	 * @param subscriptionBaseModel
	 * 		the subscription base to be checked
	 * @return the product code of the main tariff subscribed product
	 */
	String getMainTariffProductCodeForSubscriptionBase(TmaSubscriptionBaseModel subscriptionBaseModel);

	/**
	 * Determines the Subscribed Product Id of the main tariff subscribed product for the given subscription base model
	 *
	 * @param subscriptionBaseModel
	 * 		the subscription base to be checked
	 * @return the Subscribed Product Id of the main tariff subscribed product
	 */
	String getMainTariffSubscribedProductIdForSubscriptionBase(TmaSubscriptionBaseModel subscriptionBaseModel);

	/**
	 * The list of {@link TmaSubscriptionBaseModel} given is grouped by billing agreement and BPO, obtaining a map of subscriptions.
	 * The key of the map is:
	 * - Combination of billing agreement id and BPO: for those subscriptions having the billing agreement and BPO
	 * attached
	 * - Billing agreement ID + 'NOBPO' + indexNo: for those subscriptions having billing agreement attached but no BPO
	 * At first the subscriptions are grouped by billing agreement. If a subscription has no billing
	 * agreement that subscription is not considered anymore. The list of subscriptions grouped by billing
	 * agreement is then grouped by BPO. Each list of subscription having the same billing agreement and same
	 * BPO will be added to the result map as an entry having the key determined as described above.
	 *
	 * @param subscriptionBases
	 * 		the list with subscriptions that will be processed
	 * @return the map grouping the subscription bases by billing agreement and BPO
	 */
	Map<String, List<TmaSubscriptionBaseModel>> groupSubscriptionsByBillingArrangementAndBpo(
			final List<TmaSubscriptionBaseModel> subscriptionBases);

	/**
	 * Return {@link TmaSubscriptionBaseModel} for given subscriptionBase.
	 *
	 * @param subscriberIdentity
	 * 		unique identifier of the subscriber from the billing system
	 * @return TmaSubscriptionBaseModel for given subscriberIdentity
	 */
	TmaSubscriptionBaseModel getSubscriptionBaseByIdentity(String subscriberIdentity);

	/**
	 * Determines the Subscribed Product Id of the main tariffs corresponding to the each of the subscription bases
	 * given.
	 *
	 * @param subscriptionBaseModelList
	 *           the list of subscription bases
	 * @return the set of Subscribed Product Id's or empty list if the input data is empty
	 */
	Set<String> getMainTariffSubscribedProductIdsForSubscriptionBases(List<TmaSubscriptionBaseModel> subscriptionBaseModelList);

	/** Checks if SubscriptionBase exist for given subscriberIdentity
	 * @param subscriberIdentity
	 *          SubscriptionBase identifier 
	 * @return true if TmaSubscriptionBaseModel exists for given subscriberIdentity else false
	 */
	boolean doesSubscriptionBaseExist(String subscriberIdentity);
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;


import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Service handling business logic around all customer inventory entities
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel},
 * {@link TmaSubscriptionBaseModel},
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel}
 *
 * @since 6.7
 */
public interface TmaCustomerInventoryService
{
	/**
	 * Creates a new billing account for the given customer and a subscription base having the given access type.
	 *
	 * @param customerId
	 * 		customer identifier
	 * @param accessType
	 * 		subscription access type
	 * @return {@link TmaSubscriptionBaseModel} returns TmaSubscriptionBaseModel
	 */
	TmaSubscriptionBaseModel createNewSubscriptionHierarchy(final String customerId, final TmaAccessType accessType);

	/**
	 * Returns the list of subscribed products from all customer's subscriptions.
	 *
	 * @param customerModel
	 * 		customer to be considered
	 * @return list of all customer subscribed products
	 */
	List<TmaSubscribedProductModel> getAllSubscribedProducts(final CustomerModel customerModel);

	/**
	 * The list of subscription selections given is grouped by billing agreement and BPO, obtaining a map of subscription
	 * selection lists. The key of the map is:
	 * - Combination of billing agreement id and BPO: for those subscriptions having the billing agreement and BPO
	 * attached
	 * - Billing agreement ID + 'NOBPO' + indexNo: for those subscriptions having billing agreement attached but no BPO
	 * At first the subscription selections are grouped by billing agreement. If a subscription selection has no billing
	 * agreement that subscription is not considered anymore. The list of subscription selections grouped by billing
	 * agreement is then grouped by BPO. Each list of subscription selections having the same billing agreement and same
	 * BPO will be added to the result map as an entry having the key determined as described above.
	 *
	 * @param subscriptionBases
	 * 		the list with subscription selections that will be processed
	 * @return the subscription selections map grouping the subscription bases by billing agreement and BPO
	 */
	Map<String, List<TmaSubscriptionBaseModel>> groupSubscriptionsByBillingArrangementAndBpo(
			final List<TmaSubscriptionBaseModel> subscriptionBases);

	/**
	 * Retrieves the {@link TmaProcessType}s for which the current customer is eligible.
	 *
	 * @return eligible process types.
	 */
	Set<TmaProcessType> retrieveProcesses();

	/**
	 * Retrieves the {@link TmaProcessType}s for which the given customer is eligible.
	 * If no input customer id is given it will return the {@link TmaProcessType} for the current user on the session.
	 *
	 * @param customerId
	 * 		The id of the customer
	 * @return eligible process flows for the given customer; if customerId is empty return the processes for the current user.
	 */
	Set<TmaProcessType> retrieveProcessesByCustomerId(String customerId);

	/**
	 * Retrieves the {@link TmaSubscriptionBaseModel}s eligible for a given process.
	 *
	 * @param processType
	 * 		the given process type for which eligible subscription ids are retrieved
	 * @return the {@link TmaSubscriptionBaseModel} eligible for given process
	 */
	Set<TmaSubscriptionBaseModel> retrieveSubscriptionsForProcess(final TmaProcessType processType);

	/**
	 * Retrieves the {@link TmaSubscriptionBaseModel}s eligible for a given process and user.
	 *
	 * @param processType
	 * 		the given process type for which eligible subscription ids are retrieved
	 * @param userId
	 * 		the identifier of the user
	 * @return the {@link TmaSubscriptionBaseModel} eligible for given process and user
	 */
	Set<TmaSubscriptionBaseModel> retrieveSubscriptionsForProcess(final TmaProcessType processType, final String userId);

	/**
	 * Retrieves the {@link TmaSubscriptionBaseModel} eligible for a given process, billing system and subscriber identity
	 *
	 * @param subscriberIdentity
	 * 		the given subscriber identity for which eligible subscription is retrieved
	 * @param billingSystemId
	 * 		the given billing system id for which eligible subscription is retrieved
	 * @param processType
	 * 		the given process type for which eligible subscription is retrieved
	 * @return {@link TmaSubscriptionBaseModel}
	 */
	Optional<TmaSubscriptionBaseModel> retrieveEligibleSubscriptions(final String subscriberIdentity,
			final String billingSystemId, final TmaProcessType processType);

	/**
	 * Gets all the ProcessTypes that exist in the system.
	 *
	 * @return A list of processTypes.
	 */
	Set<TmaProcessType> getAllProcessTypes();

	/**
	 * Creates a {@link TmaCartSubscriptionInfoModel} from given subscriber info
	 *
	 * @param subscriberIdentity
	 * 		the identity of the subscriber
	 * @param billingId
	 * 		the identifier of the billing system
	 * @param subscriptionTermId
	 * 		the identifier of the subscription term
	 * @param processType
	 * 		the process type
	 * @return {@link TmaCartSubscriptionInfoModel}
	 */
	TmaCartSubscriptionInfoModel createNewCartSubscriptionInfo(final String subscriberIdentity,
			final String billingId, final String subscriptionTermId, final String processType);

	/**
	 * Gets a subscribedProduct by id for a given customer.
	 *
	 * @param subscribedProductId
	 * 		the id of the subscribedProduct.
	 * @param customer
	 * 		the customer the subscribedProduct belongs to.
	 * @return the subscribedProduct if found;
	 */
	Optional<TmaSubscribedProductModel> getSubscribedProductById(final String subscribedProductId, final CustomerModel customer);

	/**
	 * Checks if a given {@link TmaSubscribedProductModel} can be replaced by the selected {@link TmaProductOfferingModel}.
	 *
	 * @param subscribedProduct
	 * 		the subscribed product to be replaced.
	 * @param productOffering
	 * 		the new product offering.
	 * @return true if the given subscribedProduct can be replaced by the productOffering; false otherwise.
	 */
	boolean canReplaceSubscribedProductWithOffering(final TmaSubscribedProductModel subscribedProduct, final TmaProductOfferingModel productOffering);
}

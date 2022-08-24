/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.user;


import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionSelectionData;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * Facade gathering {@link TmaSubscriptionBaseData} related operations.
 *
 * @since 6.7
 */
public interface TmaCustomerFacade extends CustomerFacade
{
	/**
	 * Identifies the subscription data from the ones given eligible for the current logged in customer.
	 *
	 * @param subscriptionBases
	 * 		the list of subscriber identities
	 * @return the list of eligible subscription bases
	 */
	List<TmaSubscriptionBaseData> getValidSubscriptionDataForCustomer(final List<TmaSubscriptionBaseData> subscriptionBases);

	/**
	 * Returns the {@link TmaSubscriptionBaseData} if any matching subscription with given details is found through the eligible customer's subscriptions for given process.
	 *
	 * @param subscriberIdentity
	 * 		subscription's identity
	 * @param billingSystemId
	 * 		subscription's billing system
	 * @param processType
	 * 		the flow against which the subscription's eligibility is being checked
	 * @return the {@link TmaSubscriptionBaseData} if any matching subscription exists
	 */
	Optional<TmaSubscriptionBaseData> getEligibleSubscriptionData(String subscriberIdentity, String billingSystemId,
			TmaProcessType processType);

	/**
	 * Determines the {@link TmaSubscriptionBaseData} DTOs from the given string where subscriber identities and billing
	 * account ids are separated by ','.
	 *
	 * @param subscriberIdentities
	 * 		the string value containing multiple subscriber identities and billing system ids
	 * @return the list of subscription base entities
	 */
	List<TmaSubscriptionBaseData> getSubscriptionBasesFromString(final String subscriberIdentities);

	/**
	 * Retrieves the {@link TmaProcessType}s for which the current customer is eligible.
	 *
	 * @return eligible process types.
	 */
	Set<TmaProcessType> retrieveEligibleProcessTypes();

	/**
	 * Retrieves the {@link TmaProcessType}s for which the given customer is eligible.
	 * If no input customer id is given it will return the {@link TmaProcessType} for the current user on the session.
	 *
	 * @param customerId
	 * 			The id of the customer
	 * @return eligible process flows for the given customer; if customerId is empty return the processes for the current user.
	 */
	Set<TmaProcessType> retrieveEligibleProcessTypesByCustomerId(String customerId);

	/**
	 * Retrieves customer's eligible subscriptions based on given {@link TmaProcessType}.
	 *
	 * @param processType
	 * 		the process type for which eligible subscriptions will be determined
	 * @return the eligible {@link TmaSubscriptionBaseData}
	 */
	Set<TmaSubscriptionBaseData> retrieveEligibleSubscriptions(final TmaProcessType processType);

	 /**
	  * Retrieves the provided customer's eligible subscriptions based on given {@link TmaProcessType}.
	  *
	  * @param processType
	  * 		the process type for which eligible subscriptions will be determined
	  * @param userId
	  * 		the identifier of the customer
	  * @return the eligible {@link TmaSubscriptionBaseData}
	  */
	 Set<TmaSubscriptionBaseData> retrieveEligibleSubscriptions(final TmaProcessType processType, final String userId);

	/**
	 * Retrieves all eligible subscription selections for a process, grouped by
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementModel}
	 * and {@link de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel}, for given customer UID. Each
	 * billing agreement may generate 1 or more subscription selection groups, depending on the number of different BPOs
	 * assigned to the subscription bases part of the billing agreement. All subscriptions part of the same billing
	 * agreement and with same BPO will form a selection group.
	 * If no subscription available for given customer an empty map will be returned.
	 *
	 * @param processType
	 * 		the process for which eligible subscriptions are retrieved
	 * @return {@link Map} of {@link TmaSubscriptionSelectionData} including some information from subscription base and
	 * billing agreement
	 */
	Map<String, List<TmaSubscriptionSelectionData>> determineEligibleSubscriptionSelections(final TmaProcessType processType);

	/**
	 * Determines the product codes of the main tariffs corresponding to the each of the given {@link TmaSubscriptionBaseData}.
	 *
	 * @param subscriptionDatas
	 * 		the list of subscription bases
	 * @return the set of product codes or empty list if the input data is empty
	 */
	Set<String> getMainTariffProductCodesForSubscriptionBases(final List<TmaSubscriptionBaseData> subscriptionDatas);

	/**
	 * Retrieves the credit card payment methods for the provided userId.
	 *
	 * @param userId
	 * 		Unique identifier of the user
	 * @return List of {@link CCPaymentInfoData} containing the payment methods for the provided user
	 */
	List<CCPaymentInfoData> getCcPaymentInfosForUser(String userId);

	/**
	 * Updates the {@link TmaEligibilityContext}s for the current customer.
	 *
	 * @param forceRefresh
	 * 		flag which indicates whether the eligibility contexts should be recreated before updating.
	 */
	void updateEligibilityContexts(final boolean forceRefresh);

	/**
	 * This method verifies if the authentication for current context has role provided
	 *
	 * @param role
	 * 		The input role.
	 * @return True if current client has the input role, otherwise false.
	 */
	boolean hasCurrentAuthenticationRoleProvided(String role);

	/**
	 * Gets all the ProcessTypes that exist in the system.
	 * @return A list of processTypes.
	 */
	Set<TmaProcessType> getAllProcessTypes();

	/**
	 * Updates the eligibility context for the provided user.
	 *
	 * @param userId
	 * 		The identifier of the user
	 */
	void updateEligibilityContextsByCustomer(String userId);

}

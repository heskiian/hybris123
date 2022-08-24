/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;

import java.util.Date;
import java.util.List;


/**
 * Service for operations related to the {@link TmaSubscribedProductModel}.
 *
 * @since 6.6
 */
public interface TmaSubscribedProductService
{
	/**
	 * Enhances the provided {@param subscribedProductModel} by setting it a generated id and persists it.
	 *
	 * @param subscribedProductModel
	 * 		source which needs to be persisted
	 * @return persisted source, with generated id
	 */
	TmaSubscribedProductModel createSubscribedProduct(final TmaSubscribedProductModel subscribedProductModel);

	/**
	 * Retrieves the {@link TmaSubscribedProductModel} for given billingSystemId and billingSubscriptionId.
	 *
	 * @param billingSystemId
	 * 		the id of the billing system
	 * @param billingSubscriptionId
	 * 		the id of the billing subscription
	 * @return {@link TmaSubscribedProductModel} object identified by given billingSystemId and billingSubscriptionId
	 */
	TmaSubscribedProductModel findSubscribedProduct(final String billingSystemId, final String billingSubscriptionId);

	/**
	 * Retrieves the {@link TmaSubscribedProductModel} for the given subscribedProductId.
	 *
	 * @param subscribedProductId
	 * 		the id of the subscribed product
	 * @return {@link TmaSubscribedProductModel} object identified by given subscribedProductId
	 */
	TmaSubscribedProductModel findSubscribedProductById(final String subscribedProductId);

	/**
	 * Deletes the subscribed product for given billingSystemId and billingSubscriptionId.
	 *
	 * @param billingSystemId
	 * 		the id of the billing system
	 * @param billingSubscriptionId
	 * 		the id of the billing subscription
	 */
	void deleteSubscribedProduct(final String billingSystemId, final String billingSubscriptionId);

	/**
	 * Retrieve {@link List} of {@link TmaSubscribedProductModel} identified by given paymentMethodId
	 *
	 * @param paymentMethodId
	 * 		The payment method ID for subscribed product
	 * @return {@link List} of {@link TmaSubscribedProductModel} which has paymentMethodId
	 */
	List<TmaSubscribedProductModel> findSubscriptionsForPaymentMethod(final String paymentMethodId);

	/**
	 * Creates a new {@link TmaSubscribedProductModel}.
	 *
	 * @param startDate
	 * 		subscription start date
	 * @param billingSystemId
	 * 		billing system identifier
	 * @param subscriptionStatus
	 * 		subscription status
	 * @return the new created subscribed product
	 */
	TmaSubscribedProductModel createNewSubscribedProduct(final Date startDate, final String billingSystemId, final String
			subscriptionStatus);

	/**
	 * Get the Subscription Product End date .
	 *
	 * @param frequencyCode
	 * 		Code for {@link TermOfServiceFrequency}
	 * @param duration
	 * 		Duration of Contract of the Service
	 * @param startDate
	 * 		Start date of Contract of the Service
	 * @return the End Date of the Service
	 */
	Date getSubscriptionServiceEndDate(final String frequencyCode, final Integer duration, final Date startDate);

}

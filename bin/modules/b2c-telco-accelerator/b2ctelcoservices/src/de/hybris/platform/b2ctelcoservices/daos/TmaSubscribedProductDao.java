/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;

import java.util.List;


/**
 * Data Access object for operations related to the {@link TmaSubscribedProductModel}.
 *
 * @since 6.6
 */
public interface TmaSubscribedProductDao
{
	/**
	 * Retrieves the {@link TmaSubscribedProductModel} identified by given billingSystemId and billingSubscriptionId.
	 *
	 * @param billingSystemId
	 *           the id of the billing system
	 * @param billingSubscriptionId
	 *           the id of the billing subscription
	 * @return {@link TmaSubscribedProductModel} object for given billingSystemId and billingSubscriptionId
	 */
	TmaSubscribedProductModel findSubscribedProduct(final String billingSystemId, final String billingSubscriptionId);

	/**
	 * Retrieves the {@link TmaSubscribedProductModel} identified by given subscribedProductId.
	 *
	 * @param subscribedProductId
	 *           the id of the subscribed product
	 *
	 * @return {@link TmaSubscribedProductModel} object for given subscribedProductId
	 */
	TmaSubscribedProductModel findSubscribedProductById(String subscribedProductId);

	/**
	 * Retrieve {@link List} of {@link TmaSubscribedProductModel} identified by given paymentMethodId
	 *
	 * @param paymentMethodId
	 *           The payment method ID for subscribed product
	 * @return {@link List} of {@link TmaSubscribedProductModel} which has paymentMethodId
	 */

	List<TmaSubscribedProductModel> findSubscriptionsForPaymentMethod(final String paymentMethodId);
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess;

import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * Mock processing the new subscribed products from an order.
 *
 * @since 1810
 */
public interface MockCreateCpiService
{

	/**
	 * Process each order entry from the order based on order entry process type.Supported process types:
	 * ACQUISITION: creates new customer inventory entities
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel},
	 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementModel} for assigning the new created
	 * {@link TmaSubscribedProductModel}.
	 *
	 * @param orderModel
	 * 		current order to be processed
	 */
	void mockCreateSubscriptionsFromOrder(final OrderModel orderModel);

}

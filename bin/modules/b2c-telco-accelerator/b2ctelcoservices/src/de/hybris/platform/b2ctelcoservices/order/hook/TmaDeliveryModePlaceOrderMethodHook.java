/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.hook;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;


/**
 * Cart validation hook to verify if delivery mode is required
 *
 * @since 1911
 */
public class TmaDeliveryModePlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	@Override
	public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException
	{
		// no implementation needed
	}

	@Override
	public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if (parameter.getCart().getDeliveryAddress() != null && parameter.getCart().getDeliveryMode() == null)
		{
			throw new InvalidCartException(
					"Cannot place order because cart '" + parameter.getCart().getCode() + "' does not have delivery mode.");
		}
	}

	@Override
	public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
	{
		// no implementation needed
	}
}

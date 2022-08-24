/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.hook;

import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;


/**
 * Cart validation hook to verify if cart contains eligibility errors
 *
 * @since 2003
 */
public class TmaEligibilityPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	private final TmaCartService tmaCartService;

	public TmaEligibilityPlaceOrderMethodHook(final TmaCartService tmaCartService)
	{
		this.tmaCartService = tmaCartService;
	}

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel)
			throws InvalidCartException
	{
		// no implementation needed
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final boolean cartHasInvalidProcess = tmaCartService.validateCartForEligibilityRules(parameter.getCart());

		if (cartHasInvalidProcess)
		{
			throw new InvalidCartException(
					"Cannot place order because cart '" + parameter.getCart().getCode() + "' has eligibility errors. ");
		}
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		// no implementation needed
	}

}

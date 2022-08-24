/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.hook;

import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart validation hook to verify if cart contains cardinality errors.
 *
 * @since 2011
 */
public class TmaBpoCardinalityPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{

	private TmaBpoCardinalityService bpoCardinalityService;

	public TmaBpoCardinalityPlaceOrderMethodHook(final TmaBpoCardinalityService bpoCardinalityService)
	{
		this.bpoCardinalityService = bpoCardinalityService;
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();

		final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap = getBpoCardinalityService()
				.verifyCardinalityForOrder(cartModel);
		boolean isCardinalityFulfilled = true;

		for (Map.Entry<AbstractOrderEntryModel, List<String>> mapEntry : validationMessagesMap.entrySet())
		{
			if (CollectionUtils.isNotEmpty(mapEntry.getValue()))
			{
				isCardinalityFulfilled = false;
				getBpoCardinalityService()
						.updateCardinalityValidationMessages(mapEntry.getKey(), validationMessagesMap.get(mapEntry.getKey()));
			}
		}

		if (!isCardinalityFulfilled)
		{
			throw new InvalidCartException(
					"Cannot place order because cart '" + parameter.getCart().getCode() + "' has cardinality errors.");
		}
	}

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel)
	{
		// no implementation needed
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
	{
		// no implementation needed
	}

	protected TmaBpoCardinalityService getBpoCardinalityService()
	{
		return bpoCardinalityService;
	}
}

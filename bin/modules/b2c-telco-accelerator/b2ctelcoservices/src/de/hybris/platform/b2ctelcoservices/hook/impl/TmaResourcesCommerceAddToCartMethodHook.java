/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;


/**
 * Resource specific add to cart operations.
 *
 * @since 1911
 */
public class TmaResourcesCommerceAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	private TmaCommerceCartResourceService tmaCommerceCartResourceService;

	public TmaResourcesCommerceAddToCartMethodHook(
			TmaCommerceCartResourceService tmaCommerceCartResourceService)
	{
		this.tmaCommerceCartResourceService = tmaCommerceCartResourceService;
	}

	/**
	 * Perform validation operations for cart entries containing resources.
	 *
	 * @param parameter
	 * 		cart parameter containing new cart entry details
	 */
	@Override
	public void beforeAddToCart(final CommerceCartParameter parameter)
	{
		List<TmaCartValidationResult> cartValidationResultList = getTmaCommerceCartResourceService().validateResources(parameter);

		for (TmaCartValidationResult cartValidationResult : cartValidationResultList)
		{
			if (!cartValidationResult.isValid())
			{
				throw new IllegalArgumentException(cartValidationResult.getMessage());
			}
		}
	}

	/**
	 * Updates the newly added order entry with resources from the {@link CommerceCartParameter}
	 *
	 * @param parameter
	 * 		details for the new cart entry, before to be added to cart
	 * @param result
	 * 		details for the new cart entry, after it was added to cart
	 */
	@Override
	public void afterAddToCart(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		// no implementation needed
	}

	protected TmaCommerceCartResourceService getTmaCommerceCartResourceService()
	{
		return tmaCommerceCartResourceService;
	}
}

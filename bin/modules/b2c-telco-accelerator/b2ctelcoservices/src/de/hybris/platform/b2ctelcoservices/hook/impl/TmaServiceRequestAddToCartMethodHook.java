/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * {@link TmaServiceRequestAddToCartMethodHook} specific add to cart operations.
 *
 * @since 2003
 */
public class TmaServiceRequestAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	private TmaPoService tmaPoService;

	public TmaServiceRequestAddToCartMethodHook(TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	/**
	 * Adds the appropriate operational product offering to the commerceCartParameter if necessary.
	 *
	 * @param parameters
	 * 		the commerceCartParameter
	 */
	@Override
	public void beforeAddToCart(CommerceCartParameter parameters)
	{
		if (parameters.getProduct() != null)
		{
			return;
		}
		parameters.setProduct(getTmaPoService().getProductOffering(parameters.getSubscribedProduct() != null ?
				parameters.getSubscribedProduct().getProductCode() : null, parameters.getAction()));
	}

	@Override
	public void afterAddToCart(CommerceCartParameter parameters, CommerceCartModification result)
	{
		// no implementation needed
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}
}

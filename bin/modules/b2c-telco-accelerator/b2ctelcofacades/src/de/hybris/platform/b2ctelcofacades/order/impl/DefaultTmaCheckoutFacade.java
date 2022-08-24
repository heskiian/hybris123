/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.impl;

import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;

import org.springframework.util.Assert;


/**
 * Facade that extends {@link DefaultCheckoutFacade} with telco related functionality.
 *
 * @since 2007
 */
public class DefaultTmaCheckoutFacade extends DefaultCheckoutFacade
{
	@Override
	protected AddressModel getDeliveryAddressModelForCode(final String code)
	{
		Assert.notNull(code, "Parameter code cannot be null.");
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			for (final AddressModel address : getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel, false))
			{
				if (code.equals(address.getId()))
				{
					return address;
				}
			}
		}
		return null;
	}
}

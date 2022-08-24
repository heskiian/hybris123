/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link OrderData} with details from {@link CartData}.
 *
 * @since 1907
 */

public class TmaOrderDataToCartDataPopulator implements Populator<OrderData, CartData>
{
	@Override
	public void populate(OrderData orderData, CartData cartData) throws ConversionException
	{
		// TODO: populate data from roder to cart
	}
}

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.CartData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for  valid date attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1907
 */
public class ShoppingCartValidForAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		final TimePeriod timePeriod = new TimePeriod();
		if (source.getSaveTime() != null)
		{
			timePeriod.setStartDateTime(source.getSaveTime());
		}
		if (source.getExpirationTime() != null)
		{
			timePeriod.setEndDateTime(source.getExpirationTime());
		}
		target.setValidFor(timePeriod);
	}
}


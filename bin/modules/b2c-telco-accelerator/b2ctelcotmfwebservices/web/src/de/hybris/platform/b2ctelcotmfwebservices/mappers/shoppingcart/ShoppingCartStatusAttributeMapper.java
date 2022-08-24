/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.enums.OrderStatus;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for status attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 2007
 */
public class ShoppingCartStatusAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	@Override
	public void populateTargetAttributeFromSource(final CartData source, final ShoppingCart target, final MappingContext context)
	{
		final OrderStatus status = source.getStatus();
		if (!ObjectUtils.isEmpty(status))
		{
			target.setStatus(status.toString());
		}
	}
}

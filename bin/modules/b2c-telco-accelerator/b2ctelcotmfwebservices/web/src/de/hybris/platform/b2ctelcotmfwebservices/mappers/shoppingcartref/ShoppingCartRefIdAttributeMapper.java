/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcartref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartRef;
import de.hybris.platform.commercefacades.order.data.CartData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link CartData} and {@link ShoppingCartRef}
 *
 * @since 1907
 */
public class ShoppingCartRefIdAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCartRef>
{
	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCartRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}

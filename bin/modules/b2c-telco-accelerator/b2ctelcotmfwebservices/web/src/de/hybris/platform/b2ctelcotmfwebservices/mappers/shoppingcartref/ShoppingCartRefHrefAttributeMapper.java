/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcartref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartRef;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link CartData} and {@link ShoppingCartRef}
 *
 * @since 1907
 */
public class ShoppingCartRefHrefAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCartRef>
{
	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCartRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.SHOPPING_CART_API_URL + source.getCode());
		}
	}
}

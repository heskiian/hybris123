/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for cart item attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1907
 */
public class ShoppingCartItemAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getEntries()))
		{
			target.setCartItem(getMapperFacade().mapAsList(source.getEntries(), CartItem.class, context));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

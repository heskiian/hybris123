/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for cartCosts attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1907
 */
public class ShoppingCartCartCostsAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		List<CartPrice> cartPrices = new ArrayList<>();
		if (source.getPrice() == null)
		{
			return;
		}
		cartPrices.add(getMapperFacade().map(source.getPrice(), CartPrice.class));
		target.setCartCosts(cartPrices);
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

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps itemPrice attribute for cartItem between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1907
 */
public class CartItemPriceAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	private MapperFacade mapperFacade;

	private Map<String, Class<CartPrice>> priceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target, MappingContext context)
	{
		if (source.getPrice() == null)
		{
			return;
		}
		final List<CartPrice> outputPrices = new ArrayList<>();
		final TmaAbstractOrderPriceData tmaAbstractOrderPriceData = source.getPrice();
		final CartPrice cartPrice = getMapperFacade()
				.map(tmaAbstractOrderPriceData, priceTypeDtoMap.get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
						context);
		outputPrices.add(cartPrice);
		target.setItemPrice(outputPrices);
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

	public Map<String, Class<CartPrice>> getPriceTypeDtoMap()
	{
		return priceTypeDtoMap;
	}

	@Required
	public void setPriceTypeDtoMap(Map<String, Class<CartPrice>> priceTypeDtoMap)
	{
		this.priceTypeDtoMap = priceTypeDtoMap;
	}
}

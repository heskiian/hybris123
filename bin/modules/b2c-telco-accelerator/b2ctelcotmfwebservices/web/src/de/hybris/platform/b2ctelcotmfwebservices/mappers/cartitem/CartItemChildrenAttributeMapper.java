/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for CartItem children attribute between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1907
 */
public class CartItemChildrenAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}
		List<CartItem> cartItems = new ArrayList<>();
		source.getEntries().forEach((OrderEntryData orderEntryData) -> {
			CartItem cartItem = getMapperFacade().map(orderEntryData, CartItem.class, context);
			cartItems.add(cartItem);
		});
		target.setCartItem(cartItems);
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

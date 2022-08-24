/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps product attributes for cartItem between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1907
 */
public class CartItemProductAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final CartItem target,
			final MappingContext context)
	{
		final Product product = getMapperFacade().map(source, Product.class, context);
		target.setProduct(product);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

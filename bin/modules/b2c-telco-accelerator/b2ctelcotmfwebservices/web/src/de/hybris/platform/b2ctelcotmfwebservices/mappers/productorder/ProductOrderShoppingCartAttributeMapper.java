/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartRef;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for attype attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 1907
 */
public class ProductOrderShoppingCartAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
			final MappingContext context)
	{
		if (source.getCart() == null) {
			return;
		}

		final ShoppingCartRef shoppingCartRef = getMapperFacade().map(source.getCart(), ShoppingCartRef.class, context);
		target.setShoppingCart(shoppingCartRef);
	}

	@Override
	public void populateSourceAttributeFromTarget(ProductOrder target, OrderData source,
			MappingContext context)
	{
		if (target.getShoppingCart() == null) {
			return;
		}

		CartData cartData = new CartData();
		cartData.setCode(target.getShoppingCart().getId());

		source.setCart(cartData);
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

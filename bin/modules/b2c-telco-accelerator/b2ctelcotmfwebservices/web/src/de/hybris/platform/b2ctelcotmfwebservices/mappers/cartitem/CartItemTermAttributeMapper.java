/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartTerm;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps term attribute between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1907
 */
public class CartItemTermAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target,
			MappingContext context)
	{

		if (source.getSubscriptionInfo() == null || source.getSubscriptionInfo().getSubscriptionTerm() == null)
		{
			return;
		}
		SubscriptionTermData subscriptionTerm = source.getSubscriptionInfo().getSubscriptionTerm();
		CartTerm cartTerm = getMapperFacade().map(subscriptionTerm, CartTerm.class, context);
		List<CartTerm> cartTerms = new ArrayList<>();
		cartTerms.add(cartTerm);
		target.setItemTerm(cartTerms);
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

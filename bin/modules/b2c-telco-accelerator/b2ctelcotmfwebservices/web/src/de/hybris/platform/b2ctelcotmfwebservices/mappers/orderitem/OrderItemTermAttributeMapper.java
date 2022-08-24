/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderTerm;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for orderItem children attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemTermAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		if (source.getSubscriptionInfo() == null || source.getSubscriptionInfo().getSubscriptionTerm() == null)
		{
			return;
		}

		SubscriptionTermData subscriptionTerm = source.getSubscriptionInfo().getSubscriptionTerm();
		OrderTerm orderTerm = getMapperFacade().map(subscriptionTerm, OrderTerm.class, context);

		List<OrderTerm> orderTerms = new ArrayList<>();
		orderTerms.add(orderTerm);

		target.setItemTerm(orderTerms);
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

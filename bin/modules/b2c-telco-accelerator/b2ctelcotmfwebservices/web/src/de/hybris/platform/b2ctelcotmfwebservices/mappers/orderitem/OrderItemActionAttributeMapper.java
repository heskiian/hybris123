/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ActionType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps action attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1911
 */
public class OrderItemActionAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public OrderItemActionAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderItem target,
			final MappingContext context)
	{
		final TmaCartSubscriptionInfoData subscriptionInfo = source.getSubscriptionInfo();
		if (subscriptionInfo != null && subscriptionInfo.getSubscribedProductAction() != null)
		{
			target.setAction(ActionType.valueOf(subscriptionInfo.getSubscribedProductAction().getCode()));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

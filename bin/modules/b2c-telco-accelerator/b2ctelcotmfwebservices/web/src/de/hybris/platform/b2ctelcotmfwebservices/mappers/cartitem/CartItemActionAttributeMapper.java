/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ActionType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps subscribedProductAction attribute between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1911
 */
public class CartItemActionAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, CartItem target, final MappingContext context)
	{
		final TmaCartSubscriptionInfoData subscriptionInfo = source.getSubscriptionInfo();
		if (subscriptionInfo != null && subscriptionInfo.getSubscribedProductAction() != null)
		{
			target.setAction(ActionType.valueOf(subscriptionInfo.getSubscribedProductAction().getCode()));
		}
	}
}

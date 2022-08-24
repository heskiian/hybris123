/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.subscribedproduct;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.SubscribedProductWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps name for id attribute between {@link OrderEntryData} and {@link SubscribedProductWsDTO}
 *
 * @since 1911
 */
public class TmaSubscribedProductNameAttributeMapper extends TmaAttributeMapper<OrderEntryData, SubscribedProductWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final SubscribedProductWsDTO target,
			final MappingContext context)
	{
		final TmaCartSubscriptionInfoData subscriptionInfo = source.getSubscriptionInfo();

		if (subscriptionInfo != null && StringUtils.isEmpty(subscriptionInfo.getSubscribedProductId()))
		{
			target.setName(source.getProduct().getName());
		}
		else
		{
			target.setName(null);
		}
	}
}

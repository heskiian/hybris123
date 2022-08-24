/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.product;

import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link OrderEntryData} and {@link Product}
 *
 * @since 1911
 */
public class ProductIdAttributeMapper extends TmaAttributeMapper<OrderEntryData, Product>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final Product target,
			final MappingContext context)
	{
		final TmaCartSubscriptionInfoData subscriptionInfo = source.getSubscriptionInfo();

		if (subscriptionInfo != null && StringUtils.isNotEmpty(subscriptionInfo.getSubscribedProductId()))
		{
			target.setId(subscriptionInfo.getSubscribedProductId());
		}
		else
		{
			target.setId(null);
		}
	}
}

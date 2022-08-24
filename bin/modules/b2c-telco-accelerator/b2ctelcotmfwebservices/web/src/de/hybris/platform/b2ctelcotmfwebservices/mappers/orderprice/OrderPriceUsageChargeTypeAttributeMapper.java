/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderTierUsageCompositePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for orderPrice usageChargeType attribute between
 * {@link TmaAbstractOrderTierUsageCompositePriceData} and {@link OrderPrice}
 *
 * @since 2007
 */
public class OrderPriceUsageChargeTypeAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderTierUsageCompositePriceData, OrderPrice>
{
	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderTierUsageCompositePriceData source, OrderPrice target,
			MappingContext context)
	{
		if (source.getUsageChargeType() != null && StringUtils.isNotEmpty(source.getUsageChargeType().getCode()))
		{
			target.setUsageChargeType(source.getUsageChargeType().getCode());
		}
	}
}

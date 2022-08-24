/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderTierUsageCompositePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageChargeType attribute between {@link TmaAbstractOrderTierUsageCompositePriceData} and
 * {@link CartPrice}
 *
 * @since 2007
 */
public class CartPriceUsageChargeTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderTierUsageCompositePriceData,
		CartPrice>
{
	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderTierUsageCompositePriceData source, CartPrice target,
			MappingContext context)
	{
		if (source.getUsageChargeType() != null && StringUtils.isNotEmpty(source.getUsageChargeType().getCode()))
		{
			target.setUsageChargeType(source.getUsageChargeType().getCode());
		}
	}
}

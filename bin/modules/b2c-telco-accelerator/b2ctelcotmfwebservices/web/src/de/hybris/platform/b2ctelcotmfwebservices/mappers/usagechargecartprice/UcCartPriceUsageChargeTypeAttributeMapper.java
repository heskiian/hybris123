/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagechargecartprice;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageChargeCartPrice;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageChargeType attribute between {@link TmaAbstractOrderUsageChargePriceData} and
 * {@link UsageChargeCartPrice}
 *
 * @since 1907
 * @deprecated since 2007. Use instead {@link CartPriceUsageChargeTypeAttributeMapper}.
 */
@Deprecated(since = "2007")
public class UcCartPriceUsageChargeTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderUsageChargePriceData,
		UsageChargeCartPrice>
{
	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderUsageChargePriceData source,
			UsageChargeCartPrice target, MappingContext context)
	{
		if (source.getUsageChargeType() != null && StringUtils.isNotEmpty(source.getUsageChargeType().getCode()))
		{
			target.setUsageChargeType(source.getUsageChargeType().getCode());
		}
	}
}


/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagechargecartcost;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageChargeCartCostWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for unitOfMeasure attribute between
 * {@link TmaAbstractOrderUsageChargePriceData} and {@link UsageChargeCartCostWsDTO}
 *
 * @since 1911
 */
public class TmaUCCartCostUnitOfMeasureAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderUsageChargePriceData, UsageChargeCartCostWsDTO>
{
	private static final String USAGE_CHARGE = "usage";

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderUsageChargePriceData source,
			final UsageChargeCartCostWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setChargeType(USAGE_CHARGE);

		if (!ObjectUtils.isEmpty(source.getUsageUnit()) && StringUtils.isNotEmpty(source.getUsageUnit().getName()))
		{
			target.setUnitOfMeasure(source.getUsageUnit().getName());
		}
	}
}

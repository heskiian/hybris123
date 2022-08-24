/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringchargecartcost;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CycleWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringChargeCartCostWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for baseType attribute between {@link TmaAbstractOrderRecurringChargePriceData}
 * and {@link RecurringChargeCartCostWsDTO}
 *
 * @since 1911
 */
public class TmaRCCartCostCycleAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderRecurringChargePriceData, RecurringChargeCartCostWsDTO>
{
	private static final String RECURRING_TYPE = "recurring";
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderRecurringChargePriceData source,
			final RecurringChargeCartCostWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setChargeType(RECURRING_TYPE);

		final CycleWsDTO cycle = new CycleWsDTO();
		if (source.getCycleStart() != null)
		{
			cycle.setCycleStart(source.getCycleStart());
		}
		if (source.getCycleEnd() != null)
		{
			cycle.setCycleEnd(source.getCycleEnd());
		}
		target.setCycle(cycle);
	}
}

/*
 *   Copyright (c) 2021 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recurringchargeorderprice;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Cycle;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringChargeOrderPrice;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaAbstractOrderRecurringChargePriceData} and
 * {@link RecurringChargeOrderPrice}
 *
 * @since 2105
 */
public class RcOrderPriceCycleAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderRecurringChargePriceData,
		RecurringChargeOrderPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderRecurringChargePriceData source,
			final RecurringChargeOrderPrice target, final MappingContext context)
	{
		final Cycle cycle = new Cycle();
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

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recurringchargecartprice;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Cycle;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringChargeCartPrice;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for baseType attribute between {@link TmaAbstractOrderRecurringChargePriceData} and
 * {@link RecurringChargeCartPrice}
 *
 * @since 1907
 */
public class RcCartPriceCycleAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderRecurringChargePriceData,
		RecurringChargeCartPrice>
{
	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderRecurringChargePriceData source,
			RecurringChargeCartPrice target, MappingContext context)
	{
		Cycle cycle = new Cycle();
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

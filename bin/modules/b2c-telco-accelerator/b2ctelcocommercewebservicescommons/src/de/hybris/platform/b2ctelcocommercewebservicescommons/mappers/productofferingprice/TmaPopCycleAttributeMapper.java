/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaRecurringProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CycleWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringPriceChargeWsDTO;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaRecurringProdOfferPriceChargeData} and
 * {@link RecurringPriceChargeWsDTO}
 *
 * @since 2007
 */
public class TmaPopCycleAttributeMapper
		extends TmaAttributeMapper<TmaRecurringProdOfferPriceChargeData, RecurringPriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaRecurringProdOfferPriceChargeData source,
			final RecurringPriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (ObjectUtils.isEmpty(source.getCycleStart()))
		{
			return;
		}

		final CycleWsDTO cycle = new CycleWsDTO();
		cycle.setCycleStart(source.getCycleStart());
		cycle.setCycleEnd(source.getCycleEnd());
		target.setCycle(cycle);
	}
}

/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaRecurringProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringPriceCharge;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for recurringChargePeriodType attribute between
 * {@link TmaAbstractOrderRecurringChargePriceData} and {@link RecurringPriceCharge}
 *
 * @since 2007
 */
public class PopRecChargePeriodAttributeMapper
		extends TmaAttributeMapper<TmaRecurringProdOfferPriceChargeData, RecurringPriceCharge>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaRecurringProdOfferPriceChargeData source,
			final RecurringPriceCharge target, final MappingContext context)
	{
		if (source.getPriceEvent() != null)
		{
			target.setRecurringChargePeriodType(source.getPriceEvent().getCode());
		}
	}
}

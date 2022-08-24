/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.recurringpricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringPriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price type attribute between {@link RecurringChargeEntryData} and
 * {@link RecurringPriceCharge}
 *
 * @since 1903
 */
public class RpcPriceTypeAttributeMapper extends TmaAttributeMapper<RecurringChargeEntryData, RecurringPriceCharge>
{
	private static final String RECURRING_CHARGE = "recurring";

	@Override
	public void populateTargetAttributeFromSource(final RecurringChargeEntryData source, final RecurringPriceCharge target,
			final MappingContext context)
	{
		target.setPriceType(RECURRING_CHARGE);
	}
}

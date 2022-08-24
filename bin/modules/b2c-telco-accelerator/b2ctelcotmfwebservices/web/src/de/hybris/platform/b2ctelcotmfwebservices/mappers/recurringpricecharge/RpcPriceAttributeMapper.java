/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.recurringpricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringPriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import ma.glasnost.orika.MappingContext;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;


/**
 * This attribute Mapper class maps data for price attribute between {@link RecurringChargeEntryData} and
 * {@link RecurringPriceCharge}
 *
 * @since 1903
 */
public class RpcPriceAttributeMapper extends TmaAttributeMapper<RecurringChargeEntryData, RecurringPriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final RecurringChargeEntryData source, final RecurringPriceCharge target,
			final MappingContext context)
	{
		if (source.getPrice() == null || source.getPrice().getValue() == null)
		{
			return;
		}

		final Money rpcPrice = new Money();
		rpcPrice.setUnit(source.getPrice().getCurrencyIso());
		rpcPrice.setValue(source.getPrice().getValue().toString());

		target.setPrice(rpcPrice);
	}
}

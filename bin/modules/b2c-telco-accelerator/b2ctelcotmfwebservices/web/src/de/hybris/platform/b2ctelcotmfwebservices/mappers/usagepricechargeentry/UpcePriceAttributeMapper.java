/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricechargeentry;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link UsageChargeEntryData} and {@link UsagePriceChargeEntry}
 *
 * @since 1903
 */
public class UpcePriceAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntry>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntry target,
			final MappingContext context)
	{
		if (source.getPrice() == null || source.getPrice().getValue() == null)
		{
			return;
		}

		final Money price = new Money();
		price.setValue(source.getPrice().getValue().toString());
		price.setUnit(source.getPrice().getCurrencyIso());
		target.setPrice(price);
	}
}

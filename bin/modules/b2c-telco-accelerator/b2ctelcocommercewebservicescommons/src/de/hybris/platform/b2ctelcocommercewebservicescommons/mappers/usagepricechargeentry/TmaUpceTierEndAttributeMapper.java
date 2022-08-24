/*
 *Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for tierEnd attribute between {@link UsageChargeEntryData} and
 * {@link UsagePriceChargeEntryWsDTO}
 *
 * @since 1907
 */
public class TmaUpceTierEndAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntryWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntryWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getPrice() != null || source.getPrice().getValue() != null)
		{
			final MoneyWsDTO rpcPrice = new MoneyWsDTO();
			rpcPrice.setCurrencyIso(source.getPrice().getCurrencyIso());
			rpcPrice.setValue(source.getPrice().getValue().toString());
			target.setPrice(rpcPrice);
		}
		if (!(source instanceof TierUsageChargeEntryData))
		{
			return;
		}
		if (ObjectUtils.isEmpty(((TierUsageChargeEntryData) source).getTierEnd()))
		{
			return;
		}
		target.setTierEnd(((TierUsageChargeEntryData) source).getTierEnd());
	}
}

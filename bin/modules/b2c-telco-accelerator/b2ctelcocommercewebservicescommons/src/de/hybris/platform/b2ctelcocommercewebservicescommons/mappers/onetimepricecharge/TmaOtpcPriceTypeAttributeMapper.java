/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.onetimepricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price type attribute between {@link OneTimeChargeEntryData} and
 * {@link OneTimePriceChargeWsDTO}
 *
 * @since 1907
 */
public class TmaOtpcPriceTypeAttributeMapper extends TmaAttributeMapper<OneTimeChargeEntryData, OneTimePriceChargeWsDTO>
{
	private static final String ONE_TIME = "oneTime";

	@Override
	public void populateTargetAttributeFromSource(final OneTimeChargeEntryData source, final OneTimePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setChargeType(ONE_TIME);

		if (source.getPrice() != null || source.getPrice().getValue() != null)
		{
			final MoneyWsDTO rpcPrice = new MoneyWsDTO();
			rpcPrice.setCurrencyIso(source.getPrice().getCurrencyIso());
			rpcPrice.setValue(source.getPrice().getValue().toString());
			target.setPrice(rpcPrice);
			if (source.getBillingTime() != null)
			{
				target.setBillingEvent(source.getBillingTime().getCode());
			}
		}
	}
}

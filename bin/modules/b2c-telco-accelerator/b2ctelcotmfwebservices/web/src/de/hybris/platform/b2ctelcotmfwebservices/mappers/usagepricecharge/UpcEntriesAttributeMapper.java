/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for entries attribute between {@link UsageChargeData} and {@link UsagePriceCharge}
 *
 * @since 1903
 */
public class UpcEntriesAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceCharge>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceCharge target,
			final MappingContext context)
	{
		target.setIsBundle(false);
		if (CollectionUtils.isEmpty(source.getUsageChargeEntries()))
		{
			return;
		}

		final List<ProductOfferingPrice> usageChargeEntries = new ArrayList<>();
		source.getUsageChargeEntries().forEach(usageChargeEntryData ->
		{
			final UsagePriceChargeEntry usageChargeEntryRefWsDto = getMapperFacade()
					.map(usageChargeEntryData, UsagePriceChargeEntry.class, context);
			usageChargeEntries.add(usageChargeEntryRefWsDto);
		});

		target.setBundledPop(usageChargeEntries);
		target.setIsBundle(true);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

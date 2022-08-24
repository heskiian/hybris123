/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageUnitWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageUnit attribute between {@link TmaUsageProdOfferPriceChargeData} and {@link UsagePriceChargeEntryWsDTO}
 *
 * @since 2007
 */
public class TmaPopUsageUnitAttributeMapper
		extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, UsagePriceChargeEntryWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaUsageProdOfferPriceChargeData source,
			final UsagePriceChargeEntryWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getUsageUnit() == null)
		{
			return;
		}

		target.setUsageUnit(createUsageUnitWsDto(source));
	}

	private UsageUnitWsDTO createUsageUnitWsDto(final TmaUsageProdOfferPriceChargeData source)
	{
		final UsageUnitWsDTO usageUnit = new UsageUnitWsDTO();
		usageUnit.setId(source.getUsageUnit().getId());
		usageUnit.setName(source.getUsageUnit().getName());
		return usageUnit;
	}
}

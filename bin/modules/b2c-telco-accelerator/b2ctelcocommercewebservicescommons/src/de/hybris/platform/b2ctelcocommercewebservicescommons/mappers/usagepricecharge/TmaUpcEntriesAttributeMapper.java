/*
 *Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for entries attribute between {@link UsageChargeData} and
 * {@link UsagePriceChargeWsDTO}
 *
 * @since 1907
 */
public class TmaUpcEntriesAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceChargeWsDTO>
{
	private static final String USAGE_CHARGE = "usage";
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setIsBundle(false);
		if (CollectionUtils.isEmpty(source.getUsageChargeEntries()))
		{
			return;
		}

		final List<ProductOfferingPriceWsDTO> usageChargeEntries = new ArrayList<>();
		source.getUsageChargeEntries().forEach(usageChargeEntryData ->
		{
			final UsagePriceChargeEntryWsDTO usageChargeEntryRefWsDto = getMapperFacade().map(usageChargeEntryData,
					UsagePriceChargeEntryWsDTO.class, context);
			usageChargeEntries.add(usageChargeEntryRefWsDto);
		});

		target.setBundledPop(usageChargeEntries);
		target.setIsBundle(true);
		target.setChargeType(USAGE_CHARGE);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

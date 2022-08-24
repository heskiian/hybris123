/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usage charge type attribute between {@link TmaTierUsageCompositeProdOfferPriceData} and
 * {@link UsagePriceChargeWsDTO}.
 *
 * @since 2007
 */
public class TmaPopUsageTypeAttributeMapper extends TmaAttributeMapper<TmaTierUsageCompositeProdOfferPriceData, UsagePriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaTierUsageCompositeProdOfferPriceData source, final UsagePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getUsageChargeType() != null)
		{
			target.setUsageType(source.getUsageChargeType().getCode());
		}
	}
}

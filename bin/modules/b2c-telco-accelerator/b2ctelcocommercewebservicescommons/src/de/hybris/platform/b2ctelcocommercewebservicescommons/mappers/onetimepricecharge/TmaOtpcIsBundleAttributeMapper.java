/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.onetimepricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link OneTimeChargeEntryData} and
 * {@link OneTimePriceChargeWsDTO}
 *
 * @since 1907
 */
public class TmaOtpcIsBundleAttributeMapper extends TmaAttributeMapper<OneTimeChargeEntryData, OneTimePriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final OneTimeChargeEntryData source, final OneTimePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setIsBundle(false);
	}
}

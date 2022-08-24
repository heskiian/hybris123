/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringpricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringPriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link RecurringChargeEntryData} and
 * {@link RecurringPriceChargeWsDTO}
 *
 * @since 1907
 */
public class TmaRpcIsBundleAttributeMapper extends TmaAttributeMapper<RecurringChargeEntryData, RecurringPriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final RecurringChargeEntryData source, final RecurringPriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setIsBundle(false);
	}
}

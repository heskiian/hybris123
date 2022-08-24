/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.onetimepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OneTimePriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for base type attribute between {@link OneTimeChargeEntryData} and {@link OneTimePriceCharge}
 *
 * @since 1903
 */
public class OtpcBaseTypeAttributeMapper extends TmaAttributeMapper<OneTimeChargeEntryData, OneTimePriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final OneTimeChargeEntryData source, final OneTimePriceCharge target,
			final MappingContext context)
	{
		if (target.getClass().getSuperclass() != null) {
			target.setAtbaseType(target.getClass().getSuperclass().getSimpleName());
		}
	}
}

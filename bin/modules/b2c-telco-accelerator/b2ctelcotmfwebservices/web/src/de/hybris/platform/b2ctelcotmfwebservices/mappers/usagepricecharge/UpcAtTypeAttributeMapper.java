/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attype attribute between {@link UsageChargeData} and {@link UsagePriceCharge}
 *
 * @since 1903
 */
public class UpcAtTypeAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceCharge>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceCharge target,
			final MappingContext context)
	{
		target.setAttype(target.getClass().getSimpleName());
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link UsageChargeData} and {@link UsagePriceCharge}
 *
 * @since 1903
 */
public class UpcSchemaLocationAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceCharge>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceCharge target,
			final MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}

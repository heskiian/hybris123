/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.onetimepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OneTimePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link OneTimeChargeEntryData} and {@link OneTimePriceCharge}
 *
 * @since 1903
 */
public class OtpcHrefAttributeMapper extends TmaAttributeMapper<OneTimeChargeEntryData, OneTimePriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final OneTimeChargeEntryData source, final OneTimePriceCharge target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL + source.getId());
		}
	}
}

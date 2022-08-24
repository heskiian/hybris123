/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.recurringpricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringPriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link RecurringChargeEntryData} and
 * {@link RecurringPriceCharge}
 *
 * @since 1903
 */
public class RpcHrefAttributeMapper extends TmaAttributeMapper<RecurringChargeEntryData, RecurringPriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final RecurringChargeEntryData source, final RecurringPriceCharge target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL + source.getId());
		}
	}
}

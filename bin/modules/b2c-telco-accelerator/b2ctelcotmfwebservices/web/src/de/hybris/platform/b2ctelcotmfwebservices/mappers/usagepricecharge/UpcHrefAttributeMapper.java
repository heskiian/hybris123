/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link UsageChargeData} and {@link UsagePriceCharge}
 *
 * @since 1903
 */
public class UpcHrefAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceCharge>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceCharge target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL + source.getId());
		}
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPriceRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaAbstractOrderPriceData} and {@link ProductOfferingPriceRef}
 *
 * @since 2102
 */
public class ProductOfferingPriceRefHrefAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderPriceData,
		ProductOfferingPriceRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source,
			final ProductOfferingPriceRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPriceId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL_V3 + source.getPriceId());
		}
	}
}



/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPriceRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link TmaProductOfferingPriceData} and {@link ProductOfferingPriceRef}
 *
 * @since 2007
 */
public class ProductOfferingPriceRefSchemaLocationAttributeMapper
		extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPriceRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source, final ProductOfferingPriceRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA_V3);
		}
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPriceRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link TmaAbstractOrderPriceData} and
 * {@link ProductOfferingPriceRef}
 *
 * @since 2102
 */
public class ProductOfferingPriceRefIdAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderPriceData, ProductOfferingPriceRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source,
			final ProductOfferingPriceRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPriceId()))
		{
			target.setId(source.getPriceId());
		}
	}
}

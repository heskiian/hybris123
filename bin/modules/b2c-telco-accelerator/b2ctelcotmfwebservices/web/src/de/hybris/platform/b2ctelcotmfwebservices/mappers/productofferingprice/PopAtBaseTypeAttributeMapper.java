/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;


import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType between {@link TmaProductOfferingPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopAtBaseTypeAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtbaseType(target.getClass().getSuperclass().getSimpleName());
		}
	}
}
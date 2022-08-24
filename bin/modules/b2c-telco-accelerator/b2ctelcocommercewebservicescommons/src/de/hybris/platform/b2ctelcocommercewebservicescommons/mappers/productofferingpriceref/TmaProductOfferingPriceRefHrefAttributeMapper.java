/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcocommercewebservicescommons.constants.B2ctelcocommercewebservicescommonsConstants;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaAbstractOrderPriceData} and
 * {@link ProductOfferingPriceRefWsDTO}
 *
 * @since 2102
 */
public class TmaProductOfferingPriceRefHrefAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderPriceData, ProductOfferingPriceRefWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source,
			final ProductOfferingPriceRefWsDTO target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPriceId()))
		{
			target.setHref(
					B2ctelcocommercewebservicescommonsConstants.TMA_PRODUCT_OFFERING_PRICE_API_URL + source.getPriceId());
		}
	}
}

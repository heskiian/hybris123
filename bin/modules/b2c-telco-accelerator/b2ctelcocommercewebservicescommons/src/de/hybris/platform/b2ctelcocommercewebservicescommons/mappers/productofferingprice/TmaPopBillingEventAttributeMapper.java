/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;


import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billingEvent attribute between {@link TmaProductOfferingPriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 2007
 */
public class TmaPopBillingEventAttributeMapper
		extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source,
			final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getPriceEvent() != null)
		{
			target.setBillingEvent(source.getPriceEvent().getCode());
		}
	}
}

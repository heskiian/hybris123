/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingPriceEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingPriceEvent;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingPriceRef;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaProductOfferingPriceEventPayload} and
 * {@link ProductOfferingPriceEvent}
 *
 * @since 2105
 */
public class ProductOfferingPriceRefAttributeMapper
		extends TmaAttributeMapper<TmaProductOfferingPriceEventPayload, ProductOfferingPriceEvent>
{
	private MapperFacade mapperFacade;

	public ProductOfferingPriceRefAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceEventPayload source,
			final ProductOfferingPriceEvent target, final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getProductOfferingPrice()))
		{
			return;
		}
		target.setProductOfferingPriceRef(
				getMapperFacade().map(source.getProductOfferingPrice(), ProductOfferingPriceRef.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

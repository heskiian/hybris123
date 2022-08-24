/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingEvent;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingRef;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaProductOfferingEventPayload} and
 * {@link ProductOfferingEvent}
 *
 * @since 2105
 */
public class ProductOfferingRefAttributeMapper extends TmaAttributeMapper<TmaProductOfferingEventPayload, ProductOfferingEvent>
{
	private MapperFacade mapperFacade;

	public ProductOfferingRefAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingEventPayload source,
			final ProductOfferingEvent target, final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getOffering()))
		{
			return;
		}
		target.setProductOfferingRef(getMapperFacade().map(source.getOffering(), ProductOfferingRef.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

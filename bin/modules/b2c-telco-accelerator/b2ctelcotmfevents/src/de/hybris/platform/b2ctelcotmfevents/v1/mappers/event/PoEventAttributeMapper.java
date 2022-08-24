/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingEvent;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaProductOfferingEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class PoEventAttributeMapper extends TmaAttributeMapper<TmaProductOfferingEventPayload, Event>
{
	private MapperFacade mapperFacade;

	public PoEventAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingEventPayload source, final Event target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getOffering()))
		{
			return;
		}
		target.setEvent(getMapperFacade().map(source, ProductOfferingEvent.class, context));
	}


	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

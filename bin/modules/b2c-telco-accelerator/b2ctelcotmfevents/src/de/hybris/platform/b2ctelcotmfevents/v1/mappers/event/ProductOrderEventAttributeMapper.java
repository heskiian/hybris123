/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaOrderEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOrderEvent;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaOrderEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class ProductOrderEventAttributeMapper extends TmaAttributeMapper<TmaOrderEventPayload, Event>
{
	private MapperFacade mapperFacade;

	public ProductOrderEventAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaOrderEventPayload source, final Event target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getOrder()))
		{
			return;
		}
		target.setEvent(getMapperFacade().map(source, ProductOrderEvent.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

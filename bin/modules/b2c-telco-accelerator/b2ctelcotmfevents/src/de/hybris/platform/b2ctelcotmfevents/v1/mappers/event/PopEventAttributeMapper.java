/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingPriceEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingPriceEvent;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaProductOfferingPriceEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class PopEventAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceEventPayload, Event>
{
	private MapperFacade mapperFacade;

	public PopEventAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceEventPayload source, final Event target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getProductOfferingPrice()))
		{
			return;
		}
		target.setEvent(getMapperFacade().map(source, ProductOfferingPriceEvent.class, context));
	}


	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

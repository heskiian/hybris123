/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaAbstractEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class EventAttypeAttributeMapper extends TmaAttributeMapper<TmaAbstractEventPayload, Event>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractEventPayload source, final Event target,
			final MappingContext context)
	{
		target.setAttype(Event.class.getSimpleName());
	}
}

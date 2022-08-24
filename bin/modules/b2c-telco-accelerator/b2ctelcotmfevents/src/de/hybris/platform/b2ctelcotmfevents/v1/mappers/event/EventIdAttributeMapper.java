/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link TmaAbstractEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class EventIdAttributeMapper extends TmaAttributeMapper<TmaAbstractEventPayload, Event>
{
	private PersistentKeyGenerator tmaEventIdGenerator;

	public EventIdAttributeMapper(final PersistentKeyGenerator tmaEventIdGenerator)
	{
		this.tmaEventIdGenerator = tmaEventIdGenerator;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractEventPayload source, final Event target,
			final MappingContext context)
	{
		target.setEventId(getTmaEventIdGenerator().generate().toString());
	}

	protected PersistentKeyGenerator getTmaEventIdGenerator()
	{
		return tmaEventIdGenerator;
	}
}

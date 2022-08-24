/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.events;

import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Tma implementation of the {@link AbstractEvent}.
 *
 * @since 2105
 */
public abstract class TmaAbstractEvent extends AbstractEvent
{
	private transient Event payload;

	public Event getPayload()
	{
		return payload;
	}

	public void setPayload(Event payload)
	{
		this.payload = payload;
	}
}

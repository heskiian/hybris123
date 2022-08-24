/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.builders;

import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * Builder responsible for creating {@link TmaAbstractEventPayload}s for tmf events.
 *
 * @since 2105
 */
public abstract class TmaAbstractEventPayloadBuilder
{
	private TimeService timeService;

	public TmaAbstractEventPayloadBuilder(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	/**
	 * Create a new instance of a {@link TmaAbstractEventPayload}.
	 *
	 * @param item
	 * 		the item from which the payload will be created
	 * @param eventType
	 * 		the type of the event
	 * @return the payload
	 */
	public abstract TmaAbstractEventPayload build(final ItemModel item,
			final TmaEventType eventType);

	protected void setCommonFields(final TmaAbstractEventPayload payload, final TmaEventType eventType)
	{
		payload.setEventType(eventType);
		payload.setEventTime(getTimeService().getCurrentTime());
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}
}

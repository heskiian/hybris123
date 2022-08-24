/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.builders.impl;

import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfevents.data.TmaOrderEventPayload;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * Builder responsible for creating {@link TmaOrderEventPayload}s for tmf events.
 *
 * @since 2105
 */
public class TmaOrderEventPayloadBuilder extends TmaAbstractEventPayloadBuilder
{
	public TmaOrderEventPayloadBuilder(final TimeService timeService)
	{
		super(timeService);
	}

	@Override
	public TmaAbstractEventPayload build(final ItemModel item, final TmaEventType eventType)
	{
		final TmaOrderEventPayload orderEventPayload = new TmaOrderEventPayload();
		orderEventPayload.setOrder((OrderModel) item);
		setCommonFields(orderEventPayload, eventType);
		return orderEventPayload;
	}
}

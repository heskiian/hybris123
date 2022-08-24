/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.listeners;

import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaAbstractEvent;
import de.hybris.platform.b2ctelcotmfevents.events.TmaOrderEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import ma.glasnost.orika.MapperFacade;


/**
 * AfterSaveListener for a {@link OrderModel} related event.
 *
 * @since 2105
 */
public class TmaOrderAfterSaveListener extends TmaAbstractAfterSaveListener<OrderModel>
{
	private static final Integer ORDER_TYPECODE = 45;

	private MapperFacade dataMapper;

	public TmaOrderAfterSaveListener(final ModelService modelService, final MapperFacade dataMapper,
			final TmaAbstractEventPayloadBuilder eventPayloadBuilder, final TmaAbstractEventsService eventsService,
			final Set<String> eventAllowedCatalogVersions)
	{
		super(modelService, eventPayloadBuilder, eventsService, eventAllowedCatalogVersions);
		this.dataMapper = dataMapper;
	}

	@Override
	protected boolean checkVersionMatch(final PK pk)
	{
		return true;
	}

	@Override
	protected Integer getObjectTypeCode()
	{
		return ORDER_TYPECODE;
	}

	@Override
	protected TmaAbstractEvent createEventWithPayload(final OrderModel item, final TmaEventType eventType)
	{
		final TmaOrderEvent orderEvent = new TmaOrderEvent();
		orderEvent.setPayload(getDataMapper().map(getEventPayloadBuilder().build(item, eventType), Event.class));
		return orderEvent;
	}

	protected MapperFacade getDataMapper()
	{
		return dataMapper;
	}
}

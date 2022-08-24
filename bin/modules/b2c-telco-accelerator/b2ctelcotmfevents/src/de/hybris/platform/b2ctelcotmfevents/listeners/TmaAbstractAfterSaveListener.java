/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.listeners;

import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaAbstractEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;

import java.util.Collection;
import java.util.Set;


/**
 * Abstract template for a TUA implementation of AfterSaveListener used for publishing tmfEvents.
 *
 * @since 2105
 */
public abstract class TmaAbstractAfterSaveListener<T extends ItemModel> implements AfterSaveListener
{
	private static final int UPDATE_EVENT = 1;
	private static final int CREATE_EVENT = 4;

	private ModelService modelService;
	private TmaAbstractEventPayloadBuilder eventPayloadBuilder;
	private TmaAbstractEventsService eventsService;
	private Set<String> eventAllowedCatalogVersions;


	public TmaAbstractAfterSaveListener(final ModelService modelService, final TmaAbstractEventPayloadBuilder eventPayloadBuilder,
			final TmaAbstractEventsService eventsService, final Set<String> eventAllowedCatalogVersions)
	{
		this.modelService = modelService;
		this.eventsService = eventsService;
		this.eventPayloadBuilder = eventPayloadBuilder;
		this.eventAllowedCatalogVersions = eventAllowedCatalogVersions;
	}

	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{
		events.forEach(event -> {
			if (getObjectTypeCode().equals(event.getPk().getTypeCode()) && shouldSendEvent(event.getPk(),
					event.getType()))
			{
				getEventsService().removeItemFromEventsWhitelist(event.getPk().toString());

				final T changedItem = getModelService().get(event.getPk());
				final TmaAbstractEvent abstractEvent = createEventWithPayload(changedItem, getTypeAsEnum(event.getType()));
				getEventsService().publishEvent(abstractEvent);
			}
		});
	}

	private TmaEventType getTypeAsEnum(final Integer type)
	{
		switch (type)
		{
			case UPDATE_EVENT:
				return TmaEventType.ATTRIBUTEVALUECHANGEEVENT;
			case CREATE_EVENT:
				return TmaEventType.CREATEEVENT;
			default:
				throw new IllegalArgumentException("Invalid EventType code");
		}
	}

	protected boolean shouldSendEvent(final PK pk, final int eventType)
	{
		if (UPDATE_EVENT == eventType)
		{
			return getEventsService().isItemInEventsWhiteList(pk.toString());
		}
		if (CREATE_EVENT == eventType)
		{
			return checkVersionMatch(pk);
		}

		return false;
	}

	protected abstract boolean checkVersionMatch(final PK pk);

	protected abstract Integer getObjectTypeCode();

	protected abstract TmaAbstractEvent createEventWithPayload(final T item, final TmaEventType eventType);

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TmaAbstractEventsService getEventsService()
	{
		return eventsService;
	}

	protected TmaAbstractEventPayloadBuilder getEventPayloadBuilder()
	{
		return eventPayloadBuilder;
	}

	protected Set<String> getEventAllowedCatalogVersions()
	{
		return eventAllowedCatalogVersions;
	}
}

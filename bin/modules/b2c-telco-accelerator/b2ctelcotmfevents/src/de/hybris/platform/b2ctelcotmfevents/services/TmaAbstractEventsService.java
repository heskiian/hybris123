/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.services;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.event.impl.DefaultEventService;

import java.util.HashSet;
import java.util.Set;


/**
 * TMA customization of {@link DefaultEventService} with specific operations required for publishing events of catalog aware
 * items.
 *
 * @since 2105
 */
public abstract class TmaAbstractEventsService extends DefaultEventService
{
	/**
	 * List of pk of items used to determine if tmf events need to be sent for an item.
	 */
	private Set<String> eventsWhiteList;

	public TmaAbstractEventsService()
	{
		this.eventsWhiteList = new HashSet<>();
	}

	/**
	 * Add the pk of an item to the eventsWhiteList enabling publish of events for this item.
	 *
	 * @param pk
	 * 		the pk of the item
	 */
	public synchronized void addItemToEventsWhiteList(final String pk)
	{
		getEventsWhiteList().add(pk);
	}

	/**
	 * Remove the pk of an item from the eventsWhiteList disabling publish of events for this item.
	 *
	 * @param pk
	 * 		the pk of the item
	 */
	public synchronized void removeItemFromEventsWhitelist(final String pk)
	{
		getEventsWhiteList().remove(pk);
	}

	/**
	 * Checks if an item is in the eventsWhiteList.
	 *
	 * @param pk
	 * 		the pk of the item.
	 * @return true if found, false otherwise.
	 */
	public boolean isItemInEventsWhiteList(final String pk)
	{
		return getEventsWhiteList().contains(pk);
	}

	/**
	 * Checks if events should be published for an item at catalog synch.
	 *
	 * @param itemModel
	 * 		the item.
	 * @return true if an event should be sent; false otherwise.
	 */
	public abstract boolean shouldSendEventOnSynchronize(final ItemModel itemModel);

	protected synchronized Set<String> getEventsWhiteList()
	{
		return eventsWhiteList;
	}
}

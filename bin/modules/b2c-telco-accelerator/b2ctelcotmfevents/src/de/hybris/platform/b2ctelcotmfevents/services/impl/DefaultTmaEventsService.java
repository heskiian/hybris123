/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.services.impl;

import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link TmaAbstractEventsService}.
 *
 * @since 2105
 */
public class DefaultTmaEventsService extends TmaAbstractEventsService
{

	private transient ModelService modelService;

	public DefaultTmaEventsService(final ModelService modelService)
	{
		super();
		this.modelService = modelService;
	}

	@Override
	public boolean shouldSendEventOnSynchronize(final ItemModel itemModel)
	{

		Item item = getModelService().getSource(itemModel);

		final CatalogManager catalogManager = CatalogManager.getInstance();
		final List<ItemSyncTimestamp> itemSyncTimestampList = catalogManager.getSynchronizationSources(item);
		if (CollectionUtils.isEmpty(itemSyncTimestampList))
		{
			return false;
		}

		Boolean outdated = itemSyncTimestampList.iterator().next().isOutdated();
		return Boolean.TRUE.equals(outdated);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services.impl;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscribedproductservices.daos.SpiGenericDao;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;

import java.util.Map;


/**
 * Default implementation of {@link SpiGenericService}.
 *
 * @since 2111
 */
public class DefaultSpiGenericService implements SpiGenericService
{
	private ModelService modelService;
	private SpiGenericDao spiGenericDao;

	public DefaultSpiGenericService(final ModelService modelService, final SpiGenericDao spiGenericDao)
	{
		this.modelService = modelService;
		this.spiGenericDao = spiGenericDao;
	}

	@Override
	public ItemModel getItem(final String typeCode, final String id)
	{
		return getSpiGenericDao().getItem(typeCode, id);
	}

	@Override
	public ItemModel getItem(final String typeCode, final Map<String, String> params)
	{
		return getSpiGenericDao().getItem(typeCode, params);
	}

	@Override
	public ItemModel createItem(final Class itemClass)
	{
		return getModelService().create(itemClass);
	}

	@Override
	public void saveItem(final ItemModel item)
	{
		getModelService().save(item);
		getModelService().refresh(item);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected SpiGenericDao getSpiGenericDao()
	{
		return spiGenericDao;
	}
}

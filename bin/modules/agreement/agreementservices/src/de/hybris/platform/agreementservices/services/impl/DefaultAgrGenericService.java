/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.services.impl;

import de.hybris.platform.agreementservices.daos.AgrGenericDao;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;


/**
 * Default implementation of {@link AgrGenericService}.
 *
 * @since 2111
 */
public class DefaultAgrGenericService implements AgrGenericService
{
	private ModelService modelService;
	private AgrGenericDao agrGenericDao;

	public DefaultAgrGenericService(final ModelService modelService, final AgrGenericDao agrGenericDao)
	{
		this.modelService = modelService;
		this.agrGenericDao = agrGenericDao;
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

	protected AgrGenericDao getSpiGenericDao()
	{
		return agrGenericDao;
	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.services.impl;

import de.hybris.platform.billingaccountservices.daos.BaGenericDao;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;


/**
 * Default implementation of {@link BaGenericService}.
 *
 * @since 2111
 */
public class DefaultBaGenericService implements BaGenericService
{
	private ModelService modelService;
	private BaGenericDao baGenericDao;

	public DefaultBaGenericService(final ModelService modelService, final BaGenericDao baGenericDao)
	{
		this.modelService = modelService;
		this.baGenericDao = baGenericDao;
	}

	@Override
	public ItemModel getItem(final String typeCode, final String id)
	{
		return getBaGenericDao().getItem(typeCode, id);
	}

	@Override public ItemModel getItem(final String typeCode, final Map<String, String> params)
	{
		return getBaGenericDao().getItem(typeCode, params);
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

	protected BaGenericDao getBaGenericDao()
	{
		return baGenericDao;
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaCategoryDao;
import de.hybris.platform.b2ctelcoservices.services.TmaCategoryService;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaCategoryService}.
 *
 * @since 1903
 */
public class DefaultTmaCategoryService extends DefaultCategoryService implements TmaCategoryService
{
	private transient TmaCategoryDao tmaCategoryDao;

	@Override
	public Collection<CategoryModel> getAllCategories(final Map<String, Object> params)
	{
		return this.getTmaCategoryDao().findAllCategories(params);
	}

	@Required
	public void setTmaCategoryDao(final TmaCategoryDao tmaCategoryDao)
	{
		this.tmaCategoryDao = tmaCategoryDao;
	}

	protected TmaCategoryDao getTmaCategoryDao()
	{
		return tmaCategoryDao;
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaCategoryDao;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.daos.impl.DefaultCategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation for {@link CategoryDao}.
 *
 * @since 1903
 */
public class DefaultTmaCategoryDao extends DefaultCategoryDao implements TmaCategoryDao
{
	private DefaultGenericDao<CategoryModel> defaultGenericDao;
	private static final String IS_ROOT = "isRoot";

	public DefaultTmaCategoryDao()
	{
		super();
		defaultGenericDao = new DefaultGenericDao<>(CategoryModel._TYPECODE);
	}

	@Override
	public Collection<CategoryModel> findAllCategories(final Map<String, Object> params)
	{
		boolean filterByRoot = false;
		boolean isRoot = false;
		if (params.containsKey(IS_ROOT))
		{
			filterByRoot = true;
			isRoot = (boolean) params.get(IS_ROOT);
			params.remove(IS_ROOT);
		}

		params.forEach((key, value) -> validateParameterNotNullStandardMessage(key, value));
		List<CategoryModel> categoriesList = getDefaultGenericDao().find(params);
		if (CollectionUtils.isEmpty(categoriesList))
		{
			throw new ModelNotFoundException("Could not find " + CategoryModel._TYPECODE + ".");
		}
		if (filterByRoot)
		{
			return filterCategoriesByRoot(categoriesList, isRoot);
		}
		return categoriesList;
	}

	private List<CategoryModel> filterCategoriesByRoot(List<CategoryModel> categoriesList, Boolean isRoot)
	{
		List<CategoryModel> filteredList = new ArrayList<>();
		for (CategoryModel categoryModel : categoriesList)
		{
			if (isRoot && (CollectionUtils.isEmpty(categoryModel.getAllSupercategories())))
			{
				filteredList.add(categoryModel);
			}

			if ((!isRoot) && (CollectionUtils.isNotEmpty(categoryModel.getAllSupercategories())))
			{
				filteredList.add(categoryModel);
			}

		}
		return filteredList;
	}

	public DefaultGenericDao<CategoryModel> getDefaultGenericDao()
	{
		return defaultGenericDao;
	}

  @Override
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		defaultGenericDao.setFlexibleSearchService(flexibleSearchService);
	}
}

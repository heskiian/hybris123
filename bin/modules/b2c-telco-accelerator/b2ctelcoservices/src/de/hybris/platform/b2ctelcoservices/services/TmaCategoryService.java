/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.Map;


/**
 * Provides methods for working with {@link CategoryModel}.
 *
 * @since 1903
 */
public interface TmaCategoryService extends CategoryService
{
	/**
	 * Retrieves all the {@link CategoryModel}s.
	 *
	 * @param params
	 * 		Used to filter the results.
	 * @return all found {@link CategoryModel}s or an empty collection if no categories can be found
	 */
	Collection<CategoryModel> getAllCategories(final Map<String, Object> params);
}

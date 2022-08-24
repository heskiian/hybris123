/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.Map;


/**
 * The DAO for the {@link CategoryModel}.
 *
 * @since 1903
 */
public interface TmaCategoryDao extends CategoryDao
{
	/**
	 * Finds all {@link CategoryModel}s that belong to the current site.
	 *
	 * @param params
	 * 		Used to filter the results.
	 * @return all found {@link CategoryModel}s, or an empty collection if no category can be found
	 */
	Collection<CategoryModel> findAllCategories(final Map<String, Object> params);
}

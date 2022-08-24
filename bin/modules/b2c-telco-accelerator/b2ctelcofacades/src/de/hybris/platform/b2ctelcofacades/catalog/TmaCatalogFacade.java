/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.catalog;

import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Implements TMA specific functionality for catalog related data.
 * Determines the list of {@link CategoryHierarchyData}s for a specific catalog.
 * Allows filtering of the result list.
 *
 * @since 1903
 */
public interface TmaCatalogFacade extends CatalogFacade
{
	/**
	 * Returns a list of category DTOs for catalog id, catalog version id and options (BASIC, PRODUCTS)
	 *
	 * @param catalogId
	 * 		the catalog id
	 * @param catalogVersionId
	 * 		the catalog version id
	 * @param params
	 * 		parameters used to filter the categories.
	 * @param page
	 * 		the page options
	 * @param opts
	 * 		the catalog options
	 * @return the categories represented by an list of {@link CategoryHierarchyData}
	 */
	List<CategoryHierarchyData> getAllCategories(final String catalogId, final String catalogVersionId,
			Map<String, Object> params, final PageOption page, final Set<CatalogOption> opts);


	/**
	 * Returns a list with all the catalogs for the provided base store.
	 *
	 * @param baseStoreId
	 * 		The id of the base store.
	 * @return List of {@link CatalogData}
	 */
	Collection<CatalogData> getAllCatalogsForCurrentBaseStore(String baseStoreId);

}

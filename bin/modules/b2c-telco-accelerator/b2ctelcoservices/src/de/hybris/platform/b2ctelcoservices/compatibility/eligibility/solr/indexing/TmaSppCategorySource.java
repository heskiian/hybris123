/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing;

import com.google.common.collect.ImmutableList;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.DefaultCategorySource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * @since 1810
 */
public class TmaSppCategorySource extends DefaultCategorySource
{

	private TmaSpoSource spoSource;

	@Override
	public Collection<CategoryModel> getCategoriesForConfigAndProperty(IndexConfig cfg, IndexedProperty prop, Object model)
	{
		Set<ProductModel> products = getProducts(model);
		Set<CategoryModel> directCategories = getDirectSuperCategories(products);
		if (directCategories == null || directCategories.isEmpty())
		{
			return Collections.emptyList();
		}
		Collection<CatalogVersionModel> catalogVersions = ImmutableList.of(getCatalogVersion(model));
		Set<CategoryModel> allCategories = new HashSet<>();
		directCategories.stream()
				.map(category -> getAllCategories(category, lookupRootCategories(catalogVersions)))
				.forEach(allCategories::addAll);
		return allCategories;
	}

	protected CatalogVersionModel getCatalogVersion(Object model)
	{
		return ((PriceRowModel) model).getCatalogVersion();
	}

	@Override
	protected Set<ProductModel> getProducts(Object model)
	{
		return Collections.singleton(spoSource.getProduct((PriceRowModel) model));
	}

	public TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

	public void setSpoSource(TmaSpoSource spoSource)
	{
		this.spoSource = spoSource;
	}
}

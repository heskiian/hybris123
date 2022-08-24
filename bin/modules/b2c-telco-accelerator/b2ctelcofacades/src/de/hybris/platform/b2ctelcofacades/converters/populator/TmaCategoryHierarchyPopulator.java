/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.converters.populator.CategoryHierarchyPopulator;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates {@link CategoryHierarchyData} with details from {@link CategoryModel}.
 *
 * @since 1903
 */
public class TmaCategoryHierarchyPopulator extends CategoryHierarchyPopulator
{

	@Override
	public void populate(final CategoryModel source, final CategoryHierarchyData target,
			final Collection<? extends CatalogOption> options, final PageOption page) throws ConversionException
	{
		super.populate(source, target, options, page);
		target.setDescription(source.getDescription());
		target.setType(source.getItemtype());
		target.setSupercategories(new ArrayList());

		boolean hasSuperCategories = !CollectionUtils.isEmpty(source.getSupercategories());
		if (hasSuperCategories)
		{
			populateSuperCategories(target, source, false, options);
		}
	}

	protected void populateSuperCategories(final CategoryHierarchyData categoryData, final CategoryModel category, final boolean root,
			final Collection<? extends CatalogOption> options)
	{
		if (!root)
		{
			for (final CategoryModel superc : category.getAllSupercategories())
			{
				populateSuperCategories(categoryData, superc, true, options);
			}
		}
		else
		{
			final CategoryHierarchyData categoryHierarchyData = getCategoryHierarchyData(category, options);
			List<CategoryHierarchyData> supercategories = categoryData.getSupercategories();
			supercategories.add(categoryHierarchyData);
			categoryData.setSupercategories(supercategories);
			for (final CategoryModel superc : category.getAllSupercategories())
			{
				populateSuperCategories(categoryHierarchyData, superc, false, options);
			}
		}
	}

	private CategoryHierarchyData getCategoryHierarchyData(CategoryModel category, Collection<? extends CatalogOption> options)
	{
		final CategoryHierarchyData categoryHierarchyData = new CategoryHierarchyData();
		categoryHierarchyData.setId(category.getCode());
		categoryHierarchyData.setType(category.getItemtype());
		categoryHierarchyData.setName(category.getName());
		categoryHierarchyData.setDescription(category.getDescription());
		categoryHierarchyData.setLastModified(category.getModifiedtime());
		categoryHierarchyData.setUrl(getCategoryUrlResolver().resolve(category));
		categoryHierarchyData.setSupercategories(new ArrayList<CategoryHierarchyData>());
		categoryHierarchyData.setSubcategories(new ArrayList<CategoryHierarchyData>());
		categoryHierarchyData.setProducts(new ArrayList<ProductData>());


		if (options.contains(CatalogOption.PRODUCTS))
		{
			final List<ProductModel> products = category.getProducts();
			for (final ProductModel product : products)
			{
				final ProductData productData = getProductConverter().convert(product);
				categoryHierarchyData.getProducts().add(productData);
			}
		}
		return categoryHierarchyData;
	}
}

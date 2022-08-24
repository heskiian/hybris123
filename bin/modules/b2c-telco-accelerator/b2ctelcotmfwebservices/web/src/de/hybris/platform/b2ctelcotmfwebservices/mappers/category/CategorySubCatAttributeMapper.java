/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.category;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Category;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CategoryRef;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for subCategory attribute between {@link CategoryHierarchyData} and {@link Category}
 *
 * @since 1903
 */
public class CategorySubCatAttributeMapper extends TmaAttributeMapper<CategoryHierarchyData, Category>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CategoryHierarchyData source, Category target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getSubcategories()))
		{
			return;
		}

		final List<CategoryRef> subCategoryList = new ArrayList<>();
		source.getSubcategories().forEach(categoryHierarchyData ->
		{
			CategoryData categoryData = new CategoryData();
			categoryData.setCode(categoryHierarchyData.getId());
			categoryData.setName(categoryHierarchyData.getName());
			categoryData.setDescription(categoryHierarchyData.getDescription());
			categoryData.setType(categoryHierarchyData.getType());
			categoryData.setUrl(categoryHierarchyData.getUrl());

			final CategoryRef categoryRef = getMapperFacade().map(categoryData, CategoryRef.class, context);
			subCategoryList.add(categoryRef);
		});

		target.setSubCategory(subCategoryList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

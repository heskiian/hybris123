/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.category;

import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Category;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for lastUpdate attribute between {@link CategoryHierarchyData} and {@link Category}
 *
 * @since 1903
 */
public class CategoryLastUpdateAttributeMapper extends TmaAttributeMapper<CategoryHierarchyData, Category>
{

	@Override
	public void populateTargetAttributeFromSource(CategoryHierarchyData source, Category target, MappingContext context)
	{
		if (source.getLastModified() != null)
		{
			target.setLastUpdate(source.getLastModified());
		}
	}
}

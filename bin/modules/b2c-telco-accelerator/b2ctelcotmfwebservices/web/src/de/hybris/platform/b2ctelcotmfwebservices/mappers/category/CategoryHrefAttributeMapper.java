/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.category;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Category;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link CategoryHierarchyData} and {@link Category}
 *
 * @since 1903
 */
public class CategoryHrefAttributeMapper extends TmaAttributeMapper<CategoryHierarchyData, Category>
{

	@Override
	public void populateTargetAttributeFromSource(CategoryHierarchyData source, Category target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.CATEGORY_API_URL + source.getId());
		}
	}
}

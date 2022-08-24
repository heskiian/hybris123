/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.categoryref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CategoryRef;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link CategoryData} and {@link CategoryRef}
 *
 * @since 1903
 */
public class CategoryRefHrefAttributeMapper extends TmaAttributeMapper<CategoryData, CategoryRef>
{

	@Override
	public void populateTargetAttributeFromSource(CategoryData source, CategoryRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.CATEGORY_API_URL + source.getCode());
		}
	}
}

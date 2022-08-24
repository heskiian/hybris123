/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.categoryref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CategoryRef;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.util.Config;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link CategoryData} and {@link CategoryRef}
 *
 * @since 1903
 */
public class CategoryRefReferredTypeAttributeMapper extends TmaAttributeMapper<CategoryData, CategoryRef>
{

	@Override
	public void populateTargetAttributeFromSource(CategoryData source, CategoryRef target, MappingContext context)
	{
		target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_CATEGORY_DEFAULT_REFERRED));
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspeccharvalueuse;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdSpecCharValueUse;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attype attribute between {@link FeatureData} and
 * {@link ProdSpecCharValueUse}
 *
 * @since 1903
 */
public class ClassificationValueUseValueTypeAttributeMapper extends TmaAttributeMapper<FeatureData, ProdSpecCharValueUse>
{
	@Override
	public void populateTargetAttributeFromSource(FeatureData source, ProdSpecCharValueUse target, MappingContext context)
	{
		target.setValueType(source.getType());
	}
}

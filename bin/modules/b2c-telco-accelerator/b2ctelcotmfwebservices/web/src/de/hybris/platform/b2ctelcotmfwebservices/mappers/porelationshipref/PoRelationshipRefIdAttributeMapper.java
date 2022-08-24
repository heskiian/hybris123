/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.porelationshipref;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PoRelationshipRef;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for ID attribute between {@link ProductData} and {@link PoRelationshipRef}
 *
 * @since 1903
 */
public class PoRelationshipRefIdAttributeMapper extends TmaAttributeMapper<ProductData, PoRelationshipRef>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final PoRelationshipRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}

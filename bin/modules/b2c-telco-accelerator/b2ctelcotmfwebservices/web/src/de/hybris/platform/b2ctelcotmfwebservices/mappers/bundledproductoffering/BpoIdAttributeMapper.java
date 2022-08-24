/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.bundledproductoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BundledProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for ID attribute between {@link ProductData} and {@link BundledProductOffering}
 *
 * @since 1903
 */
public class BpoIdAttributeMapper extends TmaAttributeMapper<ProductData, BundledProductOffering>
{

	@Override
	public void populateTargetAttributeFromSource(ProductData source, BundledProductOffering target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}

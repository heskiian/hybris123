/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ID attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoIdAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(ProductOffering target, ProductData source,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getId()))
		{
			source.setCode(target.getId());
		}
	}
}

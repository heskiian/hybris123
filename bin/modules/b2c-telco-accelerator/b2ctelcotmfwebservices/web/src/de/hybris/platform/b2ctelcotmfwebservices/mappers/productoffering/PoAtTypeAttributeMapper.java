/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Map;

import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for atType attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoAtTypeAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	private Map<String, String> productOfferingType;

	public PoAtTypeAttributeMapper(final Map<String, String> productOfferingType)
	{
		this.productOfferingType = productOfferingType;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getItemType()))
		{
			target.setAttype(getProductOfferingType().get(source.getItemType()));
		}
	}

	protected Map<String, String> getProductOfferingType()
	{
		return productOfferingType;
	}
}

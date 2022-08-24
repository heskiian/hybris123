/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.indexedproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndexedProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link ProductData} and {@link IndexedProductOffering}
 *
 * @since 1907
 */
public class TmaIndexedPoAtTypeAttributeMapper extends TmaAttributeMapper<ProductData, IndexedProductOffering>
{
	private Map<String, String> productOfferingType;

	public TmaIndexedPoAtTypeAttributeMapper(final Map<String, String> productOfferingType)
	{
		this.productOfferingType = productOfferingType;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final IndexedProductOffering target,
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

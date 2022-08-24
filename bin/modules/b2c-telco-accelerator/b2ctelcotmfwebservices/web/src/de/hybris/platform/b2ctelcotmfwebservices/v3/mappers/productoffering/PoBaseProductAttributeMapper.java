/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for base product attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoBaseProductAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getBaseProduct()))
		{
			return;
		}

		final ProductOfferingRef baseProduct = new ProductOfferingRef();
		baseProduct.setId(source.getBaseProduct());
		baseProduct.setName(source.getBaseProductName());
		baseProduct.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_API_URL + source.getBaseProduct());

		target.setBaseProduct(baseProduct);
	}
}

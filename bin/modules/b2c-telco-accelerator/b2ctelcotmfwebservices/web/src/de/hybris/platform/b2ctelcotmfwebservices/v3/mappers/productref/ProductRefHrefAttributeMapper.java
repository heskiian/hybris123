/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productref;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps href attribute for product relationship between {@link TmaSubscribedProductData} and
 * {@link ProductRef}
 *
 * @since 2102
 */
public class ProductRefHrefAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, ProductRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final ProductRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_API_URL_V3 + source.getId());
		}
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for href attribute between {@link ProductData} and {@link ProductOfferingRef}
 *
 * @since 1903
 */
public class PoRefHrefAttributeMapper extends TmaAttributeMapper<ProductData, ProductOfferingRef>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOfferingRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_API_URL + source.getCode());
		}
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.util.Config;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for atreferredType attribute between {@link ProductData} and {@link ProductOfferingRef}
 *
 * @since 1903
 */
public class PoRefReferredTypeAttributeMapper extends TmaAttributeMapper<ProductData, ProductOfferingRef>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOfferingRef target,
			final MappingContext context)
	{
		if (source.getPurchasable() == null || source.getPurchasable())
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_OFFERING_DEFAULT_REFERRED));
		}
		else
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_OFFERING_OPERATIONAL_REFERRED));
		}
	}
}

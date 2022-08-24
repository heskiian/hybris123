/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOfferingPriceRef;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link TmaProductOfferingPriceModel} and
 * {@link ProductOfferingPriceRef}
 *
 * @since 2105
 */
public class PopRefReferredTypeAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceModel, ProductOfferingPriceRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceModel source, final ProductOfferingPriceRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfresourcesConstants.TMA_PRODUCT_OFFERING_PRICE_DEFAULT_REFERRED));
		}
	}
}

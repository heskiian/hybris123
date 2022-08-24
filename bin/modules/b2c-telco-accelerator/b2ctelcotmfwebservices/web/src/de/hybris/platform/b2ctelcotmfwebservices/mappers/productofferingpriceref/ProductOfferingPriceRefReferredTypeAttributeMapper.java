/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingpriceref;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPriceRef;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link TmaAbstractOrderPriceData} and {@link
 * ProductOfferingPriceRef}
 *
 * @since 2102
 */
public class ProductOfferingPriceRefReferredTypeAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderPriceData, ProductOfferingPriceRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source,
			final ProductOfferingPriceRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPriceId()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_POP_DEFAULT_REFERRED));
		}
	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productofferingpriceref;

import de.hybris.platform.subscribedproductservices.model.SpiProductOfferingPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductOfferingPriceRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link SpiProductOfferingPriceModel} and {@link ProductOfferingPriceRef}
 *
 * @since 2105
 */
public class ProductOfferingPriceRefHrefAttributeMapper
		extends SpiAttributeMapper<SpiProductOfferingPriceModel, ProductOfferingPriceRef>
{
	public ProductOfferingPriceRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductOfferingPriceModel source, final ProductOfferingPriceRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(SubscribedproducttmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL + source.getId());
		}
	}
}

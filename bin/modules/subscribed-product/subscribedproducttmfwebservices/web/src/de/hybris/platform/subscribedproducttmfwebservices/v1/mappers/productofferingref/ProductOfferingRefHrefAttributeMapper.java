/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productofferingref;

import de.hybris.platform.subscribedproductservices.model.SpiProductOfferingModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductOfferingRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link SpiProductOfferingModel} and {@link ProductOfferingRef}
 *
 * @since 2105
 */
public class ProductOfferingRefHrefAttributeMapper extends SpiAttributeMapper<SpiProductOfferingModel, ProductOfferingRef>
{
	public ProductOfferingRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductOfferingModel source, final ProductOfferingRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(SubscribedproducttmfwebservicesConstants.PRODUCT_OFFERING_API_URL + source.getId());
		}
	}
}

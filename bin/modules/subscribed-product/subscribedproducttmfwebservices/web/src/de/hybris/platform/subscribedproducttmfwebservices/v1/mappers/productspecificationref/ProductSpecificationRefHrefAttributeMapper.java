/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productspecificationref;

import de.hybris.platform.subscribedproductservices.model.SpiProductSpecificationModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductSpecificationRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link SpiProductSpecificationModel} and {@link ProductSpecificationRef}
 *
 * @since 2105
 */
public class ProductSpecificationRefHrefAttributeMapper
		extends SpiAttributeMapper<SpiProductSpecificationModel, ProductSpecificationRef>
{
	public ProductSpecificationRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductSpecificationModel source, final ProductSpecificationRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(SubscribedproducttmfwebservicesConstants.PRODUCT_SPECIFICATION_API_URL + source.getId());
		}
	}
}

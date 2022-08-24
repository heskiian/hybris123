/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productprice;

import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link SpiProductPriceModel} and {@link ProductPrice}
 *
 * @since 2105
 */
public class ProductPriceTypeAttributeMapper extends SpiAttributeMapper<SpiProductPriceModel, ProductPrice>
{
	public ProductPriceTypeAttributeMapper(String sourceAttributeName, String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductPriceModel source, final ProductPrice target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(SubscribedproducttmfwebservicesConstants.SPI_PRODUCT_PRICE_TYPE);
		}
	}
}

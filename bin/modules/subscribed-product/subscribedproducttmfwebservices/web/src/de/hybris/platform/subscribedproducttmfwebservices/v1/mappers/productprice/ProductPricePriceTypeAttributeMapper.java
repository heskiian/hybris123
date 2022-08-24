/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productprice;

import de.hybris.platform.subscribedproductservices.enums.SpiPriceType;
import de.hybris.platform.subscribedproductservices.model.SpiProdPriceChargeModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for priceType attribute between {@link SpiProductPriceModel} and {@link ProductPrice}
 *
 * @since 2111
 */
public class ProductPricePriceTypeAttributeMapper extends SpiAttributeMapper<SpiProdPriceChargeModel, ProductPrice>
{
	public ProductPricePriceTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProdPriceChargeModel source, final ProductPrice target,
			final MappingContext context)
	{
		if (source.getPriceType() != null)
		{
			target.setPriceType(source.getPriceType().getCode());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductPrice target, final SpiProdPriceChargeModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getPriceType()))
		{
			source.setPriceType(SpiPriceType.valueOf(target.getPriceType()));
		}
	}
}

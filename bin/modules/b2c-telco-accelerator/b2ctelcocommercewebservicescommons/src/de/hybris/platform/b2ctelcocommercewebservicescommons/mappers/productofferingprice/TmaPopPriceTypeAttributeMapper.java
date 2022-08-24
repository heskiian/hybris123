/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;

import java.util.Map;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price type attribute between {@link TmaComponentProdOfferPriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 2007
 */
public class TmaPopPriceTypeAttributeMapper extends TmaAttributeMapper<TmaComponentProdOfferPriceData, ProductOfferingPriceWsDTO>
{
	private Map<String, String> productOfferingPriceTypeDtoMap;

	public TmaPopPriceTypeAttributeMapper(final Map<String, String> productOfferingPriceTypeDtoMap)
	{
		this.productOfferingPriceTypeDtoMap = productOfferingPriceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaComponentProdOfferPriceData source,
			final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setChargeType(getProductOfferingPriceTypeDtoMap().get(source.getClass().getSimpleName()));
	}

	protected Map<String, String> getProductOfferingPriceTypeDtoMap()
	{
		return productOfferingPriceTypeDtoMap;
	}
}

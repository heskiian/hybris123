/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAllowanceProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for priceType between {@link TmaAllowanceProdOfferPriceAlterationData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2011
 */
public class PopAllowanceTypeAttributeMapper
		extends TmaAttributeMapper<TmaAllowanceProdOfferPriceAlterationData, ProductOfferingPrice>
{
	private static final String SEPARATOR = "_";

	private Map<String, String> productOfferingPriceTypeDtoMap;

	public PopAllowanceTypeAttributeMapper(final Map<String, String> productOfferingPriceTypeDtoMap)
	{
		this.productOfferingPriceTypeDtoMap = productOfferingPriceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAllowanceProdOfferPriceAlterationData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setPriceType(source.getAllowanceType().getCode() + SEPARATOR + getProductOfferingPriceTypeDtoMap()
					.get(source.getClass().getSimpleName()));
		}
	}

	protected Map<String, String> getProductOfferingPriceTypeDtoMap()
	{
		return productOfferingPriceTypeDtoMap;
	}
}

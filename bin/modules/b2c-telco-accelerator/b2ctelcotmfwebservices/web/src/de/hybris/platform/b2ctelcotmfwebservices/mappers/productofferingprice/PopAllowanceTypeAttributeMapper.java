/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAllowanceProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdOfferPriceAllowance;

import java.util.Map;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for allowance attribute between {@link TmaAllowanceProdOfferPriceAlterationData} and
 * {@link ProdOfferPriceAllowance}
 *
 * @since 2011
 */
public class PopAllowanceTypeAttributeMapper
		extends TmaAttributeMapper<TmaAllowanceProdOfferPriceAlterationData, ProdOfferPriceAllowance>
{
	private static final String SEPARATOR = "_";
	private Map<String, String> productOfferingPriceTypeDtoMap;

	public PopAllowanceTypeAttributeMapper(final Map<String, String> productOfferingPriceTypeDtoMap)
	{
		this.productOfferingPriceTypeDtoMap = productOfferingPriceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAllowanceProdOfferPriceAlterationData source,
			final ProdOfferPriceAllowance target, final MappingContext context)
	{
		target.setPriceType(
				source.getAllowanceType().getCode() + SEPARATOR + getProductOfferingPriceTypeDtoMap().get(source.getClass().getSimpleName()));
	}

	protected Map<String, String> getProductOfferingPriceTypeDtoMap()
	{
		return productOfferingPriceTypeDtoMap;
	}
}
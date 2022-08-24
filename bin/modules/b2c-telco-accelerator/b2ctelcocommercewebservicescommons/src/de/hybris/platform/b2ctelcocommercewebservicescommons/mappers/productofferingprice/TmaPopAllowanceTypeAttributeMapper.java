/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceAllowanceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceAlterationWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaAllowanceProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import java.util.Map;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for chargeType attribute between {@link TmaAllowanceProdOfferPriceAlterationData} and
 * {@link ProductOfferingPriceAlterationWsDTO}
 *
 * @since 2011
 */
public class TmaPopAllowanceTypeAttributeMapper
		extends TmaAttributeMapper<TmaAllowanceProdOfferPriceAlterationData, ProductOfferingPriceAllowanceWsDTO>
{
	private static final String SEPARATOR = "_";

	private Map<String, String> productOfferingPriceTypeDtoMap;

	public TmaPopAllowanceTypeAttributeMapper(final Map<String, String> productOfferingPriceTypeDtoMap)
	{
		this.productOfferingPriceTypeDtoMap = productOfferingPriceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAllowanceProdOfferPriceAlterationData source,
			final ProductOfferingPriceAllowanceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setChargeType(
				source.getAllowanceType().getCode() + SEPARATOR + getProductOfferingPriceTypeDtoMap().get(source.getClass().getSimpleName()));
	}

	protected Map<String, String> getProductOfferingPriceTypeDtoMap()
	{
		return productOfferingPriceTypeDtoMap;
	}
}
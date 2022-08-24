/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PricingLogicAlgorithmWsDto;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import java.util.Collections;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for pricing logic algorithm between {@link TmaComponentProdOfferPriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 2102
 */
public class TmaPopPricingLogicAlgorithmAttributeMapper
		extends TmaAttributeMapper<TmaComponentProdOfferPriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPopPricingLogicAlgorithmAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaComponentProdOfferPriceData source,
			final ProductOfferingPriceWsDTO target, final MappingContext context)
	{
		if (source.getPla() != null)
		{
			target.setPricingLogicAlgorithm(getMapperFacade()
					.mapAsList(Collections.singletonList(source.getPla()), PricingLogicAlgorithmWsDto.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

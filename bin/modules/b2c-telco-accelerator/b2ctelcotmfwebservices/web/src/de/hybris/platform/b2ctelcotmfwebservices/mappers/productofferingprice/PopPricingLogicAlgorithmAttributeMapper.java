/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PricingLogicAlgorithm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;

import java.util.Collections;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for pricing logic algorithm attribute between {@link TmaComponentProdOfferPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2102
 */
public class PopPricingLogicAlgorithmAttributeMapper
		extends TmaAttributeMapper<TmaComponentProdOfferPriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	public PopPricingLogicAlgorithmAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaComponentProdOfferPriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (source.getPla() != null)
		{
			target.setPricingLogicAlgorithm(
					getMapperFacade().mapAsList(Collections.singleton(source.getPla()), PricingLogicAlgorithm.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageSpecificationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageSpecification attribute between {@link TmaUsageProdOfferPriceChargeData}
 * and {@link ProductOfferingPrice}
 *
 * @since 2102
 */
public class PopUsageSpecificationAttributeMapper
		extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	public PopUsageSpecificationAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaUsageProdOfferPriceChargeData source,
			final ProductOfferingPrice target, MappingContext context)
	{
		if (source.getProductUsageSpecification() != null)
		{
			target.setUsageSpecification(
					getMapperFacade().map(source.getProductUsageSpecification(), UsageSpecificationRef.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

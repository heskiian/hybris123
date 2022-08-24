/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageSpecificationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageSpecification attribute between {@link TmaUsageProdOfferPriceChargeData}
 * and {@link ProductOfferingPrice}
 *
 * @since 2102
 */
public class PopUsageSpecificationRefAttributeMapper
		extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, UsagePriceChargeEntry>
{
	private MapperFacade mapperFacade;

	public PopUsageSpecificationRefAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(TmaUsageProdOfferPriceChargeData source,
			UsagePriceChargeEntry target, MappingContext context)
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

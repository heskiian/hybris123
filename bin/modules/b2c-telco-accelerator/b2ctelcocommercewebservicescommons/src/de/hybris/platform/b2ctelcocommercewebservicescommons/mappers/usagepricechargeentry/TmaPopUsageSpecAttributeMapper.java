/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageSpecificationRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productUsageSpecification attribute between {@link TmaUsageProdOfferPriceChargeData}
 * and {@link ProductOfferingPriceWsDTO}
 *
 * @since 2102
 */
public class TmaPopUsageSpecAttributeMapper
		extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPopUsageSpecAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaUsageProdOfferPriceChargeData source,
			final ProductOfferingPriceWsDTO target, MappingContext context)
	{
		if (source.getProductUsageSpecification() != null)
		{
			target.setUsageSpecification(
					getMapperFacade().map(source.getProductUsageSpecification(), UsageSpecificationRefWsDTO.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
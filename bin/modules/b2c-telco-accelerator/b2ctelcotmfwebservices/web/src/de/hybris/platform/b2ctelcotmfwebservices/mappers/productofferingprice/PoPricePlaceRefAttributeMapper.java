/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PlaceRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for placeRef attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1907
 */
public class PoPricePlaceRefAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getRegions()))
		{
			return;
		}

		final List<PlaceRef> placeRefWsDtoList = new ArrayList<>();
		source.getRegions().forEach(regionData ->
		{
			final PlaceRef placeRefWsDto = getMapperFacade()
					.map(regionData, PlaceRef.class, context);
			placeRefWsDtoList.add(placeRefWsDto);
		});

		target.setPlace(placeRefWsDtoList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

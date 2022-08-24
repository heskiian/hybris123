/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for region attribute between {@link PriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceRegionAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getRegions()))
		{
			return;
		}

		final List<RegionWsDTO> regions = source.getRegions().stream().map(regionData -> getMapperFacade()
				.map(regionData, RegionWsDTO.class, context)).collect(Collectors.toList());

		target.setRegion(regions);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

}

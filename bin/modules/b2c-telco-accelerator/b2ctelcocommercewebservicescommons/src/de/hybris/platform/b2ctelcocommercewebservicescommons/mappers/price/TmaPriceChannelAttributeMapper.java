/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ChannelWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * TmaPriceChannelAttributeMapper populates value of channel attribute from {@link PriceData} to
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
class TmaPriceChannelAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getDistributionChannels()))
		{
			return;
		}

		final List<ChannelWsDTO> channelList = new ArrayList<>();
		source.getDistributionChannels().forEach(channelData ->
		{
			final ChannelWsDTO channel = getMapperFacade().map(channelData, ChannelWsDTO.class, context);
			channelList.add(channel);
		});

		target.setChannel(channelList);
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

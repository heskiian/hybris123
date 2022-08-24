/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChannelRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.europe1.enums.PriceRowChannel;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This attribute Mapper class maps data for channel attribute between {@link ProductData} and {@link ProductOffering}
 * Used with Composite Prices.
 *
 * @since 2007
 */
public class PoChannelCpAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	private MapperFacade mapperFacade;

	public PoChannelCpAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPrices()))
		{
			return;
		}

		Set<PriceRowChannel> distributionChannels = new HashSet<>();
		source.getPrices().forEach((PriceData priceData) ->
		{
			if (priceData.getDistributionChannels() != null)
			{
				distributionChannels.addAll(priceData.getDistributionChannels());
			}
		});

		final List<ChannelRef> productOfferingChannelList = new ArrayList<>();
		for (PriceRowChannel channel : distributionChannels)
		{
			final ChannelRef channelRef = getMapperFacade().map(channel, ChannelRef.class, context);
			productOfferingChannelList.add(channelRef);
		}

		target.setChannel(productOfferingChannelList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

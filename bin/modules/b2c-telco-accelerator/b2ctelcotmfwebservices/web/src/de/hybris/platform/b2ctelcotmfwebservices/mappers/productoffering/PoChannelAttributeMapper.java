/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChannelRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This attribute Mapper class maps data for channel attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoChannelAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductOfferingPrices()))
		{
			return;
		}

		Set<PriceRowChannel> distributionChannels = new HashSet<>();
		source.getProductOfferingPrices().forEach((SubscriptionPricePlanData priceData) ->
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

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

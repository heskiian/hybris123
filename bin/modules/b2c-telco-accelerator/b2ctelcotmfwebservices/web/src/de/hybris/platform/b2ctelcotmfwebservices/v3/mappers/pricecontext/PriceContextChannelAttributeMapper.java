/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChannelRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for channel attribute between {@link PriceData} and {@link PriceContext}
 *
 * @since 2007
 */
public class PriceContextChannelAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	private MapperFacade mapperFacade;

	public PriceContextChannelAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getDistributionChannels()))
		{
			return;
		}

		final List<ChannelRef> channelRefList = source.getDistributionChannels().stream()
				.map(channelData -> getMapperFacade().map(channelData, ChannelRef.class, context)).collect(Collectors.toList());

		target.setChannel(channelRefList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

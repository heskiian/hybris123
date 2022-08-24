/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ChannelWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPriceChannelAttributeMapper}
 *
 */
@UnitTest
public class TmaPriceChannelAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaPriceChannelAttributeMapper mapper = new TmaPriceChannelAttributeMapper();

	SubscriptionPricePlanData source;

	MappingContext context;

	ProductOfferingPriceWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionPricePlanData();
		target = new ProductOfferingPriceWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource() throws ParseException
	{
		final ChannelWsDTO channelWsDTO = new ChannelWsDTO();
		channelWsDTO.setId(PriceRowChannel.DESKTOP.getCode());
		final Set<PriceRowChannel> distributionChannels = new HashSet<>();
		final List<ChannelWsDTO> channelList = new ArrayList<>();
		channelList.add(channelWsDTO);
		distributionChannels.add(PriceRowChannel.DESKTOP);
		source.setDistributionChannels(distributionChannels);
		for (final PriceRowChannel channelData : source.getDistributionChannels())
		{
			Mockito.when(mapperFacade.map(channelData, ChannelWsDTO.class, context)).thenReturn(channelWsDTO);
		}
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(channelWsDTO.getId(), target.getChannel().get(0).getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullSource()
	{
		mapper.populateTargetAttributeFromSource(null, target, context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullTarget()
	{
		mapper.populateTargetAttributeFromSource(source, null, context);
	}
}

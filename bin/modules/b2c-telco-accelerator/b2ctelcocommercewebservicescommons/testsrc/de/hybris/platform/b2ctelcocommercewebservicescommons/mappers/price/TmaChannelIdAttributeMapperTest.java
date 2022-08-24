/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ChannelWsDTO;
import de.hybris.platform.europe1.enums.PriceRowChannel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaChannelIdAttributeMapper}
 *
 */
@UnitTest
public class TmaChannelIdAttributeMapperTest
{
	@InjectMocks
	private final TmaChannelIdAttributeMapper mapper = new TmaChannelIdAttributeMapper();

	PriceRowChannel source;

	MappingContext context;

	ChannelWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ChannelWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(PriceRowChannel.DESKTOP, target, context);
		Assert.assertEquals(PriceRowChannel.DESKTOP.getCode(), target.getId());
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

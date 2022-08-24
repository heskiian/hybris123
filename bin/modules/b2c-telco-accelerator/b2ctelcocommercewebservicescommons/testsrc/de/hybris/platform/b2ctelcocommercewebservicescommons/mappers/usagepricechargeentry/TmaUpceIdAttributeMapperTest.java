/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUpceIdAttributeMapper}
 *
 */
@UnitTest
public class TmaUpceIdAttributeMapperTest
{
	@InjectMocks
	private final TmaUpceIdAttributeMapper mapper = new TmaUpceIdAttributeMapper();

	@Mock
	private MapperFacade mapperFacade;

	UsageChargeEntryData source;

	MappingContext context;

	UsagePriceChargeEntryWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new UsagePriceChargeEntryWsDTO();
		source = new UsageChargeEntryData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource() throws ParseException
	{
		final TierUsageChargeEntryData chargeEntryData = new TierUsageChargeEntryData();
		chargeEntryData.setId("ID");
		mapper.populateTargetAttributeFromSource(chargeEntryData, target, context);
		Assert.assertEquals(chargeEntryData.getId(), target.getId());
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

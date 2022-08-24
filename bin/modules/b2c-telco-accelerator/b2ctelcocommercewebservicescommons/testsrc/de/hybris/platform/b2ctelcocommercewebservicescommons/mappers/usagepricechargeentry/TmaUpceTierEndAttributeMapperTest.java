/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import java.math.BigDecimal;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUpceTierEndAttributeMapper}
 *
 */
@UnitTest
public class TmaUpceTierEndAttributeMapperTest
{
	@InjectMocks
	private final TmaUpceTierEndAttributeMapper mapper = new TmaUpceTierEndAttributeMapper();

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
		final PriceData pricedata = new PriceData();
		pricedata.setCurrencyIso("USD");
		pricedata.setValue(new BigDecimal(8));
		source.setPrice(pricedata);
		final TierUsageChargeEntryData tierUsageChargeEntryData = new TierUsageChargeEntryData();
		tierUsageChargeEntryData.setTierEnd(8);
		tierUsageChargeEntryData.setPrice(source.getPrice());
		mapper.populateTargetAttributeFromSource(tierUsageChargeEntryData, target, context);
		Assert.assertEquals(tierUsageChargeEntryData.getTierEnd(), target.getTierEnd().intValue());
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

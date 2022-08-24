/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUpceLastUpdateAttributeMapper}
 *
 */
@UnitTest
public class TmaUpceLastUpdateAttributeMapperTest
{
	@InjectMocks
	private final TmaUpceLastUpdateAttributeMapper mapper = new TmaUpceLastUpdateAttributeMapper();

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
		final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		final Date date = format.parse("10.10.2010 10:10");
		final TierUsageChargeEntryData chargeEntryData = new TierUsageChargeEntryData();
		chargeEntryData.setModifiedTime(date);

		mapper.populateTargetAttributeFromSource(chargeEntryData, target, context);
		Assert.assertEquals(chargeEntryData.getModifiedTime(), target.getModifiedTime());
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

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.onetimepricecharge;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;



/**
 * JUnit Tests for the @{TmaOtpcBillingEventAttributeMapper}
 *
 */
@UnitTest
public class TmaOtpcBillingEventAttributeMapperTest
{
	@InjectMocks
	private final TmaOtpcBillingEventAttributeMapper mapper = new TmaOtpcBillingEventAttributeMapper();

	OneTimeChargeEntryData source;

	MappingContext context;

	OneTimePriceChargeWsDTO target;

	private static final String BILLINGCODE = "monthly_12";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new OneTimePriceChargeWsDTO();
	}

	public void setOneTimeChargeEntryData()
	{
		source = new OneTimeChargeEntryData();
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode(BILLINGCODE);
		source.setBillingTime(billingTime);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		setOneTimeChargeEntryData();
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertNotNull(target.getBillingEvent());
		assertEquals(source.getBillingTime().getCode(), target.getBillingEvent());
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

	@Test
	public void testProductSpecificationNull()
	{
		final OneTimeChargeEntryData oneTimeChargeEntryData = new OneTimeChargeEntryData();
		oneTimeChargeEntryData.setBillingTime(null);
		final OneTimePriceChargeWsDTO oneTimePriceChargeWsDTO = new OneTimePriceChargeWsDTO();
		mapper.populateTargetAttributeFromSource(oneTimeChargeEntryData, oneTimePriceChargeWsDTO, context);
		Assert.assertNull(oneTimePriceChargeWsDTO.getBillingEvent());
	}
}

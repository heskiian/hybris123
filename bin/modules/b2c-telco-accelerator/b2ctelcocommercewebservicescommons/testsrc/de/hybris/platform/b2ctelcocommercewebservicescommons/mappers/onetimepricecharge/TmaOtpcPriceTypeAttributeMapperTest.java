/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.onetimepricecharge;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaOtpcPriceTypeAttributeMapper}
 *
 */
@UnitTest
public class TmaOtpcPriceTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaOtpcPriceTypeAttributeMapper mapper = new TmaOtpcPriceTypeAttributeMapper();

	OneTimeChargeEntryData source;

	MappingContext context;

	OneTimePriceChargeWsDTO target;

	private static final String ONE_TIME = "oneTime";
	private static final String CURRENCY = "USD";
	private static final String BILLINGCODE = "monthly";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new OneTimePriceChargeWsDTO();
		source = new OneTimeChargeEntryData();
	}

	public void setOneTimeChargeEntryData()
	{
		final PriceData price = new PriceData();
		price.setValue(BigDecimal.valueOf(12.00));
		price.setCurrencyIso(CURRENCY);
		source.setPrice(price);
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode(BILLINGCODE);
		source.setBillingTime(billingTime);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		setOneTimeChargeEntryData();
		target.setChargeType(ONE_TIME);
		mapper.populateTargetAttributeFromSource(source, target, context);
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
}

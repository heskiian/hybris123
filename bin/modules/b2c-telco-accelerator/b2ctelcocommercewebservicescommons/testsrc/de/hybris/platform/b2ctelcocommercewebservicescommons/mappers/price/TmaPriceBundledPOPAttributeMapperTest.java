/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * JUnit Tests for the @{TmaPriceBundledPOPAttributeMapper}
 *
 */
@UnitTest
public class TmaPriceBundledPOPAttributeMapperTest
{
	@Mock
	MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaPriceBundledPOPAttributeMapper mapper = new TmaPriceBundledPOPAttributeMapper();

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
	public void testPopulateTargetAttributeFromSource()
	{
		getOneTimeChangeEntries();
		final OneTimePriceChargeWsDTO oneTimePriceChargeWsDTO = new OneTimePriceChargeWsDTO();
		Mockito.when(mapperFacade.map(source.getOneTimeChargeEntries(), OneTimePriceChargeWsDTO.class, context))
				.thenReturn(oneTimePriceChargeWsDTO);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertTrue(target.getIsBundle());
	}

	@Test
	public void testBundlePopEmpty()
	{
		source.setCurrencyIso("USD");
		source.setValue(new BigDecimal(9));
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertFalse(target.getIsBundle());
		Assert.assertEquals(source.getCurrencyIso(), target.getPrice().getCurrencyIso());
	}

	private void getOneTimeChangeEntries()
	{
		final OneTimeChargeEntryData oneTimeChargeEntry = new OneTimeChargeEntryData();
		oneTimeChargeEntry.setId("ONE_TIME_ID");
		final List<OneTimeChargeEntryData> oneTimeChargeEntries = new ArrayList<>();
		oneTimeChargeEntries.add(oneTimeChargeEntry);
		source.setOneTimeChargeEntries(oneTimeChargeEntries);
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

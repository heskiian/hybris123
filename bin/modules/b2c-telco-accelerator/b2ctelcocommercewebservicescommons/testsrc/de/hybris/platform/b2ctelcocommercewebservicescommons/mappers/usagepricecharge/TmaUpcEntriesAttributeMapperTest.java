/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

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
 * JUnit Tests for the @{UpcEntriesAttributeMapper}
 *
 */
@UnitTest
public class TmaUpcEntriesAttributeMapperTest
{
	private static final String USAGE_CHARGE = "usage";
	
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaUpcEntriesAttributeMapper mapper = new TmaUpcEntriesAttributeMapper();

	UsageChargeData source;

	MappingContext context;

	UsagePriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new UsagePriceChargeWsDTO();
		source = new UsageChargeData();
	}

	private void setUsageChangeEntriesData()
	{
		final List<UsageChargeEntryData> usageChargeEntries = new ArrayList<>();
		final UsageChargeEntryData chargeEntryData = new UsageChargeEntryData();
		chargeEntryData.setPrice(new PriceData());
		usageChargeEntries.add(chargeEntryData);
		source.setUsageChargeEntries(usageChargeEntries);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		setUsageChangeEntriesData();
		final UsagePriceChargeEntryWsDTO usagePriceChargeEntryWsDTO = new UsagePriceChargeEntryWsDTO();
		final List<ProductOfferingPriceWsDTO> usageChargeEntries = new ArrayList<>();
		usagePriceChargeEntryWsDTO.setBundledPop(usageChargeEntries);
		for (final UsageChargeEntryData usageChargeEntryData : source.getUsageChargeEntries())
		{
			Mockito.when(mapperFacade.map(usageChargeEntryData, UsagePriceChargeEntryWsDTO.class, context))
					.thenReturn(usagePriceChargeEntryWsDTO);
		}
		usageChargeEntries.add(usagePriceChargeEntryWsDTO);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(usagePriceChargeEntryWsDTO.getBundledPop(), target.getBundledPop());
		Assert.assertTrue(target.getIsBundle());
		Assert.assertEquals(USAGE_CHARGE, target.getChargeType());
	}

	@Test
	public void testUsageChangeEntriesNull()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertNull(source.getUsageChargeEntries());
		Assert.assertFalse(target.getIsBundle());
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

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.onetimepricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OneTimePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaOtpcIsBundleAttributeMapper}
 *
 */
@UnitTest
public class TmaOtpcIsBundleAttributeMapperTest
{
	@InjectMocks
	private final TmaOtpcIsBundleAttributeMapper mapper = new TmaOtpcIsBundleAttributeMapper();

	OneTimeChargeEntryData source;

	MappingContext context;

	OneTimePriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new OneTimePriceChargeWsDTO();
		source = new OneTimeChargeEntryData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
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

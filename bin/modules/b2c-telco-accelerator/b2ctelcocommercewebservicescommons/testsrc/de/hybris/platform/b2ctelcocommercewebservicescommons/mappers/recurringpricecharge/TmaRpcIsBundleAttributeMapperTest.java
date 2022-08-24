/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringpricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringPriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaRpcIsBundleAttributeMapper}
 *
 */
@UnitTest
public class TmaRpcIsBundleAttributeMapperTest
{
	@InjectMocks
	private final TmaRpcIsBundleAttributeMapper mapper = new TmaRpcIsBundleAttributeMapper();

	RecurringChargeEntryData source;

	MappingContext context;

	RecurringPriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new RecurringPriceChargeWsDTO();
		source = new RecurringChargeEntryData();
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

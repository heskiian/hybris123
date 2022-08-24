/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringpricecharge;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CycleWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringPriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaRpcCycleAttributeMapper}
 *
 */
public class TmaRpcCycleAttributeMapperTest
{
	@InjectMocks
	private final TmaRpcCycleAttributeMapper mapper = new TmaRpcCycleAttributeMapper();

	RecurringChargeEntryData source;

	MappingContext context;

	RecurringPriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new RecurringPriceChargeWsDTO();
		source = new RecurringChargeEntryData();
		source.setCycleStart(12);
		source.setCycleEnd(15);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final CycleWsDTO cycle = new CycleWsDTO();
		cycle.setCycleEnd(source.getCycleEnd());
		cycle.setCycleStart(source.getCycleStart());
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(cycle.getCycleStart(), target.getCycle().getCycleStart());
		Assert.assertEquals(cycle.getCycleEnd(), target.getCycle().getCycleEnd());
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

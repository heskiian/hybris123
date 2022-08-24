/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagechargecartcost;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageChargeCartCostWsDTO;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUCCartCostUnitOfMeasureAttributeMapper}
 */

@UnitTest
public class TmaUCCartCostUnitOfMeasureAttributeMapperTest
{
	@InjectMocks
	private final TmaUCCartCostUnitOfMeasureAttributeMapper mapper = new TmaUCCartCostUnitOfMeasureAttributeMapper();

	TmaAbstractOrderUsageChargePriceData source;

	MappingContext context;

	UsageChargeCartCostWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new TmaAbstractOrderUsageChargePriceData();
		target = new UsageChargeCartCostWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final UsageUnitData usageUnit = new UsageUnitData();
		usageUnit.setName("MB/s");
		source.setUsageUnit(usageUnit);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getUsageUnit().getName(), target.getUnitOfMeasure());
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

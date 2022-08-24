/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringchargecartcost;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringChargeCartCostWsDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaRCCartCostCycleAttributeMapper}
 */

@UnitTest
public class TmaRCCartCostCycleAttributeMapperTest
{
	@InjectMocks
	private final TmaRCCartCostCycleAttributeMapper mapper = new TmaRCCartCostCycleAttributeMapper();

	TmaAbstractOrderRecurringChargePriceData source;

	MappingContext context;

	RecurringChargeCartCostWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new TmaAbstractOrderRecurringChargePriceData();
		target = new RecurringChargeCartCostWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		source.setCycleStart(1);
		source.setCycleEnd(10);

		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getCycleStart(), target.getCycle().getCycleStart());
		Assert.assertEquals(source.getCycleEnd(), target.getCycle().getCycleEnd());
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

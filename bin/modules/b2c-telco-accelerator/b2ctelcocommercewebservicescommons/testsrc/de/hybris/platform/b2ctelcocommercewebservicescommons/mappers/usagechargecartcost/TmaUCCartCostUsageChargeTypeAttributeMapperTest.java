/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagechargecartcost;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageChargeCartCostWsDTO;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUCCartCostUsageChargeTypeAttributeMapper}
 *
 * @since 1911
 * @deprecated since 2007
 */
@Deprecated(since = "2007")
@UnitTest
public class TmaUCCartCostUsageChargeTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaUCCartCostUsageChargeTypeAttributeMapper mapper = new TmaUCCartCostUsageChargeTypeAttributeMapper();

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
		final UsageChargeTypeData usageChargeType = new UsageChargeTypeData();
		usageChargeType.setCode("each_respective_tier");
		source.setUsageChargeType(usageChargeType);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getUsageChargeType().getCode(), target.getUsageChargeType());
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


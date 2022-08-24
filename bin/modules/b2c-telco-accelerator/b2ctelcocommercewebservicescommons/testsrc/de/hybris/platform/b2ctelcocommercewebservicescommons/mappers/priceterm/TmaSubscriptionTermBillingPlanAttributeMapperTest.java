/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.BillingPlanWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingCycleTypeData;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaSubscriptionTermBillingPlanAttributeMapper}
 */
@UnitTest
public class TmaSubscriptionTermBillingPlanAttributeMapperTest
{
	@InjectMocks
	private final TmaSubscriptionTermBillingPlanAttributeMapper mapper = new TmaSubscriptionTermBillingPlanAttributeMapper();

	MappingContext context;

	BillingPlanData source;
	BillingPlanWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new BillingPlanData();
		target = new BillingPlanWsDTO();
		final BillingCycleTypeData billingCycleType = new BillingCycleTypeData();
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode("monthly");
		billingCycleType.setCode("subscription_start");
		source.setBillingCycleDay(0);
		source.setBillingCycleType(billingCycleType);
		source.setBillingTime(billingTime);
		source.setName("Monthly Plan");
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getBillingCycleDay(), target.getBillingCycleDay());
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

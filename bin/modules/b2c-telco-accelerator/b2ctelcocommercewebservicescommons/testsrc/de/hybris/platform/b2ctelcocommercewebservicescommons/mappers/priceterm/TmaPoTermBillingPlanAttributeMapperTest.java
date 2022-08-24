/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.BillingPlanWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingCycleTypeData;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

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
 * JUnit Tests for the @{TmaPoTermBillingPlanAttributeMapper}
 */
@UnitTest
public class TmaPoTermBillingPlanAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@InjectMocks
	private final TmaPoTermBillingPlanAttributeMapper mapper = new TmaPoTermBillingPlanAttributeMapper();

	MappingContext context;

	SubscriptionTermData source;
	ProductOfferingTermWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionTermData();
		target = new ProductOfferingTermWsDTO();

		final BillingPlanData billingPlan = new BillingPlanData();
		final BillingCycleTypeData billingCycleType = new BillingCycleTypeData();
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode("monthly");
		billingCycleType.setCode("subscription_start");
		billingPlan.setBillingCycleDay(0);
		billingPlan.setBillingCycleType(billingCycleType);
		billingPlan.setBillingTime(billingTime);
		billingPlan.setName("Monthly Plan");

		source.setBillingPlan(billingPlan);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final BillingPlanWsDTO billingPlan = new BillingPlanWsDTO();
		billingPlan.setName("Monthly Plan");
		Mockito.when(mapperFacade.map(source.getBillingPlan(), BillingPlanWsDTO.class, context))
				.thenReturn(billingPlan);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getBillingPlan().getName(), target.getBillingPlan().getName());
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

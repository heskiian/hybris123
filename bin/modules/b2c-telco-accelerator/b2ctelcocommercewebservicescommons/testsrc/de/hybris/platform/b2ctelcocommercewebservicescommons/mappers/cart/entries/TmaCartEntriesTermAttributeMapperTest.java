/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingCycleTypeData;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;

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
 * JUnit Tests for the @{TmaCartEntriesTermAttributeMapper}
 */
@UnitTest
public class TmaCartEntriesTermAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaCartEntriesTermAttributeMapper mapper = new TmaCartEntriesTermAttributeMapper();

	MappingContext context;
	OrderEntryData source;
	OrderEntryWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new OrderEntryData();
		target = new OrderEntryWsDTO();

		final TmaCartSubscriptionInfoData subscriptionInfo = new TmaCartSubscriptionInfoData();
		final SubscriptionTermData subscriptionTerm = new SubscriptionTermData();
		final TermOfServiceFrequencyData termOfServiceFrequency = new TermOfServiceFrequencyData();
		final TermOfServiceRenewalData termOfServiceRenewal = new TermOfServiceRenewalData();
		final BillingPlanData billingPlan = new BillingPlanData();
		final BillingCycleTypeData billingCycleType = new BillingCycleTypeData();
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode("monthly");
		billingCycleType.setCode("subscription_start");
		billingPlan.setBillingCycleDay(0);
		billingPlan.setBillingCycleType(billingCycleType);
		billingPlan.setBillingTime(billingTime);
		billingPlan.setName("Monthly Plan");
		termOfServiceRenewal.setCode("auto_renewing");
		termOfServiceFrequency.setCode("none");
		subscriptionTerm.setCancellable(true);
		subscriptionTerm.setId("none");
		subscriptionTerm.setName("No contract");
		subscriptionTerm.setTermOfServiceFrequency(termOfServiceFrequency);
		subscriptionTerm.setTermOfServiceNumber(0);
		subscriptionTerm.setTermOfServiceRenewal(termOfServiceRenewal);
		subscriptionTerm.setBillingPlan(billingPlan);
		subscriptionInfo.setSubscriptionTerm(subscriptionTerm);

		source.setSubscriptionInfo(subscriptionInfo);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final ProductOfferingTermWsDTO subscriptionTerm = new ProductOfferingTermWsDTO();
		subscriptionTerm.setCancellable(true);
		final SubscriptionTermData subscriptionTermData = source.getSubscriptionInfo().getSubscriptionTerm();
		Mockito.when(mapperFacade.map(source.getSubscriptionInfo().getSubscriptionTerm(), ProductOfferingTermWsDTO.class, context))
				.thenReturn(subscriptionTerm);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getSubscriptionInfo().getSubscriptionTerm().isCancellable(),
				target.getSubscriptionTerm().getCancellable());
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

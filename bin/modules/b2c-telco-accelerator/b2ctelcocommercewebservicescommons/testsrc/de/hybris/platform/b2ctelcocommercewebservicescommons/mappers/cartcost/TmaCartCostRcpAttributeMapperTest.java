/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaCartCostRcpAttributeMapper}
 */

@UnitTest
public class TmaCartCostRcpAttributeMapperTest
{
	@InjectMocks
	private final TmaCartCostRcpAttributeMapper mapper = new TmaCartCostRcpAttributeMapper();

	MappingContext context;

	TmaAbstractOrderChargePriceData source;
	CartCostWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new TmaAbstractOrderChargePriceData();
		target = new CartCostWsDTO();
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setCode("paynow");
		source.setBillingTime(billingTime);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getBillingTime().getCode(), target.getRecurringChargePeriod());
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

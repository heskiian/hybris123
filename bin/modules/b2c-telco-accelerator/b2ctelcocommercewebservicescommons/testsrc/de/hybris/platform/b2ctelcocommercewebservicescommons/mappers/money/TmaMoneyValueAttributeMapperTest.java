/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.money;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaMoneyValueAttributeMapper}
 */

@UnitTest
public class TmaMoneyValueAttributeMapperTest
{
	@InjectMocks
	private final TmaMoneyValueAttributeMapper mapper = new TmaMoneyValueAttributeMapper();

	MappingContext context;

	SubscriptionPricePlanData source;
	MoneyWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new MoneyWsDTO();
		source = new SubscriptionPricePlanData();
		source.setValue(new BigDecimal("699.99"));
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getValue().toString(), target.getValue());
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

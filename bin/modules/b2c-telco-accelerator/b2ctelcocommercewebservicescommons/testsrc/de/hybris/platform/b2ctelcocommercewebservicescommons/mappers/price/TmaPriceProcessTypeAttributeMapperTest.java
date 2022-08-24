/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPriceProcessTypeAttributeMapper}
 *
 */
@UnitTest
public class TmaPriceProcessTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaPriceProcessTypeAttributeMapper mapper = new TmaPriceProcessTypeAttributeMapper();

	SubscriptionPricePlanData source;

	MappingContext context;

	ProductOfferingPriceWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionPricePlanData();
		target = new ProductOfferingPriceWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final Set<TmaProcessType> processTypes = new HashSet<>();
		processTypes.add(TmaProcessType.DEVICE_ONLY);
		source.setProcessTypes(processTypes);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(TmaProcessType.DEVICE_ONLY.getCode(), target.getProcessType().get(0).getId());
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

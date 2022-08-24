/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.UnitData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPriceUnitOfMeasureAttributeMapper}
 *
 */
@UnitTest
public class TmaPriceUnitOfMeasureAttributeMapperTest
{
	@InjectMocks
	private final TmaPriceUnitOfMeasureAttributeMapper mapper = new TmaPriceUnitOfMeasureAttributeMapper();

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
		final UnitData unitData = new UnitData();
		unitData.setUnitType("UNIT_TYPE");
		source.setUnit(unitData);
		source.setUnitFactor(8);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getUnit().getUnitType(), target.getUnitOfMeasure().getCurrencyIso());
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

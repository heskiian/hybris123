/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPriceIsPriceOverrideAttributeMapper}
 *
 */
@UnitTest
public class TmaPriceIsPriceOverrideAttributeMapperTest
{
	@InjectMocks
	private final TmaPriceIsPriceOverrideAttributeMapper mapper = new TmaPriceIsPriceOverrideAttributeMapper();

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
		final ProductData data = new ProductData();
		data.setCode("1234");
		final List<ProductData> datas = new ArrayList<>();
		datas.add(data);
		source.setAffectedProductOffering(data);
		source.setRequiredProductOfferings(datas);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertTrue(target.getIsPriceOverride());
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

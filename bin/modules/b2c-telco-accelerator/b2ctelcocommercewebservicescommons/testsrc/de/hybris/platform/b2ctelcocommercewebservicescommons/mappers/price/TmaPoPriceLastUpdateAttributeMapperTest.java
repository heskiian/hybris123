/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPoPriceLastUpdateAttributeMappe}
 *
 */
@UnitTest
public class TmaPoPriceLastUpdateAttributeMapperTest
{
	@InjectMocks
	private final TmaPoPriceLastUpdateAttributeMapper mapper = new TmaPoPriceLastUpdateAttributeMapper();

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
	public void testPopulateTargetAttributeFromSource() throws ParseException
	{
		final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		final Date date = format.parse("10.5.2019 10:10");
		source.setModifiedtime(date);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getModifiedtime(), target.getModifiedTime());
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

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaProductValidForAttributeMapper}
 *
 */
@UnitTest
public class TmaProductValidForAttributeMapperTest
{
	@InjectMocks
	private final TmaProductValidForAttributeMapper mapper = new TmaProductValidForAttributeMapper();

	@Mock
	private MapperFacade mapperFacade;

	ProductData source;

	ProductWsDTO target;

	MappingContext context;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductWsDTO();
		source = new ProductData();
		final Date onlineDate = new Date(2019, 05, 07);
		final Date offlineDate = new Date(2019, 05, 10);
		source.setOnlineDate(onlineDate);
		source.setOfflineDate(offlineDate);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final TimePeriodWsDTO timePeriod = new TimePeriodWsDTO();
		timePeriod.setStartDateTime(source.getOnlineDate());
		timePeriod.setEndDateTime(source.getOfflineDate());
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(timePeriod.getEndDateTime(), target.getValidFor().getEndDateTime());
		Assert.assertEquals(timePeriod.getStartDateTime(), target.getValidFor().getStartDateTime());
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


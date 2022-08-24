/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUpcUsageTypeAttributeMapper}
 *
 */
@UnitTest
public class TmaUpcUsageTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaUpcUsageTypeAttributeMapper mapper = new TmaUpcUsageTypeAttributeMapper();

	@Mock
	private MapperFacade mapperFacade;

	UsageChargeData source;

	MappingContext context;

	UsagePriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new UsagePriceChargeWsDTO();
		source = new UsageChargeData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource() throws ParseException
	{
		final PerUnitUsageChargeData perUnitUsageChargeData = new PerUnitUsageChargeData();
		final UsageChargeTypeData usageChargeType = new UsageChargeTypeData();
		usageChargeType.setCode("TEST_DATA");
		perUnitUsageChargeData.setUsageChargeType(usageChargeType);
		mapper.populateTargetAttributeFromSource(perUnitUsageChargeData, target, context);
		Assert.assertEquals(perUnitUsageChargeData.getUsageChargeType().getCode(), target.getUsageType());
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

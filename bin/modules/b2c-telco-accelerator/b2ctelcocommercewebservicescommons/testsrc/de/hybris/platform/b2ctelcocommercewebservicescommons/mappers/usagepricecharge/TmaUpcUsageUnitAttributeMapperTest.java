/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageUnitWsDTO;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaUpcUsageUnitAttributeMapper}
 *
 */
@UnitTest
public class TmaUpcUsageUnitAttributeMapperTest
{
	@InjectMocks
	private final TmaUpcUsageUnitAttributeMapper mapper = new TmaUpcUsageUnitAttributeMapper();

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
		final UsageUnitData usageUnit = new UsageUnitData();
		usageUnit.setId("ID");
		usageUnit.setName("NAME");
		source.setUsageUnit(usageUnit);
		final UsageUnitWsDTO usageUnitWs = new UsageUnitWsDTO();
		usageUnitWs.setId(source.getUsageUnit().getId());
		usageUnitWs.setName(source.getUsageUnit().getName());
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(usageUnitWs.getId(), target.getUsageUnit().getId());
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

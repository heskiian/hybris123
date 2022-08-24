/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaCartEntriesProcessTypeAttributeMapper}
 */
@UnitTest
public class TmaCartEntriesProcessTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaCartEntriesProcessTypeAttributeMapper mapper = new TmaCartEntriesProcessTypeAttributeMapper();

	MappingContext context;

	OrderEntryData source;
	OrderEntryWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new OrderEntryData();
		target = new OrderEntryWsDTO();
		source.setProcessType(TmaProcessType.ACQUISITION);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getProcessType().getCode(), target.getProcessType().getId());
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


/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.UnitData;
import de.hybris.platform.core.model.product.UnitModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaUnitPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaUnitPopulatorTest
{
	private static final String UNIT_TYPE = "testUnitType";

	private TmaUnitPopulator unitPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		unitPopulator = new TmaUnitPopulator();
	}

	@Test
	public void testPopulate()
	{
		final UnitModel source = mock(UnitModel.class);
		given(source.getUnitType()).willReturn(UNIT_TYPE);
		final UnitData target = new UnitData();
		unitPopulator.populate(source, target);
		Assert.assertEquals(source.getUnitType(), target.getUnitType());
	}
}

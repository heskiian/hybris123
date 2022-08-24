/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductPriceClassData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductPriceClassModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaRequiredProductClassesPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaRequiredProductClassesPopulatorTest
{
	private static final String ID = "testId";

	private TmaRequiredProductClassesPopulator requiredProductClassesPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		requiredProductClassesPopulator = new TmaRequiredProductClassesPopulator();
	}

	@Test
	public void testPopulate()
	{
		final TmaProductPriceClassModel source = mock(TmaProductPriceClassModel.class);
		given(source.getId()).willReturn(ID);
		final TmaProductPriceClassData target = new TmaProductPriceClassData();
		requiredProductClassesPopulator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getId());
	}
}

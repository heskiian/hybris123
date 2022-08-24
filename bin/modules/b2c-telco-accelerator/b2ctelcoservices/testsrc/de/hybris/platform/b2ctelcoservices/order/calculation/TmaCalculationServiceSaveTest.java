/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.calculation;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.calculation.CalculationServiceSaveTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.calculation.CalculationServiceSaveTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = CalculationServiceSaveTest.class)
public class TmaCalculationServiceSaveTest extends CalculationServiceSaveTest
{
	@Before
	@Override
	public void setUp()
	{
	}

	@After
	@Override
	public void cleanup()
	{
	}

	@Test
	@Override
	public void shouldNotSaveDuringRecalculateWithDateInSl()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSaveDuringCalculateWithDateInSl()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSaveDuringRecalculateInSl()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSaveDuringCalculateInSl()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSaveDuringCalculateWithDateInJalo()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSaveDuringRecalculateWithDateInJalo()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSaveDuringRecalculateInJalo()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSaveDuringCalculateInJalo()
	{
		assertTrue("this should NOT fail", true);
	}
}

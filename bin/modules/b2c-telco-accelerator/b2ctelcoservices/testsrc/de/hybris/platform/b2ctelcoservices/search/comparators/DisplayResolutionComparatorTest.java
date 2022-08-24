/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.comparators;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DisplayResolutionComparatorTest
{
	private static final String VALID_RESOLUTION = "240 x 320";
	private DisplayResolutionComparator comparator;
	private String resolution1;
	private String resolution2;
	private int result;

	@Before
	public void setup()
	{
		this.comparator = new DisplayResolutionComparator();
	}

	@Test
	public void testCompareNullValues()
	{
		compareResolutions(null, null, 0);
		compareResolutions(null, VALID_RESOLUTION, 0);
		compareResolutions(VALID_RESOLUTION, null, 0);
	}

	@Test
	public void testCompareEmptyValues()
	{
		compareResolutions("", "", 0);
		compareResolutions(null, VALID_RESOLUTION, 0);
		compareResolutions(VALID_RESOLUTION, null, 0);
	}

	@Test
	public void testCompareDifferentWidths()
	{
		compareResolutions("180 x 240", "160 x 240", 1);
		compareResolutions("180 x 240", "180x240", 0);
		compareResolutions("180 x 320", "1190 x 760", -1);
	}

	@Test
	public void testCompareDifferentHeights()
	{
		compareResolutions("240x320", "240 x 160", 1);
		compareResolutions("240X320", "240 x 320", 0);
		compareResolutions("240x320", "240 X 400", -1);
	}

	@Test
	public void testInvalidResolutionFormat()
	{
		compareResolutions(VALID_RESOLUTION, "240 x abc", 0);
		compareResolutions(VALID_RESOLUTION, "2xx2", 0);
		compareResolutions("240 - 320", VALID_RESOLUTION, 0);
		compareResolutions("0", VALID_RESOLUTION, 0);
	}


	private void compareResolutions(String resolution1, String resolution2, int expectedResult)
	{
		givenResolutions(resolution1, resolution2);
		whenResolutionsAreCompared();
		thenResultIs(expectedResult);
	}

	private void givenResolutions(String resolution1, String resolution2)
	{
		this.resolution1 = resolution1;
		this.resolution2 = resolution2;
	}

	private void whenResolutionsAreCompared()
	{
		this.result = comparator.compare(resolution1, resolution2);
	}

	private void thenResultIs(int expected)
	{
		assertEquals(expected, result);
	}
}

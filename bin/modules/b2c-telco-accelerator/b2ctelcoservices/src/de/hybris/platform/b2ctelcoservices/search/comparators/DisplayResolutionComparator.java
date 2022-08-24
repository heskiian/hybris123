/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.comparators;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


/**
 * <pre>
 * Comparator for screen resolutions with format: "height x width"
 * Compares ascending by height; if heigths are equal, compares by width.
 * e.g
 * 176 x 540
 * 240 x 320
 * 240 x 400
 * </pre>
 */
public class DisplayResolutionComparator implements Comparator<String>
{
	private static final Logger LOG = LoggerFactory.getLogger(DisplayResolutionComparator.class);

	private static final String SPLIT_VALUE = "X";
	private static final int RESOLUTION_ELEMENTS_COUNT = 2;


	@Override
	public int compare(final String value1, final String value2)
	{
		try
		{
			final String[] resolution1 = parseResolution(value1);
			final String[] resolution2 = parseResolution(value2);
			validateResolution(resolution1);
			validateResolution(resolution2);
			int result = compareNumeric(resolution1[0], resolution2[0]);
			if (result == 0)
			{
				result = compareNumeric(resolution1[1], resolution2[1]);
			}
			return result;
		}
		catch (final DisplayResolutionComparatorException e)
		{
			LOG.error("Cannot compare resolutions " + value1 + ", " + value2 + ".", e);
			return 0;
		}
	}


	private String[] parseResolution(final String text) throws DisplayResolutionComparatorException
	{
		if (StringUtils.isEmpty(text))
		{
			throw new DisplayResolutionComparatorException("Null/empty resolution.");
		}
		return text.toUpperCase().split(SPLIT_VALUE);
	}

	private void validateResolution(final String[] resolution) throws DisplayResolutionComparatorException
	{
		if (resolution.length != RESOLUTION_ELEMENTS_COUNT)
		{
			throw new DisplayResolutionComparatorException("Invalid resolution format.");
		}
	}

	private int compareNumeric(final String value1, final String value2) throws DisplayResolutionComparatorException
	{
		try
		{
			return Integer.compare(Integer.parseInt(value1.trim()), Integer.parseInt(value2.trim()));
		}
		catch (final NumberFormatException e)
		{
			throw new DisplayResolutionComparatorException("Invalid resolution value(s).");
		}
	}

	private static class DisplayResolutionComparatorException extends Exception
	{
		public DisplayResolutionComparatorException(final String msg)
		{
			super(msg);
		}
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationaddon.data.impl;

import de.hybris.platform.personalizationaddon.data.CxViewValueCoder;

import java.nio.charset.Charset;
import java.util.Base64;


public class Base64ValueCoder implements CxViewValueCoder
{
	private static final Charset CHARSET = Charset.forName("UTF-8");

	@Override
	public String encode(final String input)
	{
		if (input == null)
		{
			return null;
		}
		final byte[] inputBytes = input.getBytes(CHARSET);
		return Base64.getEncoder().encodeToString(inputBytes);
	}

	@Override
	public String decode(final String input)
	{
		if (input == null)
		{
			return null;
		}
		final byte[] inputBytes = input.getBytes(CHARSET);
		final byte[] outputBytes = Base64.getDecoder().decode(inputBytes);
		return new String(outputBytes, CHARSET);
	}


}

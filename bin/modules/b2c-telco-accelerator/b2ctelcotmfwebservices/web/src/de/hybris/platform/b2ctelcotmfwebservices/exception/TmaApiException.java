/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.exception;

/**
 * Base exception class for TMFwebservices
 *
 * @since 1810
 */
public class TmaApiException extends Exception
{

	static final long serialVersionUID = -387516993124229948L;
	private final int code;

	public TmaApiException(final int code, final String msg)
	{
		super(msg);
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

}

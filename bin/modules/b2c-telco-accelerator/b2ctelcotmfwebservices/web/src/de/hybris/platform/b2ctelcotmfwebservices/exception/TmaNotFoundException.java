/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.exception;


/**
 * Throws TmaNotFoundException if entity is not found.
 *
 * @since 1810
 */
public class TmaNotFoundException extends TmaApiException
{
	private static final long serialVersionUID = -3387516993124229948L;

	public TmaNotFoundException(final int code, final String msg)
	{
		super(code, msg);
	}
}

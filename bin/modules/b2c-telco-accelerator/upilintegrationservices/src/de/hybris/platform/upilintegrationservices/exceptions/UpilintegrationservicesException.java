/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.exceptions;

/**
 * Exception class for Upilintegrationservices.
 *
 * @since 1911
 */
public class UpilintegrationservicesException extends Exception
{

	private static final long serialVersionUID = 1L;

	public UpilintegrationservicesException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UpilintegrationservicesException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public UpilintegrationservicesException(final String message)
	{
		super(message);
	}

	public UpilintegrationservicesException(final Throwable cause)
	{
		super(cause);
	}

}

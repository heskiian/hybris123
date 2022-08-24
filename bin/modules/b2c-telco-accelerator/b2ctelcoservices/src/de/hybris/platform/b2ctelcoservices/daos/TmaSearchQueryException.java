/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

/**
 * Exception to be thrown in case the search query builder cannot be created.
 *
 * @since 1810
 */
public class TmaSearchQueryException extends Exception
{
	public TmaSearchQueryException(final String message)
	{
		super(message);
	}
}

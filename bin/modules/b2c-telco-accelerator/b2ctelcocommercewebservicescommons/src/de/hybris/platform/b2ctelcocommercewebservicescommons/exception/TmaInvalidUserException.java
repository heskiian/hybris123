/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.exception;

/**
 * Throws TmaInvalidUserException if userID is null
 *
 * @since 1907
 */
public class TmaInvalidUserException extends RuntimeException
{
	public TmaInvalidUserException()
	{
		super("No [User ID] parameter specified for pre authorization");
	}
}

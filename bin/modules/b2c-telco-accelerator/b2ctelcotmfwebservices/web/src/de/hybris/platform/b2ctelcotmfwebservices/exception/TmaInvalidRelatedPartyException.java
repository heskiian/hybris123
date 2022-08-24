/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.exception;

/**
 * Throws TmaInvalidRelatedPartyException if relatedPartyId is null
 *
 * @since 1907
 */
public class TmaInvalidRelatedPartyException extends RuntimeException
{
	public TmaInvalidRelatedPartyException()
	{
		super("No [relatedPartyId] parameter specified for pre authorization");
	}
}

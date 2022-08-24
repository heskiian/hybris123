/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.exception;

import javax.servlet.ServletException;


/**
 * Throws TmaInvalidResourceException if base site id from the requested url is invalid.
 *
 * @since 1810
 */
public class TmaInvalidResourceException extends ServletException
{

	private final String baseSiteId;

	/**
	 * @param baseSiteUid
	 */
	public TmaInvalidResourceException(final String baseSiteUid)
	{
		super("Base site " + baseSiteUid + " doesn't exist");
		this.baseSiteId = baseSiteUid;
	}

	/**
	 * @return the baseSiteId
	 */
	public String getBaseSiteId()
	{
		return baseSiteId;
	}
}

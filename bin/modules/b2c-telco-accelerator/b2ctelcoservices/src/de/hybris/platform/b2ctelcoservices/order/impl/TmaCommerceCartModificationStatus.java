/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;


/**
 * Status values for the CommerceCartModification statusCode specific to TMA.
 *
 * @since 1810
 */
public enum TmaCommerceCartModificationStatus implements CommerceCartModificationStatus
{
	/**
	 * Indicates a problem between cart entries compatibility policies.
	 */
	COMPATIBILITY_ERROR("compactibilityError"),

	ELIGIBILTY_ERROR("eligibilityError");

	private String code;

	private TmaCommerceCartModificationStatus(final String code)
	{
		this.code = code;
	}

	@SuppressWarnings("javadoc")
	public String getCode()
	{
		return code;
	}


}

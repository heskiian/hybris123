/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao;

import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;


/**
 * Exception to be thrown in case the search query cannot be determined based on the {@link TmaEligibilityContext}.
 *
 * @since 1810
 */
public class TmaEligibilitySearchQueryException extends TmaSearchQueryException
{
	public TmaEligibilitySearchQueryException(String message)
	{
		super(message);
	}
}

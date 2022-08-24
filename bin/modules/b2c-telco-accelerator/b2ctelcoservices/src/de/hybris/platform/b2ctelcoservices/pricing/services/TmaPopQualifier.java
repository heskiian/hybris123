/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;


/**
 * Checks the qualification of prices {@link TmaProductOfferingPriceModel} on a {@link TmaProductOfferingModel}.
 *
 * @since 2011.
 */
public interface TmaPopQualifier
{
	/**
	 * Checks that the given price qualifies for the offering.
	 *
	 * @param price
	 * 		the Product Offering Price
	 * @param productOffering
	 * 		the Product Offering
	 * @return true if valid, false otherwise.
	 */
	boolean qualifies(final TmaProductOfferingPriceModel price, final TmaProductOfferingModel productOffering);
}

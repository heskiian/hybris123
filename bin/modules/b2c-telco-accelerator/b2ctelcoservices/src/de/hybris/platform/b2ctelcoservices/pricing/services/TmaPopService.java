/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services;


import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Set;


/**
 * Service responsible for handling {@link TmaProductOfferingPriceModel}  related operations.
 *
 * @since 2011
 */
public interface TmaPopService
{
	/**
	 * Checks that a given Product Offering Price qualifies for a Product Offering.
	 *
	 * @param price
	 * 		the product offering price.
	 * @param product
	 * 		the product offering.
	 * @return true if valid, otherwise false.
	 */
	boolean popQualifiesFor(final TmaProductOfferingPriceModel price, final TmaProductOfferingModel product);

	/**
	 * Finds all the {@link PriceRowModel}s that a {@link TmaProductOfferingPriceModel} or its parents are used in.
	 *
	 * @param price
	 * 		the product offering price.
	 * @return the price rows found.
	 */
	Set<PriceRowModel> getPriceRowsFor(final TmaProductOfferingPriceModel price);

	/**
	 * Returns a {@link TmaProductOfferingPriceModel} for the given code.
	 *
	 * @param code
	 * 		identifier of {@link TmaProductOfferingPriceModel}
	 * @return the {@link TmaProductOfferingPriceModel} found.
	 * @throws ModelNotFoundException
	 * 		if no product offering price is found.
	 */
	TmaProductOfferingPriceModel getPop(final String code);
}

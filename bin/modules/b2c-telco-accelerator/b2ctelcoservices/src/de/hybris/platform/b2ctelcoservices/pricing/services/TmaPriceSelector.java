/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services;

import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Set;


/**
 * Responsible for selecting a price from a list of prices
 *
 * @since 2007
 */
public interface TmaPriceSelector
{

	/**
	 * Selects a price from the given input prices
	 *
	 * @param standAlonePrices
	 * 		the stand alone prices
	 * @param priceOverrides
	 *        the price overrides.
	 * @return the price selected from the input prices.
	 */
	PriceRowModel selectPrice(final Set<PriceRowModel> standAlonePrices, final Set<PriceRowModel> priceOverrides);
}

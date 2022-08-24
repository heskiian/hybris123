/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.pricing.comparator.PriceRowModelComparator;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceSelector;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Selects the minimum price from the list of prices
 *
 * @since 2007
 * @deprecated since 2007
 */
@Deprecated(since = "2007")
public class DefaultTmaMinimumPriceSelector implements TmaPriceSelector
{
	private PriceRowModelComparator priceRowComparator;

	public DefaultTmaMinimumPriceSelector(final PriceRowModelComparator priceRowComparator)
	{
		this.priceRowComparator = priceRowComparator;
	}

	@Override
	public PriceRowModel selectPrice(final Set<PriceRowModel> standalonePrices, final Set<PriceRowModel> priceOverrides)
	{
		final Set<PriceRowModel> prices = standalonePrices;
		prices.addAll(priceOverrides);
		return CollectionUtils.isNotEmpty(prices) ? Collections.min(prices, getPriceRowComparator()) : null;
	}

	protected PriceRowModelComparator getPriceRowComparator()
	{
		return priceRowComparator;
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceSelector;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Selects the price which has the highest priority from the list
 *
 * @since 2007
 */
public class DefaultTmaHighestPriorityPriceSelector implements TmaPriceSelector
{

	@Override
	public PriceRowModel selectPrice(final Set<PriceRowModel> standalonePrices, final Set<PriceRowModel> priceOverrides)
	{
		if (CollectionUtils.isEmpty(priceOverrides))
		{
			return CollectionUtils.isNotEmpty(standalonePrices) ? standalonePrices.iterator().next() : null;
		}
		if (CollectionUtils.isEmpty(standalonePrices))
		{
			return priceOverrides.iterator().next();
		}

		return priceOverrides.iterator().next().getPriority() > standalonePrices.iterator().next().getPriority() ?
				priceOverrides.iterator().next() : standalonePrices.iterator().next();
	}
}

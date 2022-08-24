/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Strategy to compute the order entry prices
 *
 * @since 1907
 */
public interface TmaFindAbstractOrderEntryPriceStrategy
{

	/**
	 * Find the base price for an {@link AbstractOrderEntryModel}
	 *
	 * @param entry
	 * 		the order entry for which the price will be computed
	 * @param taxes
	 * 		the taxes that should be applied on the order entry
	 * @return the populated price for the given order entry
	 */
	TmaAbstractOrderPriceModel findBasePrice(final AbstractOrderEntryModel entry, final List<TaxValue> taxes);
}

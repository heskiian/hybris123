/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder for creating the list of {@link TmaAbstractOrderPriceModel} to be set on the order entry
 *
 * @since 1907
 */
public interface TmaAbstractOrderEntryPriceBuilder
{
	/**
	 * Builds a list of {@link TmaAbstractOrderPriceModel} for the given param.
	 *
	 * @param price
	 * 		the price used to create and populate the order entry prices list
	 * @param entry
	 * 		the order entry for which the price is created
	 * @param taxes
	 * 		the taxes to be applied on the order price
	 * @return the list of order entry prices
	 */
	List<TmaAbstractOrderPriceModel> buildPrices(final PriceRowModel price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes);
}

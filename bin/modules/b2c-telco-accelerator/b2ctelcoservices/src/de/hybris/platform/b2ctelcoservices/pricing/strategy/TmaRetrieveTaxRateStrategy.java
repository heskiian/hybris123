/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Strategy to compute and retrieve tax rate for an order price or order entry price
 *
 * @since 1907
 */
public interface TmaRetrieveTaxRateStrategy
{

	/**
	 * Retrieves the tax rate for an order entry price
	 *
	 * @param price
	 * 		the price without taxes for which the taxes will be applied
	 * @param entry
	 * 		the order entry
	 * @param taxes
	 * 		the taxes to be applied
	 * @return the tax rate after applying the taxes
	 */
	Double retrieveTaxRate(final Double price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes);

	/**
	 * Retrieves the tax rate for an order price
	 *
	 * @param price
	 * 		the price without taxes for which the taxes will be applied
	 * @param order
	 * 		the order with the price to apply the taxes on
	 * @param taxes
	 * 		the taxes to be applied
	 * @return the tax rate after applying the taxes
	 */
	Double retrieveTaxRate(final Double price, final AbstractOrderModel order, final List<TaxValue> taxes);
}

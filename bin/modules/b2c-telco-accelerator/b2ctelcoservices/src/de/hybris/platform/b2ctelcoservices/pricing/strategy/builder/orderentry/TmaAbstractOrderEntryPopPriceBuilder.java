/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder for creating the {@link TmaAbstractOrderPriceModel} to be set on the order entry
 *
 * @since 2007
 */
public interface TmaAbstractOrderEntryPopPriceBuilder<T extends TmaProductOfferingPriceModel>
{

	/**
	 * Builds a {@link TmaAbstractOrderPriceModel} for the given param.
	 *
	 * @param productOfferingPrice
	 * 		the product offering price used to create and populate the order entry prices list
	 * @param entry
	 * 		the order entry for which the price is created
	 * @param taxes
	 * 		the taxes to be applied on the order price
	 * @return the list of order entry prices
	 */
	TmaAbstractOrderPriceModel buildPrice(final T productOfferingPrice, final AbstractOrderEntryModel entry, final List<TaxValue> taxes);
}

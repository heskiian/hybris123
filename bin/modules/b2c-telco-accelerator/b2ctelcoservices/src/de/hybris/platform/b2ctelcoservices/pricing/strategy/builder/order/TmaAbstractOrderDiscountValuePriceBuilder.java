/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder for creating a {@link TmaAbstractOrderPriceModel} from discount value.
 *
 * @since 2007
 */
public interface TmaAbstractOrderDiscountValuePriceBuilder
{
	/**
	 * Builds an instance of {@link TmaAbstractOrderPriceModel} for the given param.
	 *
	 * @param order
	 * 		the order for which the prices are created
	 * @param taxes
	 * 		the taxes to be applied on the order price
	 * @return the list of order entry prices
	 */
	TmaAbstractOrderPriceModel buildPrice(final DiscountValue discountValue, final AbstractOrderModel order,
			final List<TaxValue> taxes);
}

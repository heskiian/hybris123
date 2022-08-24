/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Builder for creating {@link TmaAbstractOrderPriceModel} to be set on the order
 *
 * @since 1907
 */
public interface TmaAbstractOrderPriceBuilder
{
	/**
	 * Builds a {@link TmaAbstractOrderPriceModel} for the given param.
	 *
	 * @param price
	 * 		the price value to be set on the order price
	 * @param order
	 * 		the order for which the price is created
	 * @param taxes
	 * 		the taxes to be applied on the order price
	 * @param priceType
	 * 		the price type to be set on the order price
	 * @return created and populated order price
	 */
	TmaAbstractOrderPriceModel buildPrice(final Double price, final AbstractOrderModel order, final List<TaxValue> taxes,
			final TmaAbstractOrderPriceType priceType);
}

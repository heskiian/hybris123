/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Custom implementation of the {@link PriceDataFactory}.
 *
 * @since 6.7
 */
public interface TmaPriceDataFactory extends PriceDataFactory, Serializable
{
	/**
	 * Creates a PriceData object with a formatted currency string based on the price type and currency ISO code.
	 *
	 * @param priceType
	 * 		price type of the resulting price data
	 * @param value
	 * 		price amount
	 * @param currencyIso
	 * 		currency ISO code determining the currency
	 * @return a newly created {@link SubscriptionPricePlanData}
	 */
	SubscriptionPricePlanData create(final PriceDataType priceType, final BigDecimal value, final String currencyIso);

	/**
	 * Creates a PriceData object with a formatted currency string based on the price type and currency.
	 *
	 * @param priceType
	 * 		price type of the resulting price data
	 * @param value
	 * 		price amount
	 * @param currency
	 * 		currency to be used by the price
	 * @return a newly created {@link SubscriptionPricePlanData}
	 */
	SubscriptionPricePlanData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency);

	/**
	 * Retrieves the price value of a {@link PriceData}. The priorities are the following:
	 * <ul>
	 * <li>if the price has recurring charges, return the first recurring charge</li>
	 * <li>if the price has a value, return the value</li>
	 * <li>if the price has one time charges, return the first one time charge </li>
	 * </ul>
	 *
	 * @param price
	 * 		price for which to retrieve the value
	 * @return price value considering the priorities mentioned above
	 */
	BigDecimal getValueForPrice(final PriceData price);

	/**
	 * Returns the {@link PriceDataType} for the {@param product} given.
	 *
	 * @param product
	 * 		product to retrieve the price data type for
	 * @return price data type for the product given
	 */
	PriceDataType getPriceDataTypeForProduct(final ProductModel product);

	/**
	 * Obtains a formatted string for the given price value and currency.
	 *
	 * @param value
	 * 		the price value
	 * @param currency
	 * 		the currency
	 * @return formatted String with the price value and currency
	 */
	String formatPriceValue(final BigDecimal value, final CurrencyModel currency);
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price;


import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;


/**
 * Class responsible for formatting a price value.
 *
 * @since 2007
 */
public interface TmaPriceValueFormatter
{
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

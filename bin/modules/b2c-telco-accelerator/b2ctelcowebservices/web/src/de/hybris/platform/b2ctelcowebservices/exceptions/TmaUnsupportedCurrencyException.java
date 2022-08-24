/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.exceptions;

import de.hybris.platform.core.model.c2l.CurrencyModel;

import javax.servlet.ServletException;


/**
 * Thrown when currency is not supported for current base store configuration.
 *
 * @since 1810
 *
 */
public class TmaUnsupportedCurrencyException extends ServletException
{

	private final CurrencyModel currency;

	/**
	 * @param currencyToSet
	 */
	public TmaUnsupportedCurrencyException(final CurrencyModel currencyToSet)
	{
		super("Currency " + currencyToSet + " is not supported by the current base store");
		this.currency = currencyToSet;
	}

	public TmaUnsupportedCurrencyException(final CurrencyModel currencyToSet, final Throwable rootCouse)
	{
		super("Currency " + currencyToSet + " is not supported by the current base store", rootCouse);
		this.currency = currencyToSet;
	}

	/**
	 * @param msg
	 */
	public TmaUnsupportedCurrencyException(final String msg)
	{
		super(msg);
		currency = null;
	}

	public TmaUnsupportedCurrencyException(final String msg, final Throwable rootCouse)
	{
		super(msg, rootCouse);
		currency = null;
	}

	/**
	 * @return the currency
	 */
	public CurrencyModel getCurrency()
	{
		return currency;
	}
}

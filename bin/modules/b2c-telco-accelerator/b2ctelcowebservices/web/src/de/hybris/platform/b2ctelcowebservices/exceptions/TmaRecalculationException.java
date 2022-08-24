/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.exceptions;

import de.hybris.platform.order.exceptions.CalculationException;

import javax.servlet.ServletException;


/**
 * Thrown when recalculation of cart is impossible due to any reasons.
 * 
 * @since 1810
 */
public class TmaRecalculationException extends ServletException
{
	public TmaRecalculationException(final CalculationException exception, final String currencyIso)
	{
		super("Cannot recalculate cart for currency: " + currencyIso, exception);
	}
}

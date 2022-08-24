/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;
import java.util.List;


/**
 * Default implementation of {@link TmaRetrieveTaxRateStrategy}
 *
 * @since 1907
 */
public class DefaultTmaRetrieveTaxRateStrategy implements TmaRetrieveTaxRateStrategy
{

	@Override
	public Double retrieveTaxRate(final Double price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		final AbstractOrderModel order = entry.getOrder();
		final Boolean isNet = order.getNet();
		final CurrencyModel currency = order.getCurrency();
		final int digits = currency.getDigits();
		final String isoCode = currency.getIsocode();
		final double quantity = entry.getQuantity();

		final Collection<TaxValue> appliedTaxes = TaxValue.apply(quantity, price, digits, taxes, isNet, isoCode);
		return CoreAlgorithms.round(appliedTaxes.stream().mapToDouble(TaxValue::getAppliedValue).sum(), digits);
	}

	@Override
	public Double retrieveTaxRate(final Double price, final AbstractOrderModel order, final List<TaxValue> taxes)
	{
		final CurrencyModel orderCurrency = order.getCurrency();
		final int digits = orderCurrency.getDigits();
		final String isoCode = orderCurrency.getIsocode();
		final Boolean isNet = order.getNet();

		final Collection<TaxValue> appliedTaxes = TaxValue.apply(1, price, digits, taxes, isNet, isoCode);
		return CoreAlgorithms.round(appliedTaxes.stream().mapToDouble(TaxValue::getAppliedValue).sum(), digits);
	}
}

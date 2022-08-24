/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.impl;

import de.hybris.platform.b2ctelcofacades.price.TmaPriceValueFormatter;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Default implementation of the price value formatter.
 *
 * @since 2007
 */
public class DefaultTmaPriceValueFormatter implements TmaPriceValueFormatter
{
	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private I18NService i18NService;

	private final ConcurrentMap<String, NumberFormat> currencyFormats = new ConcurrentHashMap<>();

	public DefaultTmaPriceValueFormatter(final CommonI18NService commonI18NService,
			final CommerceCommonI18NService commerceCommonI18NService, final I18NService i18NService)
	{
		this.commonI18NService = commonI18NService;
		this.commerceCommonI18NService = commerceCommonI18NService;
		this.i18NService = i18NService;
	}

	@Override
	public String formatPriceValue(BigDecimal value, CurrencyModel currency)
	{
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);
		if (locale == null)
		{
			// Fallback to session locale
			locale = getI18NService().getCurrentLocale();
		}

		final NumberFormat currencyFormat = createCurrencyFormat(locale, currency);
		return currencyFormat.format(value);
	}

	/**
	 * Returns the currency formatted based on the given locale
	 *
	 * @param locale
	 * 		current locale which determines how the currency should be formatted.
	 * @param currency
	 * 		currency which needs to be formatted
	 * @return A clone of {@link NumberFormat} from the instance in the local cache, if the cache does not contain an
	 * instance of a NumberFormat for a given locale and currency one would be added.
	 */
	protected NumberFormat createCurrencyFormat(final Locale locale, final CurrencyModel currency)
	{
		final String key = locale.getISO3Country() + "_" + currency.getIsocode();

		NumberFormat numberFormat = currencyFormats.get(key);
		if (numberFormat == null)
		{
			final NumberFormat currencyFormat = createNumberFormat(locale, currency);
			numberFormat = currencyFormats.putIfAbsent(key, currencyFormat);
			if (numberFormat == null)
			{
				numberFormat = currencyFormat;
			}
		}
		// don't allow multiple references
		return (NumberFormat) numberFormat.clone();
	}

	protected NumberFormat createNumberFormat(final Locale locale, final CurrencyModel currency)
	{
		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		adjustDigits(currencyFormat, currency);
		adjustSymbol(currencyFormat, currency);
		return currencyFormat;
	}

	/**
	 * Adjusts {@link DecimalFormat}'s fraction digits according to given {@link CurrencyModel}.
	 *
	 * @param format
	 * 		decimal format which needs to be adjusted based on the input currency.
	 * @param currencyModel
	 * 		currency to be used in the formatting logic
	 * @return adjusted value based on the given currency's digits.
	 */
	protected DecimalFormat adjustDigits(final DecimalFormat format, final CurrencyModel currencyModel)
	{
		final int tempDigits = currencyModel.getDigits() == null ? 0 : currencyModel.getDigits().intValue();
		final int digits = Math.max(0, tempDigits);

		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);
		if (digits == 0)
		{
			format.setDecimalSeparatorAlwaysShown(false);
		}

		return format;
	}

	/**
	 * Adjusts {@link DecimalFormat}'s symbol according to given {@link CurrencyModel}.
	 *
	 * @param format
	 * 		decimal format which needs to be adjusted based on the input currency.
	 * @param currencyModel
	 * 		currency to be used in the formatting logic
	 * @return adjusted value based on the given currency's symbol.
	 */
	protected DecimalFormat adjustSymbol(final DecimalFormat format, final CurrencyModel currencyModel)
	{
		final String symbol = currencyModel.getSymbol();
		if (symbol != null)
		{
			final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols(); // does cloning
			final String iso = currencyModel.getIsocode();
			boolean changed = false;
			if (!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
			{
				symbols.setInternationalCurrencySymbol(iso);
				changed = true;
			}
			if (!symbol.equals(symbols.getCurrencySymbol()))
			{
				symbols.setCurrencySymbol(symbol);
				changed = true;
			}
			if (changed)
			{
				format.setDecimalFormatSymbols(symbols);
			}
		}
		return format;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

}

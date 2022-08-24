/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price.impl;

import de.hybris.platform.b2ctelcoservices.dataimport.batch.price.TmaPriceRowExportAdapter;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.StandardDateRange;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Default implementation of {@link TmaPriceRowExportAdapter}.
 *
 * @since 1907
 */
public class DefaultTmaPriceRowExportAdapter implements TmaPriceRowExportAdapter
{
	private static final String COLLECTION_DELIMITER = "collection-delimiter";
	private static final String DATE_FORMAT_STRING = "dateformat";
	private static final String NUMBER_FORMAT_STRING = "numberformat";
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private static final char EQUAL_SYMBOL = '=';
	private static final char COMMA_SYMBOL = ',';
	private static final char OPEN_SQUARE_BRACKET_SYMBOL = '[';
	private static final char CLOSE_SQUARE_BRACKET_SYMBOL = ']';
	private static final char LINE_END_SYMBOL = 'N';
	private static final int DATE_STYLE = 2;
	private static final int TIME_STYLE = 2;

	private char collectionValueDelimiter;
	private SimpleDateFormat dateFormat;
	private NumberFormat numberFormat;

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor)
	{
		final String customDelimiter = columnDescriptor.getDescriptorData().getModifier(COLLECTION_DELIMITER);
		collectionValueDelimiter = StringUtils.length(customDelimiter) > 0 ? customDelimiter.charAt(0) : COMMA_SYMBOL;
		final Locale locale = columnDescriptor.getHeader().getReader().getLocale();
		dateFormat = StringUtils.isNotBlank(getDateFormatString(columnDescriptor))
				? new SimpleDateFormat(getDateFormatString(columnDescriptor), locale)
				: (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(DATE_STYLE, TIME_STYLE, locale);
		numberFormat = StringUtils.isNotBlank(getNumberFormatString(columnDescriptor))
				? new DecimalFormat(getNumberFormatString(columnDescriptor), new DecimalFormatSymbols(locale))
				: NumberFormat.getInstance(locale);
	}

	@Override
	public String performExport(final Object value)
	{
		if (!ObjectUtils.isEmpty(value))
		{
			final List strings = new LinkedList();
			final Iterator iter = ((Collection) value).iterator();
			while (iter.hasNext())
			{
				strings.add(exportValue(iter.next()));
			}
			return joinAndEscape(strings);
		}
		else
		{
			return StringUtils.EMPTY;
		}
	}

	protected String joinAndEscape(final List strings)
	{
		return CSVUtils.joinAndEscape(strings, (char[]) null, collectionValueDelimiter, false);
	}

	public final String exportValue(final Object value)
	{
		return ObjectUtils.isEmpty(value) ? StringUtils.EMPTY : convertToString(value);
	}

	private String convertToString(final Object value)
	{
		final PriceRowModel pricerow = (PriceRowModel) value;

		final CurrencyModel currency = pricerow.getCurrency();
		final double price = pricerow.getPrice();
		final UnitModel unit = pricerow.getUnit();
		final UserModel user = pricerow.getUser();
		final long quantity = pricerow.getMinqtd();
		final boolean net = pricerow.getNet();
		final StandardDateRange dateRange = pricerow.getDateRange();
		final HybrisEnumValue productPriceGroup = pricerow.getPg();
		final HybrisEnumValue userPriceGroup = pricerow.getUg();
		final PriceRowChannel priceRowChannel = pricerow.getChannel();

		final StringBuilder text = new StringBuilder();
		text.append(appendUserAndGroups(user, userPriceGroup, productPriceGroup));
		if (quantity > 0L)
		{
			text.append(quantity).append(StringUtils.SPACE);
		}
		if (!ObjectUtils.isEmpty(unit))
		{
			text.append(unit.getCode()).append(StringUtils.SPACE).append(EQUAL_SYMBOL).append(StringUtils.SPACE);
		}
		if (price >= 0.0D)
		{
			final NumberFormat numberformat = getNumberFormat();
			synchronized (numberformat)
			{
				text.append(numberformat.format(price)).append(StringUtils.SPACE);
			}
		}
		if (!ObjectUtils.isEmpty(currency))
		{
			text.append(currency.getIsocode()).append(StringUtils.SPACE);
		}
		if (net)
		{
			text.append(String.valueOf(LINE_END_SYMBOL)).append(StringUtils.SPACE);
		}
		text.append(appendDateFormat(dateRange));
		if (!ObjectUtils.isEmpty(priceRowChannel))
		{
			text.append(priceRowChannel.getCode()).append(StringUtils.SPACE);
		}
		return text.toString();
	}

	private Object appendDateFormat(final StandardDateRange dateRange)
	{
		final StringBuilder text = new StringBuilder();
		if (!ObjectUtils.isEmpty(dateRange))
		{
			text.append(OPEN_SQUARE_BRACKET_SYMBOL);
			final DateFormat dateformat = getDateFormat();
			synchronized (dateformat)
			{
				text.append(dateformat.format(dateRange.getStart()));
			}
			text.append(COMMA_SYMBOL);
			synchronized (dateformat)
			{
				text.append(dateformat.format(dateRange.getEnd()));
			}
			text.append(CLOSE_SQUARE_BRACKET_SYMBOL).append(StringUtils.SPACE);
		}
		return text;
	}

	private StringBuilder appendUserAndGroups(final UserModel user, final HybrisEnumValue userPriceGroup,
			final HybrisEnumValue productPriceGroup)
	{
		final StringBuilder text = new StringBuilder();
		if (!ObjectUtils.isEmpty(user))
		{
			text.append(user.getUid()).append(StringUtils.SPACE);
		}
		else if (!ObjectUtils.isEmpty(userPriceGroup))
		{
			text.append(userPriceGroup.getCode()).append(StringUtils.SPACE);
		}
		else if (!ObjectUtils.isEmpty(productPriceGroup))
		{
			text.append(productPriceGroup.getCode()).append(StringUtils.SPACE);
		}
		return text;
	}

	protected String getDateFormatString(final SpecialColumnDescriptor columnDescriptor)
	{
		final String format = !ObjectUtils.isEmpty(columnDescriptor)
				? columnDescriptor.getDescriptorData().getModifier(DATE_FORMAT_STRING)
				: null;
		return StringUtils.length(format) > 0 ? format : DATE_FORMAT;
	}

	protected String getNumberFormatString(final SpecialColumnDescriptor columnDescriptor)
	{
		final String format = !ObjectUtils.isEmpty(columnDescriptor)
				? columnDescriptor.getDescriptorData().getModifier(NUMBER_FORMAT_STRING)
				: null;
		return StringUtils.length(format) > 0 ? format : null;
	}

	protected NumberFormat getNumberFormat()
	{
		return this.numberFormat;
	}

	protected DateFormat getDateFormat()
	{
		return this.dateFormat;
	}
}

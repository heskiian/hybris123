/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.dataimport.batch.price.impl;

import de.hybris.platform.b2ctelcoservices.dataimport.batch.price.TmaPriceRowImportAdapter;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;

import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Default implementation of {@link TmaPriceRowImportAdapter}.
 *
 * @since 1907
 */
public class DefaultTmaPriceRowImportAdapter implements TmaPriceRowImportAdapter
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaPriceRowImportAdapter.class.getName());
	private static final String NET = "net";
	private static final String USER = "user";
	private static final String PRICE = "price";
	private static final String QUANTITY = "quantity";
	private static final String CHANNEL = "channel";
	private static final String DATERANGE = "dateRange";
	private static final String CURRENCY2USE = "currency2use";
	private static final String UNIT2USE = "unit2use";
	private static final String PRODUCTPRICEGROUP = "productPriceGroup";
	private static final String USERPRICEGROUP = "userPriceGroup";
	private static final String START_OF_CURRENCY_INDEX = "startOfCurrencyIndex";
	private static final String COLLECTION_DELIMITER = "collection-delimiter";
	private static final String MODE = "mode";
	private static final String APPEND_MODE = "append";
	private static final String REMOVE_MODE = "remove";
	private static final String IGNORE_NULL = "ignorenull";
	private static final String APPEND_SYMBOL = "(+)";
	private static final String REMOVE_SYMBOL = "(-)";
	private static final String EQUAL_SYMBOL = "=";
	private static final String DATE_FORMAT_STRING = "dateformat";
	private static final String NUMBER_FORMAT_STRING = "numberformat";
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private static final String IGNORE_STRING = "<ignore>";
	private static final int COUNT = 2;
	private static final int DATE_STYLE = 2;
	private static final int TIME_STYLE = 2;
	private static final int LAST_HOUR_OF_DAY = 23;
	private static final int LAST_MINUTE_OF_DAY = 59;
	private static final int LAST_SECOND_OF_DAY = 59;
	private static final char COMMA_SYMBOL = ',';

	private char collectionValueDelimiter;

	private ModelService modelService;
	private UserService userService;
	private UnitService unitService;
	private EnumerationService enumerationService;
	private CurrencyDao currencyDao;

	private SimpleDateFormat dateFormat;
	private NumberFormat numberFormat;
	private Locale locale;
	private boolean ignoreNullElements;
	private boolean globalAdd;
	private boolean globalRemove;
	private Map<String, CurrencyModel> currenciesISOs;
	private Map<String, CurrencyModel> currenciesSymbols;
	private Map<String, PriceRowChannel> priceRowChannelsMap;
	private Map<String, ProductPriceGroup> priceProductGroupsMap;
	private Map<String, UserPriceGroup> priceUserGroupsMap;
	private volatile boolean lastWasUnresolved;

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor)
	{
		globalAdd = false;
		globalRemove = false;
		lastWasUnresolved = false;
		currenciesISOs = new HashMap<>();
		currenciesSymbols = new HashMap<>();
		priceRowChannelsMap = new HashMap<>();
		priceProductGroupsMap = new HashMap<>();
		priceUserGroupsMap = new HashMap<>();
		setupDelimiter(columnDescriptor);
		setupPriceRowChannelsMap();
		setupPriceProductGroupsMap();
		setupPriceUserGroupsMap();
		findCurrencies();
		setupDateAndNumberFormat(columnDescriptor);
		setupModes(columnDescriptor);
		ignoreNullElements = !StringUtils.equalsIgnoreCase(Boolean.FALSE.toString(),
				columnDescriptor.getDescriptorData().getModifier(IGNORE_NULL));
	}

	@Override
	public List<PriceRowModel> performImport(String cellValue, final Item processedItem)
	{
		clearStatus();
		cellValue = removeIgnoreString(cellValue);
		if (!ObjectUtils.isEmpty(processedItem)
				&& isModeApplied(cellValue))
		{
			return processCellValueWithMode(cellValue, processedItem);
		}
		else
		{
			return processCellValueWithoutMode(cellValue, processedItem);
		}
	}

	private String removeIgnoreString(final String cellValue)
	{
		if (!ObjectUtils.nullSafeEquals(StringUtils.indexOf(cellValue, IGNORE_STRING, 0), -1))
		{
			return StringUtils.replaceFirst(cellValue, IGNORE_STRING, StringUtils.EMPTY);
		}
		return cellValue;
	}

	private List<PriceRowModel> processCellValueWithoutMode(final String cellValue, final Item processedItem)
	{
		final List<PriceRowModel> priceRowList = new ArrayList<>();
		if (StringUtils.isNotBlank(cellValue))
		{
			final Iterator iter = splitAndUnescape(cellValue).iterator();
			while (iter.hasNext())
			{
				final String token = (String) iter.next();
				processItem(token, processedItem, priceRowList, true, !ignoreNullElements);
			}
		}
		else
		{
			processItem((String) null, processedItem, priceRowList, true, false);
		}
		return priceRowList;
	}

	private List<PriceRowModel> processCellValueWithMode(final String cellValue, final Item processedItem)
	{
		try
		{
			final ProductModel product = getModelService().get(processedItem);
			return processModifyCollection(cellValue, processedItem, (List<PriceRowModel>) product.getEurope1Prices());
		}
		catch (final Exception e)
		{
			throw new SystemException(e);
		}
	}

	private boolean isModeApplied(final String cellValue)
	{
		return (globalAdd || globalRemove
				|| !ObjectUtils.nullSafeEquals(StringUtils.indexOf(cellValue, APPEND_SYMBOL), -1)
				|| !ObjectUtils.nullSafeEquals(StringUtils.indexOf(cellValue, REMOVE_SYMBOL), -1));
	}

	private List<PriceRowModel> processModifyCollection(final String valueExpr, final Item forItem,
			final List<PriceRowModel> currentValues)
	{
		final List<PriceRowModel> priceRowList = new ArrayList();
		if (!CollectionUtils.isEmpty(currentValues))
		{
			priceRowList.addAll(currentValues);
		}
		if (StringUtils.isNotBlank(valueExpr))
		{
			final Iterator iter = splitAndUnescape(valueExpr).iterator();
			while (iter.hasNext())
			{
				processToken(iter, forItem, priceRowList);
			}
		}
		return priceRowList;
	}

	private void processToken(final Iterator iter, final Item forItem, final List<PriceRowModel> priceRowList)
	{
		String token = (String) iter.next();
		boolean doAppend = false;
		final boolean appendPrefix = token.startsWith(APPEND_SYMBOL);
		final boolean removePrefix = token.startsWith(REMOVE_SYMBOL);
		if (!appendPrefix && (removePrefix || !globalAdd))
		{
			if (!removePrefix && (!globalRemove))
			{
				throw new SystemException(
						"Invalid syntax! The assigned collectionvalues must either have one of the prefixes '(+)' or '(-)' or modifier mode must be set ");
			}

			if (removePrefix)
			{
				token = StringUtils.trim(StringUtils.substring(token, StringUtils.length(REMOVE_SYMBOL)));
			}
			doAppend = false;
		}
		else
		{
			if (appendPrefix)
			{
				token = StringUtils.trim(StringUtils.substring(token, StringUtils.length(APPEND_SYMBOL)));
			}
			doAppend = true;
		}
		processItem(token, forItem, priceRowList, doAppend, !ignoreNullElements);
	}

	protected boolean processItem(final String token, final Item forItem, final List<PriceRowModel> col, final boolean append,
			final boolean allowNull)
	{
		final List<PriceRowModel> elements = importValue(StringUtils.isEmpty(token) ? null : token, forItem);
		if (wasUnresolved())
		{
			setError();
			return false;
		}
		else if (CollectionUtils.isEmpty(elements) && !allowNull)
		{
			return false;
		}
		if (append)
		{
			col.addAll(elements);
		}
		else
		{
			col.removeAll(elements);
		}
		return true;
	}

	private final List<PriceRowModel> importValue(final String valueExpr, final Item toItem)
	{
		clearStatus();
		List<PriceRowModel> priceRowList = new ArrayList<>();
		if (StringUtils.isEmpty(valueExpr))
		{
			priceRowList = Collections.emptyList();
		}
		else
		{
			priceRowList.add(convertToModel(valueExpr, toItem));
		}
		return priceRowList;
	}

	private PriceRowModel convertToModel(final String valueExpr, final Item forItem)
	{
		double price;
		long quantity;
		UnitModel unit2use = null;
		UserModel user = null;
		boolean net = false;
		final ProductPriceGroup productPriceGroup = null;
		final UserPriceGroup userPriceGroup = null;
		PriceRowModel priceRowModel = null;
		final Map<String, Object> priceValuesMap = new HashMap<>();
		final ProductModel product = getModelService().get(forItem);
		final Map<String, Object> parsedCurrMap = parseCurrency(valueExpr);
		final int startOfCurrencyIndex = (int) parsedCurrMap.get(START_OF_CURRENCY_INDEX);
		final CurrencyModel currency2use = (CurrencyModel) parsedCurrMap.get(CURRENCY2USE);
		if (ObjectUtils.isEmpty(currency2use))
		{
			throw new ModelNotFoundException("Unable to find the currency definition within " + valueExpr);
		}
		if (!ObjectUtils.nullSafeEquals(StringUtils.indexOf(valueExpr, " N "), -1) || StringUtils.endsWith(valueExpr, " N"))
		{
			net = true;
		}
		final int equalsPos = StringUtils.indexOf(valueExpr, EQUAL_SYMBOL);
		if (!ObjectUtils.nullSafeEquals(equalsPos, -1))
		{
			price = setPriceValue(valueExpr, equalsPos + 1, startOfCurrencyIndex);
			final int lastNumericPos = setLastNumericPos(valueExpr, equalsPos);
			final String unitCode = StringUtils.trim(StringUtils.substring(valueExpr, lastNumericPos + 1, equalsPos));
			unit2use = setUnit2use(unitCode, product, valueExpr);
			final int firstNumericPos = getFirstNumericPos(valueExpr, lastNumericPos);
			final String quantityExpr = StringUtils.trim(StringUtils.substring(valueExpr, firstNumericPos, lastNumericPos + 1));
			quantity = Long.parseLong(quantityExpr);
			user = setPriceGroupsAndReturnUser(valueExpr, firstNumericPos, productPriceGroup, userPriceGroup);
		}
		else
		{
			price = setPriceValue(valueExpr, 0, startOfCurrencyIndex);
			if (ObjectUtils.isEmpty(product.getUnit()))
			{
				throw new InvalidParameterException("missing unit within " + valueExpr);
			}
			unit2use = product.getUnit();
			quantity = 1L;
		}
		priceValuesMap.put(NET, net);
		priceValuesMap.put(USER, user);
		priceValuesMap.put(PRICE, price);
		priceValuesMap.put(QUANTITY, quantity);
		priceValuesMap.put(CHANNEL, parseChannel(valueExpr));
		priceValuesMap.put(UNIT2USE, unit2use);
		priceValuesMap.put(CURRENCY2USE, currency2use);
		priceValuesMap.put(DATERANGE, parseDateRange(valueExpr));
		priceValuesMap.put(PRODUCTPRICEGROUP, productPriceGroup);
		priceValuesMap.put(USERPRICEGROUP, userPriceGroup);
		final Collection<PriceRowModel> priceRowsForProduct = product.getEurope1Prices();
		for (final PriceRowModel priceRow : priceRowsForProduct)
		{
			if (checkForUserAndGroups(priceRow, priceValuesMap) && checkForCurrencyAndUnitAndQuantity(priceRow, priceValuesMap)
					&& checkForDateRangeAndChannel(priceRow, priceValuesMap))
			{
				return priceRow;
			}
		}
		priceRowModel = createPriceRow(product, priceValuesMap);
		return priceRowModel;
	}

	private PriceRowModel createPriceRow(final ProductModel product, final Map<String, Object> priceValuesMap)
	{
		final PriceRowModel priceRowModel = getModelService().create(PriceRowModel.class);
		priceRowModel.setProduct(product);
		priceRowModel.setPg((HybrisEnumValue) priceValuesMap.get(PRODUCTPRICEGROUP));
		priceRowModel.setUser((UserModel) priceValuesMap.get(USER));
		priceRowModel.setUg((HybrisEnumValue) priceValuesMap.get(USERPRICEGROUP));
		priceRowModel.setMinqtd((Long) priceValuesMap.get(QUANTITY));
		priceRowModel.setCurrency((CurrencyModel) priceValuesMap.get(CURRENCY2USE));
		priceRowModel.setUnit((UnitModel) priceValuesMap.get(UNIT2USE));
		priceRowModel.setUnitFactor(1);
		priceRowModel.setNet((Boolean) priceValuesMap.get(NET));
		priceRowModel.setDateRange((StandardDateRange) priceValuesMap.get(DATERANGE));
		priceRowModel.setPrice((Double) priceValuesMap.get(PRICE));
		priceRowModel.setChannel((PriceRowChannel) priceValuesMap.get(CHANNEL));
		return priceRowModel;
	}

	private int setLastNumericPos(final String valueExpr, final int equalsPos)
	{
		int lastNumericPos = -1;
		for (int pos = 0; pos < equalsPos; pos++)
		{
			lastNumericPos = Character.isDigit(valueExpr.charAt(pos)) ? pos : lastNumericPos;
		}
		if (ObjectUtils.nullSafeEquals(lastNumericPos, -1))
		{
			throw new InvalidParameterException("Unable to find the unit definition within " + valueExpr);
		}
		return lastNumericPos;
	}

	private UnitModel setUnit2use(final String unitCode, final ProductModel product, final String valueExpr)
	{
		final UnitModel unit2use = getUnitService().getUnitForCode(unitCode);
		if (ObjectUtils.isEmpty(unit2use))
		{
			if (ObjectUtils.isEmpty(product.getUnit()))
			{
				throw new InvalidParameterException("missing unit within " + valueExpr);
			}
			return product.getUnit();
		}
		return unit2use;
	}

	private double setPriceValue(final String valueExpr, final int equalsPos, final int startOfCurrencyIndex)
	{
		final String priceDef = StringUtils.trim(StringUtils.substring(valueExpr, equalsPos, startOfCurrencyIndex));
		try
		{
			final NumberFormat numberformat = getNumberFormat();
			synchronized (numberformat)
			{
				return numberformat.parse(priceDef).doubleValue();
			}
		}
		catch (final ParseException e)
		{
			throw new SystemException(e);
		}
	}

	private UserModel setPriceGroupsAndReturnUser(final String valueExpr, final int firstNumericPos,
			ProductPriceGroup productPriceGroup,
			UserPriceGroup userPriceGroup)
	{
		UserModel user = null;
		if (firstNumericPos > COUNT)
		{
			final String userOrUsergroupExpr = StringUtils.trim(StringUtils.substring(valueExpr, 0, firstNumericPos));
			user = getUserService().getUserForUID(userOrUsergroupExpr);
			if (ObjectUtils.isEmpty(user))
			{
				if (priceProductGroupsMap.containsKey(userOrUsergroupExpr.toLowerCase()))
				{
					productPriceGroup = priceProductGroupsMap.get(userOrUsergroupExpr.toLowerCase());
				}
				else if (priceUserGroupsMap.containsKey(userOrUsergroupExpr.toLowerCase()))
				{
					userPriceGroup = priceUserGroupsMap.get(userOrUsergroupExpr.toLowerCase());
				}
				if (ObjectUtils.isEmpty(productPriceGroup) && ObjectUtils.isEmpty(userPriceGroup))
				{
					throw new SystemException("Missing user|group definition within " + valueExpr);
				}
			}
		}
		return user;
	}

	private int getFirstNumericPos(final String valueExpr, final int lastNumericPos)
	{
		int firstNumericPos = -1;
		final int firstSpacePos = StringUtils.indexOf(valueExpr, ' ');
		int firstSearchPos = 0;
		firstSearchPos = (!ObjectUtils.nullSafeEquals(firstSpacePos, -1) && (firstSpacePos < lastNumericPos)) ? firstSpacePos
				: firstSearchPos;
		for (int pos = firstSearchPos; pos < lastNumericPos; pos++)
		{
			final char digit = valueExpr.charAt(pos);
			if (Character.isDigit(digit))
			{
				firstNumericPos = pos;
				break;
			}
		}
		return ObjectUtils.nullSafeEquals(firstNumericPos, -1) ? lastNumericPos : firstNumericPos;
	}

	private boolean checkForUserAndGroups(final PriceRowModel priceRow, final Map<String, Object> priceValuesMap)
	{
		return ObjectUtils.nullSafeEquals(priceRow.getPg(), priceValuesMap.get(PRODUCTPRICEGROUP))
				&& ObjectUtils.nullSafeEquals(priceRow.getUser(), priceValuesMap.get(USER))
				&& ObjectUtils.nullSafeEquals(priceRow.getUg(), priceValuesMap.get(USERPRICEGROUP));
	}

	private boolean checkForCurrencyAndUnitAndQuantity(final PriceRowModel priceRow, final Map<String, Object> priceValuesMap)
	{
		return checkForCurrencyAndUnitAndMinqtd(priceRow, priceValuesMap)
				&& checkForUnitFactorAndNetAndPrice(priceRow, priceValuesMap);
	}

	private boolean checkForCurrencyAndUnitAndMinqtd(final PriceRowModel priceRow, final Map<String, Object> priceValuesMap)
	{
		return ObjectUtils.nullSafeEquals(priceRow.getCurrency(), priceValuesMap.get(CURRENCY2USE))
				&& ObjectUtils.nullSafeEquals(priceRow.getUnit(), priceValuesMap.get(UNIT2USE))
				&& ObjectUtils.nullSafeEquals(priceRow.getMinqtd(), priceValuesMap.get(QUANTITY));

	}

	private boolean checkForUnitFactorAndNetAndPrice(final PriceRowModel priceRow, final Map<String, Object> priceValuesMap)
	{
		return ObjectUtils.nullSafeEquals(priceRow.getUnitFactor(), 1) &&
				ObjectUtils.nullSafeEquals(priceRow.getNet(), priceValuesMap.get(NET))
				&& ObjectUtils.nullSafeEquals(priceRow.getPrice(), priceValuesMap.get(PRICE));
	}

	private boolean checkForDateRangeAndChannel(final PriceRowModel priceRow, final Map<String, Object> priceValuesMap)
	{
		return (ObjectUtils.nullSafeEquals(priceRow.getDateRange(), priceValuesMap.get(DATERANGE))
				&& ObjectUtils.nullSafeEquals(priceRow.getChannel(), priceValuesMap.get(CHANNEL)));
	}

	private DateRange parseDateRange(final String valueExpr)
	{
		DateRange dateRange = null;
		final int startPosition = StringUtils.indexOf(valueExpr, 91);
		final int endPosition = StringUtils.indexOf(valueExpr, 93);
		boolean dateRangeIsWelldefined = true;
		if ((!ObjectUtils.nullSafeEquals(startPosition, -1) || !ObjectUtils.nullSafeEquals(endPosition, -1))
				&& endPosition > startPosition)
		{
			dateRangeIsWelldefined = false;
			final String dateRangeExpr = StringUtils.trim(StringUtils.substring(valueExpr, startPosition + 1, endPosition));
			final int sepPos = StringUtils.indexOf(dateRangeExpr, 44);
			if (!ObjectUtils.nullSafeEquals(sepPos, -1))
			{
				try
				{
					final String start = StringUtils.trim(StringUtils.substring(dateRangeExpr, 0, sepPos));
					final String end = StringUtils.trim(StringUtils.substring(dateRangeExpr, sepPos + 1, dateRangeExpr.length()));
					final Date startDate = transformStartDate(dateFormat.parse(start));
					final Date endDate = transformEndDate(dateFormat.parse(end));
					dateRange = new StandardDateRange(startDate, endDate);
					dateRangeIsWelldefined = true;
				}
				catch (final ParseException e)
				{
					LOG.error(e);
				}
			}
		}
		if (!dateRangeIsWelldefined)
		{
			throw new ModelNotFoundException("Invalid daterange definition!");
		}
		else
		{
			return dateRange;
		}
	}

	protected List splitAndUnescape(final String valueExpr)
	{
		final List<String> tokens = CSVUtils.splitAndUnescape(valueExpr, new char[]
				{ collectionValueDelimiter }, false);
		if (!CollectionUtils.isEmpty(tokens) && tokens.size() >= COUNT)
		{
			final List tokensList = new ArrayList(tokens.size());
			tokensList.add(tokens.get(0));

			for (int i = 1; i < tokens.size(); ++i)
			{
				final String prev = tokens.get(i - 1);
				final String current = tokens.get(i);
				if (Character.isDigit(prev.charAt(StringUtils.length(prev) - 1)))
				{
					final String lastToken = (String) tokensList.get(tokensList.size() - 1);
					tokensList.set(tokensList.size() - 1, lastToken + collectionValueDelimiter + current);
				}
				else
				{
					tokensList.add(current);
				}
			}
			return tokensList;
		}
		else
		{
			return tokens;
		}
	}

	private Map<String, Object> parseCurrency(final String valueExpr)
	{
		int startOfCurrencyIndex = -1;
		CurrencyModel currency2use = null;
		final Map<String, Object> parsedCurrencyMap = new HashMap<>();
		Iterator it = currenciesISOs.keySet().iterator();
		String symbol;
		while (it.hasNext() && ObjectUtils.isEmpty(currency2use))
		{
			symbol = StringUtils.lowerCase((String) it.next());
			startOfCurrencyIndex = StringUtils.indexOf(StringUtils.lowerCase(valueExpr), symbol);
			currency2use = !ObjectUtils.nullSafeEquals(startOfCurrencyIndex, -1) ? currenciesISOs.get(symbol) : currency2use;
		}
		it = currenciesSymbols.keySet().iterator();
		while (it.hasNext() && ObjectUtils.isEmpty(currency2use))
		{
			symbol = StringUtils.lowerCase((String) it.next());
			startOfCurrencyIndex = StringUtils.indexOf(StringUtils.lowerCase(valueExpr), symbol);
			currency2use = !ObjectUtils.nullSafeEquals(startOfCurrencyIndex, -1) ? currenciesSymbols.get(symbol) : currency2use;
		}
		parsedCurrencyMap.put(START_OF_CURRENCY_INDEX, startOfCurrencyIndex);
		parsedCurrencyMap.put(CURRENCY2USE, currency2use);
		return parsedCurrencyMap;
	}

	private PriceRowChannel parseChannel(final String valueExpr)
	{
		int startOfChannelDef = -1;
		PriceRowChannel channel2use = null;
		final Set<String> channels = priceRowChannelsMap.keySet();
		for (final String channel : channels)
		{
			final String valueExprTrim = StringUtils.trim(valueExpr);
			startOfChannelDef = StringUtils.lastIndexOf(StringUtils.lowerCase(valueExprTrim), channel);
			if (!ObjectUtils.nullSafeEquals(startOfChannelDef, -1))
			{
				final boolean isLastElement = ObjectUtils.nullSafeEquals(startOfChannelDef + StringUtils.length(channel),
						StringUtils.length(valueExprTrim));
				if (isLastElement)
				{
					channel2use = priceRowChannelsMap.get(channel);
					break;
				}
			}
		}
		return channel2use;
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

	protected Date transformStartDate(final Date startDate)
	{
		return startDate;
	}

	protected Date transformEndDate(final Date endDate)
	{
		final Calendar cal = Calendar.getInstance(locale);
		cal.setTime(endDate);
		if (ObjectUtils.nullSafeEquals(cal.get(Calendar.HOUR_OF_DAY), 0) && ObjectUtils.nullSafeEquals(cal.get(Calendar.MINUTE), 0)
				&& ObjectUtils.nullSafeEquals(cal.get(Calendar.SECOND), 0))
		{
			cal.set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_DAY);
			cal.set(Calendar.MINUTE, LAST_MINUTE_OF_DAY);
			cal.set(Calendar.SECOND, LAST_SECOND_OF_DAY);
		}
		return cal.getTime();
	}

	private void findCurrencies()
	{
		final Collection cur = getCurrencyDao().findCurrencies();
		final Iterator it = cur.iterator();
		while (it.hasNext())
		{
			final CurrencyModel currency = (CurrencyModel) it.next();
			currenciesISOs.put(StringUtils.lowerCase(currency.getIsocode()), currency);
			if (StringUtils.isNotBlank(currency.getSymbol()))
			{
				currenciesSymbols.put(StringUtils.lowerCase(currency.getSymbol()), currency);
			}
		}
	}

	private void setupDelimiter(final SpecialColumnDescriptor columnDescriptor)
	{
		final String customDelimiter = columnDescriptor.getDescriptorData().getModifier(COLLECTION_DELIMITER);
		collectionValueDelimiter = !StringUtils.isEmpty(customDelimiter) ? customDelimiter.charAt(0) : COMMA_SYMBOL;
	}

	private void setupPriceUserGroupsMap()
	{
		final List<UserPriceGroup> priceUserGroupsList = getEnumerationService().getEnumerationValues(UserPriceGroup.class);
		for (final UserPriceGroup priceUserGroup : priceUserGroupsList)
		{
			priceUserGroupsMap.put(StringUtils.lowerCase(priceUserGroup.getCode()), priceUserGroup);
		}
	}

	private void setupPriceProductGroupsMap()
	{
		final List<ProductPriceGroup> priceProductGroupsList = getEnumerationService()
				.getEnumerationValues(ProductPriceGroup.class);
		for (final ProductPriceGroup priceProductGroup : priceProductGroupsList)
		{
			priceProductGroupsMap.put(StringUtils.lowerCase(priceProductGroup.getCode()), priceProductGroup);
		}
	}

	private void setupPriceRowChannelsMap()
	{
		final List<PriceRowChannel> priceRowChannelsList = getEnumerationService().getEnumerationValues(PriceRowChannel.class);
		for (final PriceRowChannel priceRowChannel : priceRowChannelsList)
		{
			priceRowChannelsMap.put(StringUtils.lowerCase(priceRowChannel.getCode()), priceRowChannel);
		}
	}

	private void setupDateAndNumberFormat(final SpecialColumnDescriptor columnDescriptor)
	{
		locale = columnDescriptor.getHeader().getReader().getLocale();
		dateFormat = StringUtils.isNotBlank(this.getDateFormatString(columnDescriptor))
				? new SimpleDateFormat(this.getDateFormatString(columnDescriptor), locale)
				: (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(DATE_STYLE, TIME_STYLE, locale);
		numberFormat = StringUtils.isNotBlank(this.getNumberFormatString(columnDescriptor))
				? new DecimalFormat(this.getNumberFormatString(columnDescriptor), new DecimalFormatSymbols(locale))
				: NumberFormat.getInstance(locale);
	}

	private void setupModes(final SpecialColumnDescriptor columnDescriptor)
	{
		final String mode = columnDescriptor.getDescriptorData().getModifier(MODE);
		if (StringUtils.equalsIgnoreCase(StringUtils.trim(mode), APPEND_MODE))
		{
			globalAdd = true;
		}
		else if (StringUtils.equalsIgnoreCase(StringUtils.trim(mode), REMOVE_MODE))
		{
			globalRemove = true;
		}
	}

	protected NumberFormat getNumberFormat()
	{
		return this.numberFormat;
	}

	protected void clearStatus()
	{
		this.lastWasUnresolved = false;
	}

	protected void setError()
	{
		this.lastWasUnresolved = true;
	}

	public boolean wasUnresolved()
	{
		return this.lastWasUnresolved;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected UnitService getUnitService()
	{
		return unitService;
	}

	@Required
	public void setUnitService(final UnitService unitService)
	{
		this.unitService = unitService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected CurrencyDao getCurrencyDao()
	{
		return currencyDao;
	}

	@Required
	public void setCurrencyDao(final CurrencyDao currencyDao)
	{
		this.currencyDao = currencyDao;
	}
}

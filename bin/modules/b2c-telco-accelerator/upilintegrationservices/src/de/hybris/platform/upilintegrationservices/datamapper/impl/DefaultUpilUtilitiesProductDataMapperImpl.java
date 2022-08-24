/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.datamapper.impl;

import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdBillingAttributeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdDiscountType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdSalesAttributeType;
import de.hybris.platform.upilintegrationservices.datamapper.UpilUtilitiesProductDataMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * DefaultTmaUtilitiesProductDataMapperImpl is a part of extension upilintegrationservices,
 * It is used for creating MAP Object of utilities product.
 *
 * @since 1911
 */
public class DefaultUpilUtilitiesProductDataMapperImpl implements UpilUtilitiesProductDataMapper
{

	@Override
	public Map<String, Object> getUtilitiesProductDataMap(final C_UtilitiesProductType utilsProductData)
	{
		final Map<String, Object> utilsProductDataMap = new HashMap<>();
		if (!StringUtils.isBlank(utilsProductData.getUtilitiesProduct()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT, utilsProductData.getUtilitiesProduct());
		}
		if (!StringUtils.isBlank(utilsProductData.getUtilitiesReferenceProduct()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.UTILS_REFERENCE_PRODUCT,
					utilsProductData.getUtilitiesReferenceProduct());
		}
		if (!ObjectUtils.isEmpty(utilsProductData.getValidityStartDate()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.VALIDATY_START_DATE,
					utilsProductData.getValidityStartDate());
		}
		if (!ObjectUtils.isEmpty(utilsProductData.getValidityEndDate()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.VALIDATY_END_DATE,
					utilsProductData.getValidityEndDate());
		}
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_Discount()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_DISCOUNT,
					getUtilsProductDiscountsMapList(utilsProductData.getTo_Discount()));
		}
		getUtilsProductCharges(utilsProductData, utilsProductDataMap);
		getUtilsAdditionalAttributes(utilsProductData, utilsProductDataMap);
		return utilsProductDataMap;
	}

	private void getUtilsProductCharges(final C_UtilitiesProductType utilsProductData,
			final Map<String, Object> utilsProductDataMap)
	{
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_OneTimeCharge()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_ONE_TIME_CHAGRE,
					getUtilsOneTimeChargesMapList(utilsProductData.getTo_OneTimeCharge()));
		}
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_RecurringCharge()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_RECURRING_CHARGE,
					getUtilsRecurringChargesMapList(utilsProductData.getTo_RecurringCharge()));
		}
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_UsageCharge()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_USAGE_CHARGE,
					getUtilsUsageChargesMapList(utilsProductData.getTo_UsageCharge()));
		}
	}

	private void getUtilsAdditionalAttributes(final C_UtilitiesProductType utilsProductData,
			final Map<String, Object> utilsProductDataMap)
	{
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_BillingAttribute()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_BILLING_ATTRIBUTE,
					getUtilsBillingAttributeMapList(utilsProductData.getTo_BillingAttribute()));
		}
		if (!CollectionUtils.isEmpty(utilsProductData.getTo_SalesAttribute()))
		{
			utilsProductDataMap.put(UpilintegrationservicesConstants.TO_UTILS_SALES_ATTRIBUTE,
					getUtilsSalesAttributeMapList(utilsProductData.getTo_SalesAttribute()));
		}
	}

	private List<Map<String, Object>> getUtilsProductDiscountsMapList(
			final List<I_UtilsProdDiscountType> utilsProductDiscountsDataList)
	{
		final List<Map<String, Object>> utilsProductDiscountsMapList = new ArrayList<>();
		for (final I_UtilsProdDiscountType utilsProductDiscountsData : utilsProductDiscountsDataList)
		{
			utilsProductDiscountsMapList.add(getUtilsProductDiscountsMap(utilsProductDiscountsData));
		}
		return utilsProductDiscountsMapList;
	}

	private Map<String, Object> getUtilsProductDiscountsMap(final I_UtilsProdDiscountType utilsProductDiscountsData)
	{
		final Map<String, Object> utilsProductDiscountsMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilitiesProduct()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsProductDiscountsData.getUtilitiesProduct());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountText()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_TEXT,
					utilsProductDiscountsData.getUtilsDiscountText());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsMeasurementUnit()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_MEASUREMENT_UNIT,
					utilsProductDiscountsData.getUtilsMeasurementUnit());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountAmount()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_AMOUNT,
					utilsProductDiscountsData.getUtilsDiscountAmount());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountInPercent()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_IN_PERCENT,
					utilsProductDiscountsData.getUtilsDiscountInPercent());
		}
		if (!StringUtils.isBlank(utilsProductDiscountsData.getUtilsDiscountCurrency()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_CURRENCY,
					utilsProductDiscountsData.getUtilsDiscountCurrency());
		}
		if (!StringUtils.isBlank(utilsProductDiscountsData.getUtilsDiscountFactor()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_FACTOR,
					utilsProductDiscountsData.getUtilsDiscountFactor());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountQuantity()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_QUANTITY,
					utilsProductDiscountsData.getUtilsDiscountQuantity());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountDemand()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_DEMAND,
					utilsProductDiscountsData.getUtilsDiscountDemand());
		}
		if (!ObjectUtils.isEmpty(utilsProductDiscountsData.getUtilsDiscountType()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_DISCOUNT_TYPE,
					utilsProductDiscountsData.getUtilsDiscountType());
		}
		getUtilsDiscountSemantics(utilsProductDiscountsData, utilsProductDiscountsMap);
		utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsProductDiscountsMap;
	}

	private void getUtilsDiscountSemantics(final I_UtilsProdDiscountType utilsProductDiscountsData,
			final Map<String, Object> utilsProductDiscountsMap)
	{
		if (!StringUtils.isBlank(utilsProductDiscountsData.getUtilsSemanticsName1()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsProductDiscountsData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsProductDiscountsData.getUtilsSemanticsName2()))
		{
			utilsProductDiscountsMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsProductDiscountsData.getUtilsSemanticsName2());
		}
	}

	private List<Map<String, Object>> getUtilsOneTimeChargesMapList(
			final List<I_UtilsProdChgOneTimeType> utilsOneTimeChargesDataList)
	{
		final List<Map<String, Object>> utilsOneTimeChargesMapList = new ArrayList<>();
		for (final I_UtilsProdChgOneTimeType utilsOneTimeChargesData : utilsOneTimeChargesDataList)
		{
			utilsOneTimeChargesMapList.add(getutilsOneTimeChargesMap(utilsOneTimeChargesData));
		}
		return utilsOneTimeChargesMapList;
	}

	private Map<String, Object> getutilsOneTimeChargesMap(final I_UtilsProdChgOneTimeType utilsOneTimeChargesData)
	{
		final Map<String, Object> utilsOneTimeChargesMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsOneTimeChargesData.getUtilitiesProduct()))
		{
			utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsOneTimeChargesData.getUtilitiesProduct());
		}
		if (!ObjectUtils.isEmpty(utilsOneTimeChargesData.getUtilsPriceAmount()))
		{
			utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_AMOUNT,
					utilsOneTimeChargesData.getUtilsPriceAmount());
		}

		if (!StringUtils.isBlank(utilsOneTimeChargesData.getUtilsPriceCurrency()))
		{
			utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_CURRENCY,
					utilsOneTimeChargesData.getUtilsPriceCurrency());
		}
		if (!StringUtils.isBlank(utilsOneTimeChargesData.getUtilsSemanticsName1()))
		{
			utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsOneTimeChargesData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsOneTimeChargesData.getUtilsSemanticsName2()))
		{
			utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsOneTimeChargesData.getUtilsSemanticsName2());
		}
		utilsOneTimeChargesMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsOneTimeChargesMap;
	}

	private List<Map<String, Object>> getUtilsBillingAttributeMapList(
			final List<I_UtilsProdBillingAttributeType> utilsBillingAttributeDatas)
	{
		final List<Map<String, Object>> utilsBillingAttributeList = new ArrayList<>();

		utilsBillingAttributeDatas
				.forEach(utilsBillingAttributeData -> utilsBillingAttributeList
						.add(getUtilsBillingAttributesMap(utilsBillingAttributeData)));
		return utilsBillingAttributeList;
	}

	private Map<String, Object> getUtilsBillingAttributesMap(final I_UtilsProdBillingAttributeType utilsBillingAttributeData)
	{
		final Map<String, Object> utilsBillingAttributeMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsBillingAttributeData.getUtilitiesProduct()))
		{
			utilsBillingAttributeMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsBillingAttributeData.getUtilitiesProduct());
		}
		if (!ObjectUtils.isEmpty(utilsBillingAttributeData.getUtilsProductEntryValue()))
		{
			utilsBillingAttributeMap.put(UpilintegrationservicesConstants.UTILS_PRODUCT_ENTRY_VALUE,
					utilsBillingAttributeData.getUtilsProductEntryValue());
		}

		if (!StringUtils.isBlank(utilsBillingAttributeData.getUtilsSemanticsName1()))
		{
			utilsBillingAttributeMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsBillingAttributeData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsBillingAttributeData.getUtilsSemanticsName2()))
		{
			utilsBillingAttributeMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsBillingAttributeData.getUtilsSemanticsName2());
		}
		utilsBillingAttributeMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsBillingAttributeMap;
	}

	private List<Map<String, Object>> getUtilsSalesAttributeMapList(
			final List<I_UtilsProdSalesAttributeType> utilsSalesAttributeDatas)
	{
		final List<Map<String, Object>> utilsSalesAttributeList = new ArrayList<>();

		utilsSalesAttributeDatas
				.forEach(utilsSalesAttributeData -> utilsSalesAttributeList
						.add(getUtilsSalesAttributesMap(utilsSalesAttributeData)));
		return utilsSalesAttributeList;
	}

	private Map<String, Object> getUtilsSalesAttributesMap(final I_UtilsProdSalesAttributeType utilsSalesAttributeData)
	{
		final Map<String, Object> utilsSalesAttributesMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsSalesAttributeData.getCurrency()))
		{
			utilsSalesAttributesMap.put(UpilintegrationservicesConstants.CURRENCY,
					utilsSalesAttributeData.getCurrency());
		}
		if (!ObjectUtils.isEmpty(utilsSalesAttributeData.getUtilitiesProduct()))
		{
			utilsSalesAttributesMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsSalesAttributeData.getUtilitiesProduct());
		}
		if (!ObjectUtils.isEmpty(utilsSalesAttributeData.getUtilsSalesFactValue()))
		{
			utilsSalesAttributesMap.put(UpilintegrationservicesConstants.UTILS_SALES_FACT_VALUE,
					utilsSalesAttributeData.getUtilsSalesFactValue());
		}

		if (!StringUtils.isBlank(utilsSalesAttributeData.getUtilsSemanticsName1()))
		{
			utilsSalesAttributesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsSalesAttributeData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsSalesAttributeData.getUtilsSemanticsName2()))
		{
			utilsSalesAttributesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsSalesAttributeData.getUtilsSemanticsName2());
		}
		utilsSalesAttributesMap.put(UpilintegrationservicesConstants.VALUE_POSITION_NUMBER, StringUtils.EMPTY);
		utilsSalesAttributesMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsSalesAttributesMap;
	}

	private List<Map<String, Object>> getUtilsRecurringChargesMapList(
			final List<I_UtilsProdChgRecurringType> utilsRecurringChargesDataList)
	{
		final List<Map<String, Object>> utilsRecurringChargesMapList = new ArrayList<>();
		for (final I_UtilsProdChgRecurringType utilsRecurringChargesData : utilsRecurringChargesDataList)
		{
			utilsRecurringChargesMapList.add(getUtilsRecurringChargesMap(utilsRecurringChargesData));
		}
		return utilsRecurringChargesMapList;
	}

	private Map<String, Object> getUtilsRecurringChargesMap(final I_UtilsProdChgRecurringType utilsRecurringChargesData)
	{
		final Map<String, Object> utilsRecurringChargesMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsRecurringChargesData.getUtilitiesProduct()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsRecurringChargesData.getUtilitiesProduct());
		}
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsPriceName()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_NAME,
					utilsRecurringChargesData.getUtilsPriceName());
		}
		if (!StringUtils.isEmpty(utilsRecurringChargesData.getUtilsPriceID()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_ID,
					utilsRecurringChargesData.getUtilsPriceID());
		}
		if (!ObjectUtils.isEmpty(utilsRecurringChargesData.getUtilsPriceAmount()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_AMOUNT,
					utilsRecurringChargesData.getUtilsPriceAmount());
		}
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsPriceCurrency()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_CURRENCY,
					utilsRecurringChargesData.getUtilsPriceCurrency());
		}
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsPriceFrequencyValue()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_FREQUENCY_VALUE,
					utilsRecurringChargesData.getUtilsPriceFrequencyValue());
		}
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsPriceFrequencyUnit()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_PRICE_FREQUENCY_UNIT,
					utilsRecurringChargesData.getUtilsPriceFrequencyUnit());
		}
		getUtilsRecurringChargeSemantics(utilsRecurringChargesData, utilsRecurringChargesMap);
		utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsRecurringChargesMap;
	}

	private void getUtilsRecurringChargeSemantics(final I_UtilsProdChgRecurringType utilsRecurringChargesData,
			final Map<String, Object> utilsRecurringChargesMap)
	{
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsSemanticsName1()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsRecurringChargesData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsRecurringChargesData.getUtilsSemanticsName2()))
		{
			utilsRecurringChargesMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsRecurringChargesData.getUtilsSemanticsName2());
		}
	}

	private List<Map<String, Object>> getUtilsUsageChargesMapList(final List<I_UtilsProdChgUsageType> utilsUsageChargesDataList)
	{
		final List<Map<String, Object>> utilsUsageChargesMapList = new ArrayList<>();
		for (final I_UtilsProdChgUsageType utilsUsageChargesData : utilsUsageChargesDataList)
		{
			utilsUsageChargesMapList.add(getUtilsUsageChargesMap(utilsUsageChargesData));
		}
		return utilsUsageChargesMapList;
	}

	private Map<String, Object> getUtilsUsageChargesMap(final I_UtilsProdChgUsageType utilsUsageChargesData)
	{
		final Map<String, Object> utilsUsageChargesDataMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(utilsUsageChargesData.getUtilitiesProduct()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILITIES_PRODUCT,
					utilsUsageChargesData.getUtilitiesProduct());
		}
		if (!StringUtils.isEmpty(utilsUsageChargesData.getUtilsPriceName()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_NAME,
					utilsUsageChargesData.getUtilsPriceName());
		}
		if (!StringUtils.isEmpty(utilsUsageChargesData.getUtilsPriceID()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_ID, utilsUsageChargesData.getUtilsPriceID());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsPriceFromBlock()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_FROM_BLOCK,
					utilsUsageChargesData.getUtilsPriceFromBlock());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsPriceToBlock()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_TO_BLOCK,
					utilsUsageChargesData.getUtilsPriceToBlock());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsPriceTierType()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_TIER_TYPE,
					utilsUsageChargesData.getUtilsPriceTierType());
		}
		if (!ObjectUtils.isEmpty(utilsUsageChargesData.getUtilsPriceAmount()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_AMOUNT,
					utilsUsageChargesData.getUtilsPriceAmount());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsPriceCurrency()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_PRICE_CURRENCY,
					utilsUsageChargesData.getUtilsPriceCurrency());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsMeasurementUnit()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_MEASUREMENT_UNIT,
					utilsUsageChargesData.getUtilsMeasurementUnit());
		}
		getUtilsUsageChargeSemantics(utilsUsageChargesData, utilsUsageChargesDataMap);
		utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_CHARACTERISTIC, StringUtils.EMPTY);
		return utilsUsageChargesDataMap;
	}

	private void getUtilsUsageChargeSemantics(final I_UtilsProdChgUsageType utilsUsageChargesData,
			final Map<String, Object> utilsUsageChargesDataMap)
	{
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsSemanticsName1()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME1,
					utilsUsageChargesData.getUtilsSemanticsName1());
		}
		if (!StringUtils.isBlank(utilsUsageChargesData.getUtilsSemanticsName2()))
		{
			utilsUsageChargesDataMap.put(UpilintegrationservicesConstants.UTILS_SEMANTICS_NAME2,
					utilsUsageChargesData.getUtilsSemanticsName2());
		}
	}
}

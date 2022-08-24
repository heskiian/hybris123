/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.datamapper.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdBillingAttributeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdDiscountType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdSalesAttributeType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ObjectUtils;


/**
 * Test class for {@link DefaultUpilUtilitiesProductDataMapperImpl}
 * 
 * @since 1911
 */

@UnitTest
public class DefaultUpilUtilitiesProductDataMapperImplUnitTest
{
	private static final BigDecimal PRICE = BigDecimal.TEN;
	private static final String PRODUCT_ID = "testProduct";
	private static final String PRODUCT_TYPE = "ELEC_RESI";
	private static final String CURRENCY = "USD";
	private static final String PRODUCT_ATTRIBUTE_SEMANTICNAME1 = "DURATION";
	private static final String PRODUCT_ATTRIBUTE_SEMANTICNAME2 = "PERIOD";
	private static final String ONE = "1";
	private static final String ONETIME_CHARGE_SEMANTICNAME1 = "ACTIVATION";
	private static final String ONETIME_CHARGE_SEMANTICNAME2 = "FEE";
	private static final String RECURRING_CHARGE_SEMANTICNAME1 = "MONTHLY";
	private static final String RECURRING_CHARGE_SEMANTICNAME2 = "FIXED";
	private static final String USAGE_CHARGE_SEMANTICNAME1 = "ENERGY";
	private static final String USAGE_CHARGE_SEMANTICNAME2 = "USAGE";
	private static final String DISCOUNT_SEMANTICNAME1 = "DIRECT-DEBIT";
	private static final String DISCOUNT_SEMANTICNAME2 = "DISCOUNT";
	private static final String DISCOUNT_TEXT = "Absolute Discount";
	private static final String PRICE_NAME = "Standing Charge";


	private DefaultUpilUtilitiesProductDataMapperImpl dataMapper;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		dataMapper = new DefaultUpilUtilitiesProductDataMapperImpl();
	}

	@Test
	public void testEmptyProductData()
	{
		final Map<String, Object> utilsProductDataMap = dataMapper.getUtilitiesProductDataMap(new C_UtilitiesProductType());
		Assert.assertTrue(ObjectUtils.isEmpty(utilsProductDataMap));
	}

	@Test
	public void testGetUtilitiesProductDataMap()
	{
		final C_UtilitiesProductType productData = setUpUtilsProductData();
		final Map<String, Object> utilsProductDataMap = dataMapper.getUtilitiesProductDataMap(productData);
		Assert.assertFalse(ObjectUtils.isEmpty(utilsProductDataMap));
		Assert.assertEquals(productData.getUtilitiesProduct(),
				utilsProductDataMap.get(UpilintegrationservicesConstants.UTILITIES_PRODUCT));
		Assert.assertEquals(productData.getUtilitiesReferenceProduct(),
				utilsProductDataMap.get(UpilintegrationservicesConstants.UTILS_REFERENCE_PRODUCT));
		Assert.assertTrue(!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_ONE_TIME_CHAGRE)));
		Assert.assertTrue(
				!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_RECURRING_CHARGE)));
		Assert.assertTrue(!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_USAGE_CHARGE)));
		Assert.assertTrue(!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_DISCOUNT)));
		Assert.assertTrue(!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_SALES_ATTRIBUTE)));
		Assert.assertTrue(
				!ObjectUtils.isEmpty(utilsProductDataMap.get(UpilintegrationservicesConstants.TO_UTILS_BILLING_ATTRIBUTE)));
	}

	@Test
	public void testProductDataWithEmptyCharges()
	{
		final C_UtilitiesProductType productData = setUpUtilsProductDataEmptyCharges();
		final Map<String, Object> utilsProductDataMap = dataMapper.getUtilitiesProductDataMap(productData);
		Assert.assertFalse(ObjectUtils.isEmpty(utilsProductDataMap));
	}

	private C_UtilitiesProductType setUpUtilsProductData()
	{
		final C_UtilitiesProductType productData = mock(C_UtilitiesProductType.class);

		given(productData.getUtilitiesProduct()).willReturn(PRODUCT_ID);
		given(productData.getValidityStartDate()).willReturn(new Date());
		given(productData.getValidityEndDate()).willReturn(new Date());
		given(productData.getUtilitiesReferenceProduct()).willReturn(PRODUCT_TYPE);

		final List<I_UtilsProdChgOneTimeType> oneTimeCharges = setUpOneTimeChargeDatas();
		given(productData.getTo_OneTimeCharge()).willReturn(oneTimeCharges);

		final List<I_UtilsProdSalesAttributeType> salesAttributeDatas = setUpSalesAttributeDatas();
		given(productData.getTo_SalesAttribute()).willReturn(salesAttributeDatas);

		final List<I_UtilsProdBillingAttributeType> billingAttributeDatas = setUpBillingAttributeDatas();
		given(productData.getTo_BillingAttribute()).willReturn(billingAttributeDatas);

		final List<I_UtilsProdDiscountType> discountDatas = setUpProductDiscountDatas();
		given(productData.getTo_Discount()).willReturn(discountDatas);

		final List<I_UtilsProdChgRecurringType> recurringChargeDatas = setUpRecurringChargeDatas();
		given(productData.getTo_RecurringCharge()).willReturn(recurringChargeDatas);

		final List<I_UtilsProdChgUsageType> usageChargeDatas = setUpUsageChargeDatas();
		given(productData.getTo_UsageCharge()).willReturn(usageChargeDatas);

		return productData;
	}

	private C_UtilitiesProductType setUpUtilsProductDataEmptyCharges()
	{
		final C_UtilitiesProductType productData = mock(C_UtilitiesProductType.class);

		given(productData.getUtilitiesProduct()).willReturn(PRODUCT_ID);
		given(productData.getUtilitiesReferenceProduct()).willReturn(PRODUCT_TYPE);
		given(productData.getValidityStartDate()).willReturn(new Date());
		given(productData.getValidityEndDate()).willReturn(new Date());

		final I_UtilsProdChgOneTimeType oneTimeCharge = mock(I_UtilsProdChgOneTimeType.class);
		final List<I_UtilsProdChgOneTimeType> oneTimeCharges = new ArrayList<I_UtilsProdChgOneTimeType>();
		oneTimeCharges.add(oneTimeCharge);
		given(productData.getTo_OneTimeCharge()).willReturn(oneTimeCharges);

		final I_UtilsProdBillingAttributeType billingAttributeData = mock(I_UtilsProdBillingAttributeType.class);
		final List<I_UtilsProdBillingAttributeType> billingAttributeDatas = new ArrayList<>();
		billingAttributeDatas.add(billingAttributeData);
		given(productData.getTo_BillingAttribute()).willReturn(billingAttributeDatas);

		final I_UtilsProdBillingAttributeType salesAttributeData = mock(I_UtilsProdBillingAttributeType.class);
		final List<I_UtilsProdBillingAttributeType> salesAttributeDatas = new ArrayList<>();
		salesAttributeDatas.add(salesAttributeData);
		given(productData.getTo_BillingAttribute()).willReturn(salesAttributeDatas);

		final I_UtilsProdDiscountType discountsData = mock(I_UtilsProdDiscountType.class);
		final List<I_UtilsProdDiscountType> discountsDatas = new ArrayList<>();
		discountsDatas.add(discountsData);
		given(productData.getTo_Discount()).willReturn(discountsDatas);

		final I_UtilsProdChgRecurringType recurringCharge = mock(I_UtilsProdChgRecurringType.class);
		final List<I_UtilsProdChgRecurringType> recurringCharges = new ArrayList<>();
		recurringCharges.add(recurringCharge);
		given(productData.getTo_RecurringCharge()).willReturn(recurringCharges);

		final I_UtilsProdChgUsageType usageCharge = mock(I_UtilsProdChgUsageType.class);
		final List<I_UtilsProdChgUsageType> usageCharges = new ArrayList<>();
		usageCharges.add(usageCharge);
		given(productData.getTo_UsageCharge()).willReturn(usageCharges);

		return productData;
	}

	private List<I_UtilsProdChgUsageType> setUpUsageChargeDatas()
	{
		final I_UtilsProdChgUsageType usageCharge = mock(I_UtilsProdChgUsageType.class);

		given(usageCharge.getUtilitiesProduct()).willReturn(PRODUCT_ID);
		given(usageCharge.getUtilsPriceAmount()).willReturn(PRICE);
		given(usageCharge.getUtilsPriceCurrency()).willReturn(CURRENCY);
		given(usageCharge.getUtilsPriceFromBlock()).willReturn(PRODUCT_TYPE);
		given(usageCharge.getUtilsPriceID()).willReturn(PRODUCT_TYPE);
		given(usageCharge.getUtilsPriceName()).willReturn(PRODUCT_TYPE);
		given(usageCharge.getUtilsPriceTierType()).willReturn(PRODUCT_TYPE);
		given(usageCharge.getUtilsPriceToBlock()).willReturn(PRODUCT_TYPE);
		given(usageCharge.getUtilsSemanticsName1()).willReturn(USAGE_CHARGE_SEMANTICNAME1);
		given(usageCharge.getUtilsSemanticsName2()).willReturn(USAGE_CHARGE_SEMANTICNAME2);

		final List<I_UtilsProdChgUsageType> usageCharges = new ArrayList<>();
		usageCharges.add(usageCharge);

		return usageCharges;
	}

	private List<I_UtilsProdChgOneTimeType> setUpOneTimeChargeDatas()
	{
		final I_UtilsProdChgOneTimeType oneTimeCharge = mock(I_UtilsProdChgOneTimeType.class);

		when(oneTimeCharge.getUtilitiesProduct()).thenReturn(PRODUCT_ID);
		given(oneTimeCharge.getUtilsPriceAmount()).willReturn(PRICE);
		given(oneTimeCharge.getUtilsPriceCurrency()).willReturn(CURRENCY);
		given(oneTimeCharge.getUtilsSemanticsName1()).willReturn(ONETIME_CHARGE_SEMANTICNAME1);
		given(oneTimeCharge.getUtilsSemanticsName2()).willReturn(ONETIME_CHARGE_SEMANTICNAME2);

		final List<I_UtilsProdChgOneTimeType> oneTimeCharges = new ArrayList<>();
		oneTimeCharges.add(oneTimeCharge);

		return oneTimeCharges;
	}

	private List<I_UtilsProdChgRecurringType> setUpRecurringChargeDatas()
	{
		final I_UtilsProdChgRecurringType recurringCharge = mock(I_UtilsProdChgRecurringType.class);

		given(recurringCharge.getUtilitiesProduct()).willReturn(PRODUCT_ID);
		given(recurringCharge.getUtilsPriceAmount()).willReturn(PRICE);
		given(recurringCharge.getUtilsPriceCurrency()).willReturn(CURRENCY);
		given(recurringCharge.getUtilsSemanticsName1()).willReturn(RECURRING_CHARGE_SEMANTICNAME1);
		given(recurringCharge.getUtilsSemanticsName2()).willReturn(RECURRING_CHARGE_SEMANTICNAME2);
		given(recurringCharge.getUtilsPriceFrequencyUnit()).willReturn(ONE);
		given(recurringCharge.getUtilsPriceFrequencyValue()).willReturn(ONE);
		given(recurringCharge.getUtilsPriceID()).willReturn(PRICE_NAME);
		given(recurringCharge.getUtilsPriceName()).willReturn(PRICE_NAME);

		final List<I_UtilsProdChgRecurringType> recurringCharges = new ArrayList<>();
		recurringCharges.add(recurringCharge);

		return recurringCharges;
	}

	private List<I_UtilsProdBillingAttributeType> setUpBillingAttributeDatas()
	{
		final I_UtilsProdBillingAttributeType billingAttributeData = mock(I_UtilsProdBillingAttributeType.class);
		given(billingAttributeData.getUtilitiesProduct()).willReturn(PRODUCT_ID);

		given(billingAttributeData.getUtilsSemanticsName1()).willReturn(PRODUCT_ATTRIBUTE_SEMANTICNAME1);
		given(billingAttributeData.getUtilsSemanticsName2()).willReturn(PRODUCT_ATTRIBUTE_SEMANTICNAME2);
		given(billingAttributeData.getUtilsProductEntryValue()).willReturn(ONE);

		final List<I_UtilsProdBillingAttributeType> billingAttributesDatas = new ArrayList<>();
		billingAttributesDatas.add(billingAttributeData);

		return billingAttributesDatas;
	}

	private List<I_UtilsProdSalesAttributeType> setUpSalesAttributeDatas()
	{
		final I_UtilsProdSalesAttributeType salesAttributeData = mock(I_UtilsProdSalesAttributeType.class);
		given(salesAttributeData.getUtilitiesProduct()).willReturn(PRODUCT_ID);

		given(salesAttributeData.getUtilsSemanticsName1()).willReturn(PRODUCT_ATTRIBUTE_SEMANTICNAME1);
		given(salesAttributeData.getUtilsSemanticsName2()).willReturn(PRODUCT_ATTRIBUTE_SEMANTICNAME2);
		given(salesAttributeData.getUtilsSalesFactValue()).willReturn(ONE);
		given(salesAttributeData.getCurrency()).willReturn(CURRENCY);

		final List<I_UtilsProdSalesAttributeType> salesAttributeDatas = new ArrayList<>();
		salesAttributeDatas.add(salesAttributeData);

		return salesAttributeDatas;
	}

	private List<I_UtilsProdDiscountType> setUpProductDiscountDatas()
	{
		final I_UtilsProdDiscountType discountsData = mock(I_UtilsProdDiscountType.class);

		given(discountsData.getUtilitiesProduct()).willReturn(PRODUCT_ID);
		given(discountsData.getUtilsDiscountAmount()).willReturn(BigDecimal.ONE);
		given(discountsData.getUtilsDiscountCurrency()).willReturn(CURRENCY);
		given(discountsData.getUtilsSemanticsName1()).willReturn(DISCOUNT_SEMANTICNAME1);
		given(discountsData.getUtilsSemanticsName2()).willReturn(DISCOUNT_SEMANTICNAME2);
		given(discountsData.getUtilsDiscountDemand()).willReturn(BigDecimal.ONE);
		given(discountsData.getUtilsDiscountFactor()).willReturn(CURRENCY);
		given(discountsData.getUtilsDiscountQuantity()).willReturn(BigDecimal.ONE);
		given(discountsData.getUtilsDiscountText()).willReturn(DISCOUNT_TEXT);
		given(discountsData.getUtilsDiscountType()).willReturn(ONE);
		given(discountsData.getUtilsDiscountInPercent()).willReturn(BigDecimal.TEN);
		given(discountsData.getUtilsMeasurementUnit()).willReturn(CURRENCY);

		final List<I_UtilsProdDiscountType> discountsDatas = new ArrayList<>();
		discountsDatas.add(discountsData);

		return discountsDatas;
	}
}

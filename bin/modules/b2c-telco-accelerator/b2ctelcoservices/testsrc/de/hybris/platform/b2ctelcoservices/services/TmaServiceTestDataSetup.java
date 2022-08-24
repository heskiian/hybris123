/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.b2ctelcofacades.data.SubscriptionStatusData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.subscriptionservices.enums.SubscriptionStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.util.ObjectUtils;


/**
 * Setup class for generating test data related to the Service functionality.
 */
public abstract class TmaServiceTestDataSetup
{
	public static final String TEST_ID = "TEST_SERVICE_ID";
	public static final String TEST_NAME = "TEST_NAME";
	public static final String TEST_PRODUCT_CODE = "TEST_PRODUCT_CODE";
	public static final TmaServiceTypeData TEST_SERVICE_TYPE_DATA = TmaServiceTypeData.TARIFF_PLAN;
	public static final TmaServiceType TEST_SERVICE_TYPE_MODEL = TmaServiceType.TARIFF_PLAN;
	public static final String TEST_CREATE_BILLING_SUBSCRIPTION_ID = "TEST_CREATE_BILLING_SUBSCRIPTION_ID";
	public static final String TEST_BILLING_SUBSCRIPTION_ID = "billingSubscriptionId1";
	public static final SubscriptionStatus TEST_SUBSCRIPTION_STATUS = SubscriptionStatus.ACTIVE;
	public static final SimpleDateFormat TEST_DATE_FORMAT = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	public static final String TEST_START_DATE = "11-08-2017 10:20:56";
	public static final String TEST_END_DATE = "11-08-2022 10:20:56";
	public static final String TEST_RENEWAL_TYPE = "NONE";
	public static final boolean TEST_CANCELLABLE_FLAG = false;
	public static final String TEST_BILLING_FREQUENCY = "monthly";
	public static final int TEST_CONTRACT_DURATION = 2;
	public static final String TEST_CONTRACT_FREQUENCY = "year";
	public static final String NOT_EXISTING_SUBSCRIBER_ID = "NOT_EXISTING_SUBSCRIBER_ID";
	public static final String TEST_SUBSCRIBER_ID = "02012344321";
	public static final String TEST_BILLING_SYSTEM_ID = "IN";
	public static final String TEST_PARENT_PO_CODE = "TEST_PARENT_PO_CODE";
	public static final String NOT_EXISTING_PARENT_PRODUCT_OFFERING = "NOT_EXISTING_PARENT_PRODUCT_OFFERING";
	public static final String NOT_EXISTING_PRODUCT_CODE = "NOT_EXISTING_PRODUCT_CODE";
	public static final String TEST_PAYMENT_ID = "9874687087554";
	private static final String PRODUCT_CODE = "productCodeTest";

	public static TmaSubscribedProductData generateTmaSubscribedProductData(final String billingSystemId,
			final String billingSubscriptionId, final String parentProductOffering) throws ParseException
	{
		final TmaSubscribedProductData subscribedProductData = new TmaSubscribedProductData();
		subscribedProductData.setBillingsystemId(billingSystemId);
		subscribedProductData.setBillingSubscriptionId(billingSubscriptionId);
		subscribedProductData.setId(TEST_ID);
		subscribedProductData.setName(TEST_NAME);
		subscribedProductData.setProductCode(TEST_PRODUCT_CODE);
		subscribedProductData.setServiceType(TEST_SERVICE_TYPE_DATA);
		subscribedProductData.setSubscriptionBaseId(TEST_SUBSCRIBER_ID);
		subscribedProductData.setSubscriptionStatus(TEST_SUBSCRIPTION_STATUS.name());
		subscribedProductData.setStartDate(TEST_DATE_FORMAT.parse(TEST_START_DATE));
		subscribedProductData.setEndDate(TEST_DATE_FORMAT.parse(TEST_END_DATE));
		subscribedProductData.setCancelledDate(null);
		subscribedProductData.setRenewalType(TEST_RENEWAL_TYPE);
		subscribedProductData.setCancellable(TEST_CANCELLABLE_FLAG);
		subscribedProductData.setBillingFrequency(TEST_BILLING_FREQUENCY);
		subscribedProductData.setContractDuration(TEST_CONTRACT_DURATION);
		subscribedProductData.setContractFrequency(TEST_CONTRACT_FREQUENCY);
		subscribedProductData.setParentPOCode(parentProductOffering);
		subscribedProductData.setPaymentMethodId(TEST_PAYMENT_ID);
		return subscribedProductData;
	}

	public static TmaSubscribedProductData generateRandomTmaSubscribedProductData(final String billingSystemId,
			final String billingSubscriptionId) throws ParseException
	{
		final TmaSubscribedProductData subscribedProductData = new TmaSubscribedProductData();
		subscribedProductData.setName(RandomStringUtils.randomAlphabetic(10));
		subscribedProductData.setProductCode(PRODUCT_CODE);
		subscribedProductData.setServiceType(TmaServiceTypeData.ADD_ON);
		subscribedProductData.setSubscriptionBaseId(TEST_SUBSCRIBER_ID);
		subscribedProductData.setBillingsystemId(billingSystemId);
		subscribedProductData.setBillingSubscriptionId(billingSubscriptionId);
		subscribedProductData.setSubscriptionStatus(SubscriptionStatusData.EXPIRED.toString());
		subscribedProductData.setStartDate(new Date());
		subscribedProductData.setEndDate(new Date());
		subscribedProductData.setCancelledDate(new Date());
		subscribedProductData.setRenewalType(RandomStringUtils.randomAlphabetic(10));
		subscribedProductData.setCancellable(true);
		subscribedProductData.setBillingFrequency(RandomStringUtils.randomAlphabetic(10));
		subscribedProductData.setContractDuration((int) Math.random());
		subscribedProductData.setContractFrequency(RandomStringUtils.randomAlphabetic(10));
		subscribedProductData.setParentPOCode(TEST_PARENT_PO_CODE);
		subscribedProductData.setPaymentMethodId(TEST_PAYMENT_ID);
		return subscribedProductData;
	}

	public static TmaSubscribedProductModel generateTmaSubscribedProductModelForSubscriptionBase(
			final TmaSubscribedProductModel subscribedProductModel, final TmaSubscriptionBaseModel subscriptionBaseModel)
			throws ParseException
	{
		subscribedProductModel.setId(TEST_ID);
		subscribedProductModel.setName(TEST_NAME);
		subscribedProductModel.setProductCode(TEST_PRODUCT_CODE);
		subscribedProductModel.setServiceType(TEST_SERVICE_TYPE_MODEL);
		subscribedProductModel.setSubscriptionBase(subscriptionBaseModel);
		subscribedProductModel.setBillingsystemId(TEST_BILLING_SYSTEM_ID);
		subscribedProductModel.setBillingSubscriptionId(TEST_CREATE_BILLING_SUBSCRIPTION_ID);
		subscribedProductModel.setSubscriptionStatus(TEST_SUBSCRIPTION_STATUS.name());
		subscribedProductModel.setStartDate(TEST_DATE_FORMAT.parse(TEST_START_DATE));
		subscribedProductModel.setEndDate(TEST_DATE_FORMAT.parse(TEST_END_DATE));
		subscribedProductModel.setCancelledDate(null);
		subscribedProductModel.setRenewalType(TEST_RENEWAL_TYPE);
		subscribedProductModel.setCancellable(TEST_CANCELLABLE_FLAG);
		subscribedProductModel.setBillingFrequency(TEST_BILLING_FREQUENCY);
		subscribedProductModel.setContractDuration(TEST_CONTRACT_DURATION);
		subscribedProductModel.setContractFrequency(TEST_CONTRACT_FREQUENCY);
		return subscribedProductModel;
	}

	public static TmaSubscribedProductModel generateTmaSubscribedProductModelForProductOffering(
			final TmaSubscribedProductModel subscribedProductModel, final TmaProductOfferingModel productOfferingModel,
			final TmaSubscriptionBaseModel subscriptionBaseModel) throws ParseException
	{
		subscribedProductModel.setId(TEST_ID);
		subscribedProductModel.setName(TEST_NAME);
		subscribedProductModel.setProductCode(TEST_PRODUCT_CODE);
		subscribedProductModel.setServiceType(TEST_SERVICE_TYPE_MODEL);
		subscribedProductModel.setSubscriptionBase(subscriptionBaseModel);
		subscribedProductModel.setBillingsystemId(TEST_BILLING_SYSTEM_ID);
		subscribedProductModel.setBillingSubscriptionId(TEST_CREATE_BILLING_SUBSCRIPTION_ID);
		subscribedProductModel.setSubscriptionStatus(TEST_SUBSCRIPTION_STATUS.name());
		subscribedProductModel.setStartDate(TEST_DATE_FORMAT.parse(TEST_START_DATE));
		subscribedProductModel.setEndDate(TEST_DATE_FORMAT.parse(TEST_END_DATE));
		subscribedProductModel.setCancelledDate(null);
		subscribedProductModel.setRenewalType(TEST_RENEWAL_TYPE);
		subscribedProductModel.setCancellable(TEST_CANCELLABLE_FLAG);
		subscribedProductModel.setBillingFrequency(TEST_BILLING_FREQUENCY);
		subscribedProductModel.setContractDuration(TEST_CONTRACT_DURATION);
		subscribedProductModel.setContractFrequency(TEST_CONTRACT_FREQUENCY);
		if (!ObjectUtils.isEmpty(productOfferingModel))
		{
			subscribedProductModel.setBundledProductCode(productOfferingModel.getCode());
		}
		return subscribedProductModel;
	}

	public static void assertTmaSubscribedProductModelsPopulatedWithDefaultValues(final TmaSubscribedProductModel target,
			final TmaSubscriptionBaseModel subscriptionBaseModel) throws ParseException
	{
		assertEquals(subscriptionBaseModel, target.getSubscriptionBase());
		assertTmaSubscribedProductModelsPopulatedWithCommonDefaultValues(target);
	}

	public static void assertTmaSubscribedProductModelsPopulatedWithCommonDefaultValues(final TmaSubscribedProductModel target)
			throws ParseException
	{
		assertEquals(TEST_NAME, target.getName());
		assertEquals(TEST_PRODUCT_CODE, target.getProductCode());
		assertEquals(TmaServiceType.valueOf(TEST_SERVICE_TYPE_DATA.name()),
				target.getServiceType());
		assertEquals(TEST_BILLING_SYSTEM_ID, target.getBillingsystemId());
		assertEquals(TEST_DATE_FORMAT.parse(TEST_START_DATE), target.getStartDate());
		assertEquals(TEST_DATE_FORMAT.parse(TEST_END_DATE), target.getEndDate());
		assertNull(target.getCancelledDate());
		assertEquals(TEST_RENEWAL_TYPE, target.getRenewalType());
		assertEquals(TEST_CANCELLABLE_FLAG, target.getCancellable());
		assertEquals(TEST_BILLING_FREQUENCY, target.getBillingFrequency());
		assertTrue(TEST_CONTRACT_DURATION == target.getContractDuration());
		assertEquals(TEST_CONTRACT_FREQUENCY, target.getContractFrequency());
	}

	public static void assertTmaSubscribedProductDataIsPopulatedWithDefaultValues(
			final TmaSubscribedProductData subscribedProductData) throws ParseException
	{
		assertEquals(TEST_SUBSCRIBER_ID, subscribedProductData.getSubscriptionBaseId());
		assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues(subscribedProductData);
	}

	public static void assertTmaSubscribedProductDataIsPopulatedWithDefaultValuesForPO(
			final TmaSubscribedProductData subscribedProductData) throws ParseException
	{
		assertEquals(TEST_PARENT_PO_CODE, subscribedProductData.getParentPOCode());
		assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues(subscribedProductData);
	}

	public static void assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues(
			final TmaSubscribedProductData subscribedProductData) throws ParseException
	{
		assertEquals(TEST_NAME, subscribedProductData.getName());
		assertEquals(TEST_PRODUCT_CODE, subscribedProductData.getProductCode());
		assertEquals(TmaServiceTypeData.valueOf(TEST_SERVICE_TYPE_DATA.name()), subscribedProductData.getServiceType());
		assertEquals(TEST_DATE_FORMAT.parse(TEST_START_DATE), subscribedProductData.getStartDate());
		assertEquals(TEST_DATE_FORMAT.parse(TEST_END_DATE), subscribedProductData.getEndDate());
		assertNull(subscribedProductData.getCancelledDate());
		assertEquals(TEST_RENEWAL_TYPE, subscribedProductData.getRenewalType());
		assertEquals(TEST_CANCELLABLE_FLAG, subscribedProductData.getCancellable());
		assertEquals(TEST_BILLING_FREQUENCY, subscribedProductData.getBillingFrequency());
		assertTrue(TEST_CONTRACT_DURATION == subscribedProductData.getContractDuration());
		assertEquals(TEST_CONTRACT_FREQUENCY, subscribedProductData.getContractFrequency());
	}

	public static void assertSubscribedProductIsUpdated(final TmaSubscribedProductData dataForUpdate,
			final TmaSubscribedProductData updated)
	{
		assertTrue(EqualsBuilder.reflectionEquals(dataForUpdate, updated,
				new ArrayList<>(
						Arrays.asList("productOfferingRef", "billingAccountRef", "subscriptionAccesses", "externalIdentifiers"))));
		assertNotNull(updated.getProductOfferingRef());
		assertNotNull(updated.getBillingAccountRef());
		assertNotNull(updated.getSubscriptionAccesses());
	}

	public static void assertTmaSubscribedProductModelsPopulatedWithDefaultValuesForPO(final TmaSubscribedProductModel target,
			final TmaProductOfferingModel productOfferingModel) throws ParseException
	{
		assertEquals(productOfferingModel.getCode(), target.getBundledProductCode());
		assertTmaSubscribedProductModelsPopulatedWithCommonDefaultValues(target);
	}


	public static TmaSubscriptionBaseModel createSubscriptionBaseForIdentity(final String subscriberIdentity,
			final String billingSystemId)
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = new TmaSubscriptionBaseModel();
		subscriptionBaseModel.setSubscriberIdentity(subscriberIdentity);
		subscriptionBaseModel.setBillingSystemId(billingSystemId);
		return subscriptionBaseModel;
	}


	public static TmaProductOfferingModel createParentProductOffering()
	{
		final TmaProductOfferingModel productOfferingModel = new TmaBundledProductOfferingModel();
		productOfferingModel.setCode(TEST_PARENT_PO_CODE);
		return productOfferingModel;
	}
}

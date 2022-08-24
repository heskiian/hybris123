/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderTierUsageCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.impl.DefaultTmaCalculationService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.order.PaymentModeService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @since 1907
 */
@IntegrationTest
public class DefaultTmaCalculationServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String TAPAS_S_CODE = "tapasS";
	private static final String CURRENCY_ISO_CODE = "USD";
	private static final double PAYMENT_COST = 8;
	private static final double DELIVERY_COST = 0;
	private static final int ZERO = 0;
	private static final String BILLING_TIME_CODE = "paynow";
	private static final String MONTHLY_BILLING_TIME = "monthly";
	private static final String ONFIRSTBILL_BILLING_TIME = "onfirstbill";

	@Resource
	private DefaultTmaCalculationService defaultTmaCalculationService;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private DefaultTmaPoService tmaPoService;
	@Resource
	private PaymentModeService paymentModeService;
	@Resource
	private DeliveryModeService deliveryModeService;

	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_calculateOrderPrices.impex", "utf-8");
		currency = commonI18NService.getCurrency(CURRENCY_ISO_CODE);
	}

	@Test
	public void testOrderEntriesPriceWhenPriceIsNet() throws CalculationException
	{
		final OrderModel order = createOrder(true);
		createEntryForOrder(order);

		defaultTmaCalculationService.calculateEntries(order, false);

		final AbstractOrderEntryModel orderEntry = order.getEntries().get(ZERO);
		final TmaAbstractOrderCompositePriceModel orderEntryPrice = (TmaAbstractOrderCompositePriceModel) orderEntry.getPrice();
		assertNotNull(orderEntryPrice);

		final List<TmaAbstractOrderPriceModel> childPrices = new ArrayList<>(orderEntryPrice.getChildPrices());
		final TmaAbstractOrderCompositePriceModel popPrice = (TmaAbstractOrderCompositePriceModel) childPrices.get(0);
		final List<TmaAbstractOrderPriceModel> popChildPrices = new ArrayList<>(popPrice.getChildPrices());

		final TmaAbstractOrderOneTimeChargePriceModel payNowPrice = (TmaAbstractOrderOneTimeChargePriceModel) getNetChargeFor(25.0,
				BILLING_TIME_CODE, popChildPrices);
		assertNotNull(payNowPrice);
		assertEquals(2.5, payNowPrice.getTaxRate(), ZERO);
		assertEquals(27.5, payNowPrice.getTaxIncludedAmount(), ZERO);

		final TmaAbstractOrderOneTimeChargePriceModel onFirstBillPrice = (TmaAbstractOrderOneTimeChargePriceModel) getNetChargeFor(
				10.0, ONFIRSTBILL_BILLING_TIME, popChildPrices);
		assertNotNull(onFirstBillPrice);
		assertEquals(1.0, onFirstBillPrice.getTaxRate(), ZERO);
		assertEquals(11.0, onFirstBillPrice.getTaxIncludedAmount(), ZERO);

		final TmaAbstractOrderRecurringChargePriceModel recurringChargePrice = (TmaAbstractOrderRecurringChargePriceModel) getNetChargeFor(
				10.0, MONTHLY_BILLING_TIME, popChildPrices);
		assertNotNull(recurringChargePrice);
		assertEquals(1, recurringChargePrice.getCycleStart(), ZERO);
		assertEquals(1.0, recurringChargePrice.getTaxRate(), ZERO);
		assertEquals(11.0, recurringChargePrice.getTaxIncludedAmount(), ZERO);
		assertNull(recurringChargePrice.getCycleEnd());
		assertEquals(recurringChargePrice.getBillingTime().getCode(), MONTHLY_BILLING_TIME);

		final TmaAbstractOrderTierUsageCompositePriceModel usageChargePrice = getUsageChargePrice(popChildPrices);
		final Set<TmaAbstractOrderPriceModel> usageChargeEntries = usageChargePrice.getChildPrices();
		final TmaAbstractOrderUsageChargePriceModel usageChargeEntry = (TmaAbstractOrderUsageChargePriceModel) usageChargeEntries
				.iterator().next();
		assertNotNull(usageChargeEntry);
		assertEquals(UsageChargeType.EACH_RESPECTIVE_TIER, usageChargePrice.getUsageChargeType());
		assertEquals(5, usageChargeEntry.getDutyFreeAmount(), ZERO);
		assertEquals(0.5, usageChargeEntry.getTaxRate(), ZERO);
		assertEquals(5.5, usageChargeEntry.getTaxIncludedAmount(), ZERO);
		assertEquals("phone_minutes", usageChargeEntry.getUsageUnit().getId());
		assertEquals(101, usageChargeEntry.getTierStart(), ZERO);
		assertEquals(200, usageChargeEntry.getTierEnd(), ZERO);
	}

	@Test
	public void testOrderEntriesPriceWhenPriceIsNotNet() throws CalculationException
	{
		final OrderModel order = createOrder(false);
		createEntryForOrder(order);

		defaultTmaCalculationService.calculateEntries(order, false);

		final AbstractOrderEntryModel orderEntry = order.getEntries().get(ZERO);
		final TmaAbstractOrderCompositePriceModel orderEntryPrice = (TmaAbstractOrderCompositePriceModel) orderEntry.getPrice();
		assertNotNull(orderEntryPrice);

		final List<TmaAbstractOrderPriceModel> childPrices = new ArrayList<>(orderEntryPrice.getChildPrices());
		final TmaAbstractOrderCompositePriceModel popPrice = (TmaAbstractOrderCompositePriceModel) childPrices.get(0);
		final List<TmaAbstractOrderPriceModel> popChildPrices = new ArrayList<>(popPrice.getChildPrices());

		final TmaAbstractOrderOneTimeChargePriceModel payNowPrice = (TmaAbstractOrderOneTimeChargePriceModel) getChargeFor(25.0,
				BILLING_TIME_CODE, popChildPrices);
		assertNotNull(payNowPrice);
		assertEquals(2.27, payNowPrice.getTaxRate(), ZERO);
		assertEquals(22.73, payNowPrice.getDutyFreeAmount(), ZERO);

		final TmaAbstractOrderOneTimeChargePriceModel onFirstBillPrice = (TmaAbstractOrderOneTimeChargePriceModel) getChargeFor(
				10.0, ONFIRSTBILL_BILLING_TIME, popChildPrices);
		assertNotNull(onFirstBillPrice);
		assertEquals(0.91, onFirstBillPrice.getTaxRate(), ZERO);
		assertEquals(9.09, onFirstBillPrice.getDutyFreeAmount(), ZERO);

		final TmaAbstractOrderRecurringChargePriceModel recurringChargePrice = (TmaAbstractOrderRecurringChargePriceModel) getChargeFor(
				10.0, MONTHLY_BILLING_TIME, popChildPrices);
		assertNotNull(recurringChargePrice);
		assertEquals(1, recurringChargePrice.getCycleStart(), ZERO);
		assertEquals(0.91, recurringChargePrice.getTaxRate(), ZERO);
		assertEquals(9.09, recurringChargePrice.getDutyFreeAmount(), ZERO);
		assertNull(recurringChargePrice.getCycleEnd());
		assertEquals(recurringChargePrice.getBillingTime().getCode(), MONTHLY_BILLING_TIME);

		final TmaAbstractOrderTierUsageCompositePriceModel usageChargePrice = getUsageChargePrice(popChildPrices);
		final Set<TmaAbstractOrderPriceModel> usageChargeEntries = usageChargePrice.getChildPrices();
		final TmaAbstractOrderUsageChargePriceModel usageChargeEntry = (TmaAbstractOrderUsageChargePriceModel) usageChargeEntries
				.iterator().next();
		assertNotNull(usageChargeEntry);
		assertEquals(UsageChargeType.EACH_RESPECTIVE_TIER, usageChargePrice.getUsageChargeType());
		assertEquals(4.55, usageChargeEntry.getDutyFreeAmount(), ZERO);
		assertEquals(0.45, usageChargeEntry.getTaxRate(), ZERO);
		assertEquals(5, usageChargeEntry.getTaxIncludedAmount(), ZERO);
		assertEquals("phone_minutes", usageChargeEntry.getUsageUnit().getId());
		assertEquals(101, usageChargeEntry.getTierStart(), ZERO);
		assertEquals(200, usageChargeEntry.getTierEnd(), ZERO);
	}

	@Test
	public void testOrderPriceWhenPriceIsNet() throws CalculationException
	{
		final OrderModel order = createOrder(true);
		createEntryForOrder(order);

		defaultTmaCalculationService.calculate(order);

		final TmaAbstractOrderCompositePriceModel orderPrice = (TmaAbstractOrderCompositePriceModel) order.getPrice();
		assertNotNull(orderPrice);
		final List<TmaAbstractOrderPriceModel> childPrices = new ArrayList<>(orderPrice.getChildPrices());
		final TmaAbstractOrderOneTimeChargePriceModel paymentPrice =
				(TmaAbstractOrderOneTimeChargePriceModel) getNetChargeFor(PAYMENT_COST, BILLING_TIME_CODE, childPrices);
		assertEquals(TmaAbstractOrderPriceType.PAYMENT_COST, paymentPrice.getPriceType());
		assertEquals(0.8, paymentPrice.getTaxRate(), ZERO);
		assertEquals(8.8, paymentPrice.getTaxIncludedAmount(), ZERO);
		final TmaAbstractOrderOneTimeChargePriceModel deliveryPrice = (TmaAbstractOrderOneTimeChargePriceModel) getNetChargeFor(
				DELIVERY_COST, BILLING_TIME_CODE, childPrices);
		assertEquals(TmaAbstractOrderPriceType.DELIVERY_COST, deliveryPrice.getPriceType());
		assertEquals(0, deliveryPrice.getTaxRate(), ZERO);
		assertEquals(0, deliveryPrice.getTaxIncludedAmount(), ZERO);
	}

	@Test
	public void testOrderPriceWhenPriceIsNotNet() throws CalculationException
	{
		final OrderModel order = createOrder(false);
		createEntryForOrder(order);

		defaultTmaCalculationService.calculate(order);

		final TmaAbstractOrderCompositePriceModel orderPrice = (TmaAbstractOrderCompositePriceModel) order.getPrice();
		assertNotNull(orderPrice);
		final List<TmaAbstractOrderPriceModel> childPrices = new ArrayList<>(orderPrice.getChildPrices());
		final TmaAbstractOrderOneTimeChargePriceModel paymentPrice =
				(TmaAbstractOrderOneTimeChargePriceModel) getChargeFor(PAYMENT_COST, BILLING_TIME_CODE, childPrices);
		assertEquals(TmaAbstractOrderPriceType.PAYMENT_COST, paymentPrice.getPriceType());
		assertEquals(0.73, paymentPrice.getTaxRate(), ZERO);
		assertEquals(7.27, paymentPrice.getDutyFreeAmount(), ZERO);
		final TmaAbstractOrderOneTimeChargePriceModel deliveryPrice = (TmaAbstractOrderOneTimeChargePriceModel) getChargeFor(
				DELIVERY_COST, BILLING_TIME_CODE, childPrices);
		assertEquals(TmaAbstractOrderPriceType.DELIVERY_COST, deliveryPrice.getPriceType());
		assertEquals(0, deliveryPrice.getTaxRate(), ZERO);
		assertEquals(0, deliveryPrice.getTaxIncludedAmount(), ZERO);
	}

	private TmaAbstractOrderPriceModel getNetChargeFor(final Double value, final String billingTimeCode,
			final List<TmaAbstractOrderPriceModel> childPrices)
	{
		return childPrices.stream().filter(childPrice -> childPrice instanceof TmaAbstractOrderChargePriceModel)
				.filter(childPrice -> ((TmaAbstractOrderChargePriceModel) childPrice).getDutyFreeAmount().equals(value) &&
						((TmaAbstractOrderChargePriceModel) childPrice).getBillingTime().getCode().equals(billingTimeCode)).findFirst()
				.orElse(null);
	}

	private TmaAbstractOrderPriceModel getChargeFor(final Double value, final String billingTimeCode,
			final List<TmaAbstractOrderPriceModel> childPrices)
	{
		return childPrices.stream().filter(childPrice -> childPrice instanceof TmaAbstractOrderChargePriceModel)
				.filter(childPrice -> ((TmaAbstractOrderChargePriceModel) childPrice).getTaxIncludedAmount().equals(value) &&
						((TmaAbstractOrderChargePriceModel) childPrice).getBillingTime().getCode().equals(billingTimeCode)).findFirst()
				.orElse(null);
	}

	private TmaAbstractOrderTierUsageCompositePriceModel getUsageChargePrice(List<TmaAbstractOrderPriceModel> childPrices)
	{
		return (TmaAbstractOrderTierUsageCompositePriceModel) childPrices.stream()
				.filter(childPrice -> childPrice instanceof TmaAbstractOrderTierUsageCompositePriceModel).findFirst().orElse(null);
	}

	protected OrderModel createOrder(boolean isNet)
	{
		final CustomerModel customerModel = modelService.create(CustomerModel.class);
		customerModel.setUid("customer");
		customerModel.setName("test Customer");
		customerModel.setCustomerID("testCustomerID");

		final OrderModel orderModel = modelService.create(OrderModel.class);
		orderModel.setCode("testOrder");
		orderModel.setUser(customerModel);
		orderModel.setCurrency(currency);
		orderModel.setDate(new Date());
		orderModel.setNet(isNet);

		final PaymentModeModel paymentMode = paymentModeService.getPaymentModeForCode("testPaymentMode");
		orderModel.setPaymentMode(paymentMode);

		final DeliveryModeModel deliveryMode = deliveryModeService.getDeliveryModeForCode("testDeliveryMode");
		orderModel.setDeliveryMode(deliveryMode);

		modelService.save(orderModel);
		return orderModel;
	}

	protected OrderEntryModel createEntryForOrder(AbstractOrderModel order)
	{
		final OrderEntryModel entry = modelService.create(OrderEntryModel.class);
		entry.setEntryNumber(ZERO);
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(TAPAS_S_CODE);
		entry.setProduct(productOffering);
		entry.setUnit(productOffering.getUnit());
		entry.setOrder(order);
		entry.setQuantity(1L);
		entry.setProcessType(TmaProcessType.ACQUISITION);
		modelService.save(entry);
		order.setEntries(Collections.singletonList(entry));
		return entry;
	}
}

/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaComponentProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaDiscountProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.BillingFrequencyModel;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.util.PriceValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;


/**
 * Unit test for {@link DefaultTmaFindOrderEntryPriceStrategy}.
 *
 * @since 2007
 */
@UnitTest
public class DefaultTmaFindOrderEntryPriceStrategyTest
{
	private static final String PAY_NOW_EVENT = "payNow";
	private static final String PAY_ON_FIRST_BILL_EVENT = "onfirstbill";
	private static final String MONTHLY_EVENT = "monthly";

	private DefaultTmaFindOrderEntryPriceStrategy priceStrategy;

	@Mock
	private TmaCommercePriceService commercePriceService;
	@Mock
	private TmaBillingTimeService billingTimeService;

	private AbstractOrderEntryModel orderEntryModel;
	private BillingTimeModel payNowEvent;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		priceStrategy = new DefaultTmaFindOrderEntryPriceStrategy(commercePriceService, billingTimeService);
		orderEntryModel = createOrderEntry("USD", true);
		payNowEvent = createBillingTime(BillingEventModel.class, PAY_NOW_EVENT);
	}

	@Test(expected = CalculationException.class)
	public void testFindBasePriceNoPriceFound() throws CalculationException
	{
		Mockito.when(commercePriceService.getBestApplicablePrice(any(AbstractOrderEntryModel.class))).thenReturn(null);
		priceStrategy.findBasePrice(orderEntryModel);
	}

	@Test
	public void testFindBasePriceForPopWithOnePayNowOtcPop() throws Exception
	{
		final TmaProductOfferingPriceModel payNowCharge = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 10.0, payNowEvent);

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel)).thenReturn(createPriceRow(payNowCharge));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 10.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWithDifferentEventOtcPops() throws Exception
	{
		final TmaProductOfferingPriceModel payNowCharge = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 10.0, payNowEvent);
		final TmaProductOfferingPriceModel payOnFirstBill = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 20.0, createBillingTime(BillingEventModel.class, PAY_ON_FIRST_BILL_EVENT));
		final Set<TmaProductOfferingPriceModel> childPops = new HashSet<>(Arrays.asList(payNowCharge, payOnFirstBill));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel))
				.thenReturn(createPriceRow(createCompositeProductOfferingPrice(childPops)));
		Mockito.when(billingTimeService.getDefaultBillingTime())
				.thenReturn(payNowEvent);

		final double expectedPrice = 10.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWithSameEventOtcPops() throws Exception
	{
		final TmaProductOfferingPriceModel payNowPop1 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 10.0, payNowEvent);
		final TmaProductOfferingPriceModel payNowPop2 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 20.0, payNowEvent);
		final Set<TmaProductOfferingPriceModel> childPops = new HashSet<>(Arrays.asList(payNowPop1, payNowPop2));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel))
				.thenReturn(createPriceRow(createCompositeProductOfferingPrice(childPops)));
		Mockito.when(billingTimeService.getDefaultBillingTime())
				.thenReturn(payNowEvent);

		final double expectedPrice = 30.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWithMissingPayNowOtcPops() throws Exception
	{
		final TmaProductOfferingPriceModel payOnFirstBillCharge = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 10.0, createBillingTime(BillingEventModel.class, PAY_ON_FIRST_BILL_EVENT));
		final TmaProductOfferingPriceModel recurringCharge = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 3.0, createBillingTime(BillingFrequencyModel.class, MONTHLY_EVENT));
		final Set<TmaProductOfferingPriceModel> childPops = new HashSet<>(Arrays.asList(payOnFirstBillCharge, recurringCharge));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel))
				.thenReturn(createPriceRow(createCompositeProductOfferingPrice(childPops)));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 0.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWithMultipleCompositePops() throws Exception
	{
		final BillingTimeModel payOnFirstBillEvent = createBillingTime(BillingEventModel.class, PAY_ON_FIRST_BILL_EVENT);
		final BillingTimeModel monthlyEvent = createBillingTime(BillingFrequencyModel.class, MONTHLY_EVENT);

		final TmaProductOfferingPriceModel payNowPop1 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 10.0, payNowEvent);
		final TmaProductOfferingPriceModel payOnFirstBillPrice = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 20.0, payOnFirstBillEvent);

		final TmaProductOfferingPriceModel payNowPop2 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 20.0, payNowEvent);

		final TmaProductOfferingPriceModel recurringPop1 = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 4.0, monthlyEvent);
		final TmaProductOfferingPriceModel recurringPop2 = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 2.0, monthlyEvent);
		final TmaProductOfferingPriceModel payNowPop3 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 30.0, payNowEvent);

		final TmaProductOfferingPriceModel thirdLevelCompositePop = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(recurringPop1, recurringPop2, payNowPop3)));
		final TmaProductOfferingPriceModel secondLevelCompositePop = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop2, thirdLevelCompositePop)));

		final TmaProductOfferingPriceModel rootCompositePrice = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop1, payOnFirstBillPrice, secondLevelCompositePop)));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel)).thenReturn(createPriceRow(rootCompositePrice));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 60.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWitNetDiscount() throws Exception
	{
		final TmaProductOfferingPriceModel payNowPop1 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 100.0, payNowEvent);
		final BillingTimeModel monthlyEvent = createBillingTime(BillingFrequencyModel.class, MONTHLY_EVENT);
		final TmaProductOfferingPriceModel recurringPop1 = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 4.0, monthlyEvent);

		final TmaDiscountProdOfferPriceAlterationModel discountNet1 = createDiscount(20.0, payNowEvent, false);
		final TmaDiscountProdOfferPriceAlterationModel discountRecurring1 = createDiscount(5.0, monthlyEvent, false);

		final TmaProductOfferingPriceModel rootCompositePrice = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop1, discountNet1, discountRecurring1, recurringPop1)));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel)).thenReturn(createPriceRow(rootCompositePrice));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 80.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPopWitPercentageDiscount() throws Exception
	{
		final TmaProductOfferingPriceModel payNowPop1 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 500.0, payNowEvent);
		final BillingTimeModel monthlyEvent = createBillingTime(BillingFrequencyModel.class, MONTHLY_EVENT);
		final TmaProductOfferingPriceModel recurringPop1 = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 4.0, monthlyEvent);

		final TmaDiscountProdOfferPriceAlterationModel discountRecurring1 = createDiscount(5.0, monthlyEvent, false);
		final TmaDiscountProdOfferPriceAlterationModel discountRelative1 = createDiscount(10.0, payNowEvent, true);


		final TmaProductOfferingPriceModel rootCompositePrice = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop1, recurringPop1, discountRecurring1, discountRelative1)));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel)).thenReturn(createPriceRow(rootCompositePrice));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 450.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForCompositePopWithMultipleDiscounts() throws Exception
	{
		final TmaProductOfferingPriceModel payNowPop1 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 100.0, payNowEvent);
		final TmaProductOfferingPriceModel payNowPop2 = createComponentProductOfferingPrice(
				TmaOneTimeProdOfferPriceChargeModel.class, 200.0, payNowEvent);
		final BillingTimeModel monthlyEvent = createBillingTime(BillingFrequencyModel.class, MONTHLY_EVENT);
		final TmaProductOfferingPriceModel recurringPop1 = createComponentProductOfferingPrice(
				TmaRecurringProdOfferPriceChargeModel.class, 4.0, monthlyEvent);

		final TmaDiscountProdOfferPriceAlterationModel discountNet1 = createDiscount(5.0, payNowEvent, false);
		final TmaDiscountProdOfferPriceAlterationModel discountRecurring1 = createDiscount(5.0, monthlyEvent, false);
		final TmaDiscountProdOfferPriceAlterationModel discountRelative1 = createDiscount(10.0, payNowEvent, true);

		final TmaProductOfferingPriceModel secondLevelCompositePop = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop2, recurringPop1, discountRelative1)));
		final TmaProductOfferingPriceModel rootCompositePrice = createCompositeProductOfferingPrice(
				new HashSet<>(Arrays.asList(payNowPop1, discountNet1, discountRecurring1, secondLevelCompositePop)));

		Mockito.when(commercePriceService.getBestApplicablePrice(orderEntryModel)).thenReturn(createPriceRow(rootCompositePrice));
		Mockito.when(billingTimeService.getDefaultBillingTime()).thenReturn(payNowEvent);

		final double expectedPrice = 270.0;
		final PriceValue actualPrice = priceStrategy.findBasePrice(orderEntryModel);

		Assert.assertNotNull(actualPrice);
		assertEquals("base price is wrongly computed", expectedPrice, actualPrice.getValue(), 0);
	}

	private AbstractOrderEntryModel createOrderEntry(final String currencyCode, final boolean isNet)
	{
		final CurrencyModel currencyModel = new CurrencyModel();
		currencyModel.setIsocode(currencyCode);

		final AbstractOrderModel orderModel = new AbstractOrderModel();
		orderModel.setCurrency(currencyModel);
		orderModel.setNet(isNet);

		final AbstractOrderEntryModel orderEntryModel = new AbstractOrderEntryModel();
		orderEntryModel.setOrder(orderModel);

		return orderEntryModel;
	}

	private <T> TmaProductOfferingPriceModel createComponentProductOfferingPrice(final Class<T> popClass, final Double price,
			final BillingTimeModel billingTime) throws Exception
	{
		final TmaComponentProdOfferPriceModel componentProdOfferPriceModel = (TmaComponentProdOfferPriceModel) popClass
				.getConstructor().newInstance();
		componentProdOfferPriceModel.setValue(price);
		componentProdOfferPriceModel.setPriceEvent(billingTime);

		return componentProdOfferPriceModel;
	}

	private TmaDiscountProdOfferPriceAlterationModel createDiscount(final Double price,
			final BillingTimeModel billingTime, boolean isPercentage)
	{
		final TmaDiscountProdOfferPriceAlterationModel discountModel =
				new TmaDiscountProdOfferPriceAlterationModel();
		discountModel.setValue(price);
		discountModel.setPriceEvent(billingTime);
		discountModel.setIsPercentage(isPercentage);

		return discountModel;
	}

	private TmaProductOfferingPriceModel createCompositeProductOfferingPrice(final Set<TmaProductOfferingPriceModel> childPrices)
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPriceModel = new TmaCompositeProdOfferPriceModel();
		compositeProdOfferPriceModel.setChildren(Collections.unmodifiableSet(childPrices));

		return compositeProdOfferPriceModel;
	}

	private PriceRowModel createPriceRow(final TmaProductOfferingPriceModel productOfferingPrice)
	{
		final PriceRowModel priceRowModel = new PriceRowModel();
		priceRowModel.setProductOfferingPrice(productOfferingPrice);

		return priceRowModel;
	}

	private <T> BillingTimeModel createBillingTime(final Class<T> billingTimeClass, final String code) throws Exception
	{
		final BillingTimeModel billingTimeModel = (BillingTimeModel) billingTimeClass.getConstructor().newInstance();
		billingTimeModel.setCode(code);

		return billingTimeModel;
	}
}

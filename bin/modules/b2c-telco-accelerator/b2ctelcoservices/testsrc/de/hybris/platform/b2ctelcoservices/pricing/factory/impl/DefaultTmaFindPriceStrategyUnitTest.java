/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.factory.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.util.PriceValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTmaFindPriceStrategyUnitTest
{
	private static final String IPHONE_X_CODE = "iphone_x";
	private static final double EXPECTED_ONE_TIME_CHARGE = 123;
	private static final double EXPECTED_RECURRING_CHARGE = 321;
	private static final String CURRENCY_ISO_CODE = "USD";
	private static final int PRICE_VALUE = 699;

	@Mock
	private TmaCommercePriceService commercePriceService;

	@Mock
	private TmaBillingTimeService billingTimeService;

	@InjectMocks
	private DefaultTmaFindPriceStrategy findPriceStrategy = Mockito.spy(DefaultTmaFindPriceStrategy.class);

	@Test
	public void testFindBasePriceForPriceRowAndNoPriceOverrides() throws CalculationException
	{
		AbstractOrderEntryModel iphoneX = generateEntryForParams(0, 0, null, IPHONE_X_CODE);
		PriceRowModel priceRowModel = mock(PriceRowModel.class);
		when(priceRowModel.getPrice()).thenReturn(Double.valueOf(PRICE_VALUE));
		when(commercePriceService.getBestApplicablePrice(iphoneX)).thenReturn(priceRowModel);

		PriceValue basePrice = findPriceStrategy.findBasePrice(iphoneX);
		assertEquals(PRICE_VALUE, basePrice.getValue(), 0);
	}

	@Test
	public void testFindBasePriceForPriceOverrides() throws CalculationException
	{
		AbstractOrderEntryModel iphoneX = generateEntryForParams(0, 0, null, IPHONE_X_CODE);
		BillingEventModel billingTime = new BillingEventModel();

		SubscriptionPricePlanModel expectedSpp = generateExpectedSpp();
		OneTimeChargeEntryModel oneTimeChargeEntry = expectedSpp.getOneTimeChargeEntries().iterator().next();
		oneTimeChargeEntry.setBillingEvent(billingTime);
		when(billingTimeService.getDefaultBillingTime()).thenReturn(billingTime);
		when(commercePriceService.getBestApplicablePrice(iphoneX)).thenReturn(expectedSpp);

		PriceValue basePrice = findPriceStrategy.findBasePrice(iphoneX);
		assertEquals(EXPECTED_ONE_TIME_CHARGE, basePrice.getValue(), 0);
	}

	private AbstractOrderEntryModel generateEntryForParams(int entryNumber, int entryGroupNumber,
			TmaBundledProductOfferingModel bpo, String productCode)
	{
		AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setEntryNumber(entryNumber);
		entry.setEntryGroupNumbers(Sets.newHashSet(entryGroupNumber));
		entry.setBpo(bpo);
		entry.setProduct(generateSpoForCode(productCode));
		CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(CURRENCY_ISO_CODE);
		AbstractOrderModel orderModel = new AbstractOrderModel();
		orderModel.setCurrency(currency);
		orderModel.setNet(false);
		entry.setOrder(orderModel);

		return entry;
	}

	private TmaSimpleProductOfferingModel generateSpoForCode(String spoCode)
	{
		TmaSimpleProductOfferingModel spo = new TmaSimpleProductOfferingModel();
		spo.setCode(spoCode);

		return spo;
	}

	private SubscriptionPricePlanModel generateExpectedSpp()
	{
		SubscriptionPricePlanModel expectedSpp = new SubscriptionPricePlanModel();
		OneTimeChargeEntryModel oneTimeChargeEntry = new OneTimeChargeEntryModel();
		oneTimeChargeEntry.setPrice(EXPECTED_ONE_TIME_CHARGE);
		expectedSpp.setOneTimeChargeEntries(Sets.newHashSet(oneTimeChargeEntry));

		RecurringChargeEntryModel recurringChargeEntry = new RecurringChargeEntryModel();
		recurringChargeEntry.setPrice(EXPECTED_RECURRING_CHARGE);
		expectedSpp.setRecurringChargeEntries(Sets.newHashSet(recurringChargeEntry));

		return expectedSpp;
	}

}

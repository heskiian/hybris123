/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class SubscriptionBillingDataListPopulatorUnitTest
{

	private static final String QUARTERLY = "quarterly";
	private static final String MONTH = "month";
	private static final String YEARLY = "yearly";
	private static final String DEFAULT = "default";
	private static final String PRODUCT_CODE = "productCode";

	private TmaSubscribedProductData source;
	private List<SubscriptionBillingData> target;
	private SubscriptionBillingDataListPopulator populator;

	@Mock
	private ProductService productService;

	@Mock
	private TmaCommercePriceService tmaCommercePriceService;

	@Mock
	private Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> converter;

	@Before
	public void setUp() throws ParseException
	{
		initMocks(this);
		populator = new SubscriptionBillingDataListPopulator();
		populator.setProductService(productService);
		populator.setCommercePriceService(tmaCommercePriceService);
		populator.setSubscriptionPricePlanRecurringChargeConverter(converter);

		final SubscriptionPricePlanModel subscriptionPricePlanModel = mock(SubscriptionPricePlanModel.class);
		when(tmaCommercePriceService.getMinimumPrice(any(TmaPriceContext.class))).thenReturn(subscriptionPricePlanModel);
	}

	@Test
	public void testPopulate()
	{
		whenPopulateIsRun();
	}

	private void testDataPopulated()
	{
		assertNotNull(target.get(0).getPaymentAmount());
		assertNotNull(target.get(0).getPaymentStatus());
		assertNotNull(target.get(0).getBillingId());
		assertNotNull(target.get(0).getBillingDate());
		assertNotNull(target.get(0).getBillingPeriod());
	}

	private void whenPopulateIsRun()
	{

		setUpSource(QUARTERLY, true, 1, -1);
		populator.populate(source, target);
		testDataPopulated();

		setUpSource(MONTH, false, 10, -1);
		populator.populate(source, target);
		testDataPopulated();

		setUpSource(YEARLY, true, 1, -2);
		populator.populate(source, target);
		testDataPopulated();

		setUpSource(DEFAULT, false, 1, 1);
		populator.populate(source, target);
		testDataPopulated();

	}

	@Test(expected = NullPointerException.class)
	public void whenBillingFrequencyIsNull()
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setBillingFrequency(null);
		populator.populate(source, target);
	}

	@Test(expected = NullPointerException.class)
	public void whenContractFrequencyIsNull()
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setContractFrequency(null);
		populator.populate(source, target);
	}

	@Test(expected = NullPointerException.class)
	public void whenContractDurationIsNull()
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setBillingFrequency(QUARTERLY);
		source.setContractFrequency(QUARTERLY);
		source.setContractDuration(null);
		populator.populate(source, target);
	}

	@Test
	public void whenRecurringChargeNull()
	{
		setUpRecurringChargeNull();
		populator.populate(source, target);
		testDataPopulated();
		assertEquals("$0.00", target.get(0).getPaymentAmount());
	}

	@Test
	public void whenPriceIsNull()
	{
		setUpPriceNull();
		populator.populate(source, target);
		testDataPopulated();
		assertEquals("$0.00", target.get(0).getPaymentAmount());
	}

	private void setUpSource(final String frequency, final boolean isSetBillingToPaid, final int cycleStart, final int cycleEnd)
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setBillingFrequency(frequency);
		source.setContractFrequency(frequency);
		source.setProductCode(PRODUCT_CODE);
		final SubscriptionPricePlanData pricePlanData = mock(SubscriptionPricePlanData.class);
		final RecurringChargeEntryData recurringCharge = mock(RecurringChargeEntryData.class);
		given(recurringCharge.getPrice()).willReturn(pricePlanData);
		if (isSetBillingToPaid)
		{
			source.setStartDate(Date.from(Instant.now().minus(Duration.ofDays(300))));
		}
		else
		{
			source.setStartDate(new Date());
		}
		given(recurringCharge.getCycleStart()).willReturn(Integer.valueOf(cycleStart));
		given(recurringCharge.getCycleEnd()).willReturn(Integer.valueOf(cycleEnd));
		final List<RecurringChargeEntryData> recurringChargeEntries = new ArrayList();
		recurringChargeEntries.add(recurringCharge);
		given(pricePlanData.getRecurringChargeEntries()).willReturn(recurringChargeEntries);
		source.setContractDuration(1);
		given(pricePlanData.getFormattedValue()).willReturn("$100.00");
	}

	private void setUpRecurringChargeNull()
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setBillingFrequency(QUARTERLY);
		source.setContractFrequency(QUARTERLY);
		source.setProductCode(PRODUCT_CODE);
		source.setContractDuration(1);
		source.setStartDate(new Date());
		final SubscriptionPricePlanData pricePlanData = mock(SubscriptionPricePlanData.class);
		given(pricePlanData.getRecurringChargeEntries()).willReturn(null);
	}

	private void setUpPriceNull()
	{
		target = new ArrayList<>();
		source = new TmaSubscribedProductData();
		source.setBillingFrequency(QUARTERLY);
		source.setContractFrequency(QUARTERLY);
		source.setProductCode(PRODUCT_CODE);
		source.setContractDuration(1);
		source.setStartDate(new Date());
		final SubscriptionPricePlanData priceData = mock(SubscriptionPricePlanData.class);
		final RecurringChargeEntryData recurringCharge = mock(RecurringChargeEntryData.class);
		given(recurringCharge.getPrice()).willReturn(null);
		given(recurringCharge.getCycleStart()).willReturn(1);
		given(recurringCharge.getCycleEnd()).willReturn(-1);
		final List<RecurringChargeEntryData> recurringChargeEntries = new ArrayList<>();
		recurringChargeEntries.add(recurringCharge);
		given(priceData.getRecurringChargeEntries()).willReturn(recurringChargeEntries);
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.order.impl.BillingTimeComparator;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class OrderHistoryBillingDetailPopulatorTest
{

	@Mock
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;

	@Mock
	private BillingTimeComparator billingTimeComparator;

	@Mock
	private PriceDataFactory priceDataFactory;
	@InjectMocks
	private final OrderHistoryBillingDetailPopulator orderhistoryBillingPopulator = new OrderHistoryBillingDetailPopulator();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		orderhistoryBillingPopulator.setBillingTimeComparator(billingTimeComparator);
		orderhistoryBillingPopulator.setBillingTimeConverter(billingTimeConverter);
	}

	@Test
	public void testBuildOrderPrices()
	{
		final BillingTimeModel billingTimeModel1 = mock(BillingTimeModel.class);
		final BillingTimeData billingTimeData = mock(BillingTimeData.class);
		final AbstractOrderModel abstractOrderModel = mock(AbstractOrderModel.class);
		final BillingTimeModel billingTimeModel2 = mock(BillingTimeModel.class);
		final BillingTimeModel billingTimeModel3 = mock(BillingTimeModel.class);
		final OrderHistoryData OrderHistoryData = mock(OrderHistoryData.class);
		final Collection<AbstractOrderModel> abstractmodelList = new ArrayList<>();
		final CurrencyModel currencyModel = mock(CurrencyModel.class);
		given(abstractOrderModel.getBillingTime()).willReturn(billingTimeModel3);
		given(abstractOrderModel.getCurrency()).willReturn(currencyModel);
		given(abstractOrderModel.getTotalPrice()).willReturn(new Double(10));
		given(abstractOrderModel.getTotalTax()).willReturn(new Double(20));
		given(abstractOrderModel.getSubtotal()).willReturn(new Double(40));
		given(abstractOrderModel.getDeliveryCost()).willReturn(new Double(30));
		abstractmodelList.add(abstractOrderModel);
		given(abstractOrderModel.getChildren()).willReturn(abstractmodelList);
		given(billingTimeData.getCode()).willReturn("PAY");
		given(billingTimeModel3.getCode()).willReturn("PAY");
		given(billingTimeConverter.convert(billingTimeModel3)).willReturn(billingTimeData);
		given(billingTimeModel1.getOrder()).willReturn(new Integer(4));
		given(billingTimeModel2.getOrder()).willReturn(new Integer(5));
		Assert.assertTrue(new BillingTimeComparator().compare(billingTimeModel1, billingTimeModel2) < 0);
		orderhistoryBillingPopulator.buildBillingTimes(abstractOrderModel);
		orderhistoryBillingPopulator.buildOrderPrices(abstractOrderModel, OrderHistoryData);

	}

	@Test
	public void testCreatePrice()
	{
		final AbstractOrderModel abstractOrderModel = mock(AbstractOrderModel.class);
		final Double val = new Double(10);
		final CurrencyModel currencyModel = mock(CurrencyModel.class);
		final PriceData priceData = mock(PriceData.class);
		given(currencyModel.getIsocode()).willReturn("isoCode");
		given(abstractOrderModel.getCurrency()).willReturn(currencyModel);
		given(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(3.4), currencyModel)).willReturn(priceData);
		orderhistoryBillingPopulator.createPrice(abstractOrderModel, val);
	}

}
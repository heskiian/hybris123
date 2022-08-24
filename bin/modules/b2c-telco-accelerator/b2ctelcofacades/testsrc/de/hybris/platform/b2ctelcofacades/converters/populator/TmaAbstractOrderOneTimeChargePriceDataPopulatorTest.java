/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderOneTimeChargePriceData;
import de.hybris.platform.b2ctelcofacades.price.converters.TmaAbstractOrderOneTimeChargePriceDataPopulator;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.testframework.Assert;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test suite for {@link TmaAbstractOrderOneTimeChargePriceDataPopulator}
 *
 * @since 1907
 */
@UnitTest
public class TmaAbstractOrderOneTimeChargePriceDataPopulatorTest
{
	private static final String ID = "testId";
	private static final String CURRENCY_ISO = "USD";
	private static final String PRODUCT_OFFERING_PRICE_ID = "testPop";

	@Mock
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;
	@Mock
	private PriceDataFactory priceDataFactory = new DefaultPriceDataFactory();

	@InjectMocks
	private TmaAbstractOrderOneTimeChargePriceDataPopulator<TmaAbstractOrderOneTimeChargePriceModel,
			TmaAbstractOrderOneTimeChargePriceData> oneTimeChargeEntryPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		oneTimeChargeEntryPopulator = new TmaAbstractOrderOneTimeChargePriceDataPopulator<>(billingTimeConverter, priceDataFactory);
	}

	@Test
	public void testPopulate()
	{
		final TmaAbstractOrderOneTimeChargePriceModel source = new TmaAbstractOrderOneTimeChargePriceModel();
		final BillingTimeModel billingTime = mock(BillingTimeModel.class);
		final BillingTimeData billingTimeData = mock(BillingTimeData.class);
		final CurrencyModel currencyModel = new CurrencyModel();
		final PriceData priceData = mock(PriceData.class);
		final TmaAbstractOrderOneTimeChargePriceData target = new TmaAbstractOrderOneTimeChargePriceData();

		source.setId(ID);
		source.setBillingTime(billingTime);
		source.setDutyFreeAmount(10.0);
		source.setTaxIncludedAmount(20.0);
		source.setTaxRate(10.0);
		currencyModel.setIsocode(CURRENCY_ISO);
		source.setCurrency(currencyModel);
		source.setCatalogCode(PRODUCT_OFFERING_PRICE_ID);

		when(billingTimeConverter.convert(billingTime)).thenReturn(billingTimeData);
		when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getDutyFreeAmount()),
				currencyModel)).thenReturn(priceData);
		when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getTaxIncludedAmount()),
				currencyModel)).thenReturn(priceData);
		when(priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getTaxRate()),
				currencyModel)).thenReturn(priceData);

		oneTimeChargeEntryPopulator.populate(source, target);

		Assert.assertEquals(ID, target.getId());
		Assert.assertEquals(billingTimeData, target.getBillingTime());
		Assert.assertEquals(PRODUCT_OFFERING_PRICE_ID, target.getPriceId());

		Assert.assertEquals(priceData, target.getDutyFreeAmount());
		Assert.assertEquals(priceData.getValue(), target.getDutyFreeAmount().getValue());
		Assert.assertEquals(priceData, target.getTaxIncludedAmount());
		Assert.assertEquals(priceData.getValue(), target.getTaxIncludedAmount().getValue());
		Assert.assertEquals(priceData, target.getTaxRate());
		Assert.assertEquals(priceData.getValue(), target.getTaxRate().getValue());
	}
}

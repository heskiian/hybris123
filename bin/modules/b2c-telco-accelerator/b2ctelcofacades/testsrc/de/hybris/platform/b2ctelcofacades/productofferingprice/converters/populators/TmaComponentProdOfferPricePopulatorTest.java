/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaOneTimeProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaComponentProdOfferPricePopulator}.
 *
 * @since 2007.
 */
@UnitTest
public class TmaComponentProdOfferPricePopulatorTest
{
	private static final Double PRICE = 10.0;
	private static final String USD = "USD";

	@Mock
	private Converter<CurrencyModel, CurrencyData> currencyConverter;

	@Mock
	private Map<String, Converter<TmaPricingLogicAlgorithmModel, TmaPricingLogicAlgorithmData>> pricingLogicAlgorithmConverterMap;

	private TmaComponentProdOfferPricePopulator componentProdOfferPricePopulator;

	private TmaOneTimeProdOfferPriceChargeModel source;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		componentProdOfferPricePopulator = new TmaComponentProdOfferPricePopulator(currencyConverter,
				pricingLogicAlgorithmConverterMap);
		source = new TmaOneTimeProdOfferPriceChargeModel();
		setLocale();
		source.setValue(PRICE);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(USD);
		source.setCurrency(currency);
		final CurrencyData currencyData = new CurrencyData();
		currencyData.setIsocode(USD);
		when(currencyConverter.convert(currency)).thenReturn(currencyData);
	}

	@Test
	public void testPopulate()
	{
		final TmaOneTimeProdOfferPriceChargeData target = new TmaOneTimeProdOfferPriceChargeData();
		componentProdOfferPricePopulator.populate(source, target);
		Assert.assertEquals(PRICE, target.getValue());
		Assert.assertEquals(USD, target.getCurrency().getIsocode());
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}

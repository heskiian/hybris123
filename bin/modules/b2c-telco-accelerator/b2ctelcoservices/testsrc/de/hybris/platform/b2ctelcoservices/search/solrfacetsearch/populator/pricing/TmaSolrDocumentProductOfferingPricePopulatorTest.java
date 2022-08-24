/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.BillingFrequencyModel;

import java.util.Locale;

import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;


/**
 * Default Unit Test for the {@link TmaSolrDocumentProductOfferingPricePopulator}.
 *
 * @since 2007
 */
abstract class TmaSolrDocumentProductOfferingPricePopulatorTest
{
	protected static final String DEFAULT_ID = "default_fee";
	protected static final String DEFAULT_NAME = "Default Fee";
	protected static final String DEFAULT_DESCRIPTION = "Default fee of the Service";
	protected static final String DEFAULT_PRICE_EVENT = "PAY_NOW";
	protected static final String DEFAULT_BILLING_FREQUENCY = "monthly";
	protected static final String DEFAULT_CURRENCY_ISO_CODE = "USD";
	protected static final double DEFAULT_PRICE_VALUE = 30.0;
	protected static final int DEFAULT_CYCLE_START = 1;
	protected static final int DEFAULT_CYCLE_END = 12;

	protected final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);

	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	protected void assertPopFieldsArePopulatedCorrectly(final TmaSolrDocumentProductOfferingPrice solrDocumentPop)
	{
		assertEquals(DEFAULT_ID, solrDocumentPop.getId());
		assertEquals(DEFAULT_NAME, solrDocumentPop.getName());
		assertEquals(DEFAULT_DESCRIPTION, solrDocumentPop.getDescription());
	}

	protected TmaOneTimeProdOfferPriceChargeModel getOtcPopWithoutPriceEvent()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = new TmaOneTimeProdOfferPriceChargeModel();
		setLocaleOnModel(otcPopModel);

		otcPopModel.setId(DEFAULT_ID);
		otcPopModel.setName(DEFAULT_NAME);
		otcPopModel.setDescription(DEFAULT_DESCRIPTION);
		otcPopModel.setCurrency(getCurrency());

		return otcPopModel;
	}

	protected <T extends TmaProductOfferingPriceModel> void setLocaleOnModel(T popModel)
	{
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) popModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaOneTimeProdOfferPriceChargeModel getOtcPopWithPriceEvent()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPopWithoutPriceEvent();
		otcPopModel.setPriceEvent(getPriceEvent());

		return otcPopModel;
	}

	protected BillingFrequencyModel getPriceFrequency()
	{
		final BillingFrequencyModel priceEvent = new BillingFrequencyModel();
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) priceEvent.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		priceEvent.setCode(DEFAULT_BILLING_FREQUENCY);
		return priceEvent;
	}


	protected BillingEventModel getPriceEvent()
	{
		final BillingEventModel priceEvent = new BillingEventModel();
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) priceEvent.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		priceEvent.setCode(DEFAULT_PRICE_EVENT);
		return priceEvent;
	}

	protected CurrencyModel getCurrency()
	{
		final CurrencyModel currency = new CurrencyModel();
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) currency.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		currency.setIsocode(DEFAULT_CURRENCY_ISO_CODE);
		currency.setName(DEFAULT_CURRENCY_ISO_CODE);
		return currency;
	}

}

/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link TmaPriceDataProductOfferingPricePopulator}.
 *
 * @since 2007
 */
@IntegrationTest
public class TmaPriceDataProductOfferingPricePopulatorTest extends ServicelayerTransactionalTest
{
	private static final String ID = "id_1";
	private static final String CODE = "code_1";
	private static final String BILLING_EVENT = "billing_event_1";
	private static final String USD = "USD";

	@Resource
	private TmaPriceDataProductOfferingPricePopulator tmaPriceDataProductOfferingPricePopulator;

	@Test
	public void testPopulateOtc()
	{
		final TmaOneTimeProdOfferPriceChargeModel oneTimeChargeSource = createOtcCharge();
		final PriceRowModel priceRowSource = new PriceRowModel();
		priceRowSource.setCode(CODE);
		priceRowSource.setProductOfferingPrice(oneTimeChargeSource);
		final PriceData priceDataTarget = new PriceData();
		tmaPriceDataProductOfferingPricePopulator.populate(priceRowSource, priceDataTarget);
		Assert.assertNotNull(priceDataTarget.getProductOfferingPrice());
		Assert.assertEquals(ID, priceDataTarget.getProductOfferingPrice().getId());
	}

	private TmaOneTimeProdOfferPriceChargeModel createOtcCharge()
	{
		final TmaOneTimeProdOfferPriceChargeModel oneTimeChargeSource = new TmaOneTimeProdOfferPriceChargeModel();
		setLocale(oneTimeChargeSource);
		oneTimeChargeSource.setId(ID);
		final BillingEventModel billingEvent = new BillingEventModel();
		billingEvent.setCode(BILLING_EVENT);
		setLocale(billingEvent);
		oneTimeChargeSource.setPriceEvent(billingEvent);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(USD);
		setLocale(currency);
		oneTimeChargeSource.setCurrency(currency);
		return oneTimeChargeSource;
	}

	private void setLocale(ItemModel input)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) input.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}

/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaOneTimeProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaProductOfferingPricePopulator}.
 *
 * @since 2007.
 */
@UnitTest
public class TmaProductOfferingPricePopulatorTest
{
	private static final String ID = "id_1";
	private static final String NAME = "name_1";
	private static final String DESCRIPTION = "description_1";
	private static final String BILLING_EVENT = "billing_event_1";
	private static final String PSCV = "pscv_1";

	@Mock
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;

	@Mock
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> productSpecCharacteristicValueUseConverter;

	private TmaProductOfferingPricePopulator productOfferingPricePopulator;

	private TmaOneTimeProdOfferPriceChargeModel source;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		productOfferingPricePopulator = new TmaProductOfferingPricePopulator(billingTimeConverter,
				productSpecCharacteristicValueUseConverter);
		source = new TmaOneTimeProdOfferPriceChargeModel();
		setLocale();
		source.setId(ID);
		source.setName(NAME);
		source.setDescription(DESCRIPTION);
		final BillingEventModel billingEvent = new BillingEventModel();
		billingEvent.setCode(BILLING_EVENT);
		final TmaProductSpecCharacteristicValueModel productSpecCharacteristicValue = new TmaProductSpecCharacteristicValueModel();
		productSpecCharacteristicValue.setId(PSCV);
		final Set<TmaProductSpecCharacteristicValueModel> pscvModels = new HashSet<>();
		pscvModels.add(productSpecCharacteristicValue);
		source.setProductSpecCharacteristicValues(pscvModels);
		source.setPriceEvent(billingEvent);
		final BillingTimeData billingTimeData = new BillingTimeData();
		final TmaProductSpecCharacteristicValueUseData productSpecCharacteristicValueUseData =
				new TmaProductSpecCharacteristicValueUseData();
		productSpecCharacteristicValueUseData.setName(PSCV);
		final List<TmaProductSpecCharacteristicValueUseData> pscvsData = new ArrayList<>();
		pscvsData.add(productSpecCharacteristicValueUseData);
		billingTimeData.setCode(BILLING_EVENT);
		when(billingTimeConverter.convert(billingEvent)).thenReturn(billingTimeData);
		when(productSpecCharacteristicValueUseConverter.convertAll(pscvModels))
				.thenReturn(pscvsData);
	}

	@Test
	public void testPopulate()
	{
		final TmaOneTimeProdOfferPriceChargeData target = new TmaOneTimeProdOfferPriceChargeData();
		productOfferingPricePopulator.populate(source, target);
		Assert.assertEquals(ID, target.getId());
		Assert.assertEquals(NAME, target.getName());
		Assert.assertEquals(DESCRIPTION, target.getDescription());
		Assert.assertEquals(BILLING_EVENT, target.getPriceEvent().getCode());
		Assert.assertEquals(PSCV, target.getProductSpecCharacteristicValueUses().get(0).getName());
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}

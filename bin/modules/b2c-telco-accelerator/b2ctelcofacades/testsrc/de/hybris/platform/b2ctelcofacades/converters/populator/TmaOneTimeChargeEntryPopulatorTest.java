/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaOneTimeChargeEntryPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaOneTimeChargeEntryPopulatorTest
{
	private static final String ID = "testId";

	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;

	private TmaOneTimeChargeEntryPopulator oneTimeChargeEntryPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		oneTimeChargeEntryPopulator = new TmaOneTimeChargeEntryPopulator();
		oneTimeChargeEntryPopulator.setCommonI18NService(commonI18NService);
		oneTimeChargeEntryPopulator.setBillingTimeConverter(billingTimeConverter);
		oneTimeChargeEntryPopulator.setPriceDataFactory(priceDataFactory);
	}

	@Test
	public void testPopulate()
	{
		final OneTimeChargeEntryModel source = mock(OneTimeChargeEntryModel.class);
		given(source.getId()).willReturn(ID);
		final OneTimeChargeEntryData target = new OneTimeChargeEntryData();
		oneTimeChargeEntryPopulator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getId());
	}
}

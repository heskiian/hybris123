/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaRecurringChargeEntryPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaRecurringChargeEntryPopulatorTest
{
	private static final String ID = "testId";

	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	private CommonI18NService commonI18NService;

	private TmaRecurringChargeEntryPopulator recurringChargeEntryPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		recurringChargeEntryPopulator = new TmaRecurringChargeEntryPopulator();
		recurringChargeEntryPopulator.setCommonI18NService(commonI18NService);
		recurringChargeEntryPopulator.setPriceDataFactory(priceDataFactory);
	}

	@Test
	public void testPopulate()
	{
		final RecurringChargeEntryModel source = mock(RecurringChargeEntryModel.class);
		given(source.getId()).willReturn(ID);
		final RecurringChargeEntryData target = new RecurringChargeEntryData();
		recurringChargeEntryPopulator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getId());
	}
}

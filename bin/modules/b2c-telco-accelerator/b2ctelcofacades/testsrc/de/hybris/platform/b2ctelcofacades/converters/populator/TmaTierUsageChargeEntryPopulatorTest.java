/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaTierUsageChargeEntryPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaTierUsageChargeEntryPopulatorTest
{
	private static final String ID = "testId";

	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	private CommonI18NService commonI18NService;

	private TmaTierUsageChargeEntryPopulator tierUsageChargeEntryPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		tierUsageChargeEntryPopulator = new TmaTierUsageChargeEntryPopulator();
		tierUsageChargeEntryPopulator.setCommonI18NService(commonI18NService);
		tierUsageChargeEntryPopulator.setPriceDataFactory(priceDataFactory);
	}

	@Test
	public void testPopulate()
	{
		final TierUsageChargeEntryModel source = mock(TierUsageChargeEntryModel.class);
		given(source.getId()).willReturn(ID);
		final TierUsageChargeEntryData target = new TierUsageChargeEntryData();
		tierUsageChargeEntryPopulator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getId());
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaOverageUsageChargeEntryPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaOverageUsageChargeEntryPopulatorTest
{
	private static final String ID = "testId";

	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	private CommonI18NService commonI18NService;

	private TmaOverageUsageChargeEntryPopulator overageUsageChargeEntryPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		overageUsageChargeEntryPopulator = new TmaOverageUsageChargeEntryPopulator();
		overageUsageChargeEntryPopulator.setCommonI18NService(commonI18NService);
		overageUsageChargeEntryPopulator.setPriceDataFactory(priceDataFactory);
	}

	@Test
	public void testPopulate()
	{
		final OverageUsageChargeEntryModel source = mock(OverageUsageChargeEntryModel.class);
		given(source.getId()).willReturn(ID);
		final OverageUsageChargeEntryData target = new OverageUsageChargeEntryData();
		overageUsageChargeEntryPopulator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getId());
	}
}

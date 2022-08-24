/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaRecurringProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Test suite for {@link TmaRecurringProdOfferPricePopulator}
 *
 * @since 2007
 */
@UnitTest
public class TmaRecurringProdOfferPricePopulatorTest
{
	private static final Integer CYCLE_START = 1;
	private static final Integer CYCLE_END = 6;
	private TmaRecurringProdOfferPricePopulator recurringPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		recurringPopulator = new TmaRecurringProdOfferPricePopulator();
	}

	@Test
	public void testPopulateCycleStart()
	{
		final TmaRecurringProdOfferPriceChargeModel source = mock(TmaRecurringProdOfferPriceChargeModel.class);

		given(source.getCycleStart()).willReturn(CYCLE_START);

		final TmaRecurringProdOfferPriceChargeData target = new TmaRecurringProdOfferPriceChargeData();

		recurringPopulator.populate(source, target);

		Assert.assertEquals(CYCLE_START, target.getCycleStart());
	}

	@Test
	public void testPopulateCycleStartAndEnd()
	{
		final TmaRecurringProdOfferPriceChargeModel source = mock(TmaRecurringProdOfferPriceChargeModel.class);

		given(source.getCycleStart()).willReturn(CYCLE_START);
		given(source.getCycleEnd()).willReturn(CYCLE_END);

		final TmaRecurringProdOfferPriceChargeData target = new TmaRecurringProdOfferPriceChargeData();

		recurringPopulator.populate(source, target);

		Assert.assertEquals(CYCLE_START, target.getCycleStart());
		Assert.assertEquals(CYCLE_END, target.getCycleEnd());
	}
}

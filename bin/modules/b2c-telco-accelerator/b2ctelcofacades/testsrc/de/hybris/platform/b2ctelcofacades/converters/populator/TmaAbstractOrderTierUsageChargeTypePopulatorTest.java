/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderTierUsageCompositePriceData;
import de.hybris.platform.b2ctelcofacades.price.converters.TmaAbstractOrderTierUsageChargeTypePopulator;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderTierUsageCompositePriceModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaAbstractOrderTierUsageChargeTypePopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaAbstractOrderTierUsageChargeTypePopulatorTest
{
	private static final String CHARGE_TYPE_CODE = "each_respective_tier";
	private static final UsageChargeType usageChargeType = UsageChargeType.EACH_RESPECTIVE_TIER;

	private TmaAbstractOrderTierUsageChargeTypePopulator testPopulator;
	@Mock
	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		testPopulator = new TmaAbstractOrderTierUsageChargeTypePopulator(usageChargeTypeConverter);
		final UsageChargeTypeData usageChargeTypeData = new UsageChargeTypeData();
		usageChargeTypeData.setCode(CHARGE_TYPE_CODE);
		when(usageChargeTypeConverter.convert(usageChargeType)).thenReturn(usageChargeTypeData);
	}

	@Test
	public void testPopulate()
	{
		final TmaAbstractOrderTierUsageCompositePriceModel source = new TmaAbstractOrderTierUsageCompositePriceModel();
		source.setChildPrices(new HashSet<>());
		source.setUsageChargeType(usageChargeType);
		final TmaAbstractOrderTierUsageCompositePriceData target = new TmaAbstractOrderTierUsageCompositePriceData();
		testPopulator.populate(source, target);
		Assert.assertNotNull("usageChargeType should be populated ", target.getUsageChargeType());
		Assert.assertEquals("usageChargeType should match", CHARGE_TYPE_CODE, target.getUsageChargeType().getCode());
	}

}

/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaUsageProdOfferPricePopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaUsageProdOfferPricePopulatorTest
{
	private static final Integer TIER_START = 1;
	private static final Integer TIER_END = 10;
	private static final String USAGE_UNIT_ID = "usageUnitId1";
	private static final String USAGE_SPECIFICATION_ID = "usageSpecification1";

	@Mock
	private Converter<UsageUnitModel, UsageUnitData> usageUnitConverter;

	@Mock
	private Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> productUsageSpecificationConverter;

	private TmaUsageProdOfferPricePopulator usageProdOfferPricePopulator;

	private TmaUsageProdOfferPriceChargeModel source;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		usageProdOfferPricePopulator = new TmaUsageProdOfferPricePopulator(usageUnitConverter, productUsageSpecificationConverter);
		source = new TmaUsageProdOfferPriceChargeModel();
		setLocale();
		source.setTierStart(TIER_START);
		source.setTierEnd(TIER_END);
		final UsageUnitModel usageUnit = new UsageUnitModel();
		usageUnit.setId(USAGE_UNIT_ID);
		source.setUsageUnit(usageUnit);
		final UsageUnitData usageUnitData = new UsageUnitData();
		usageUnitData.setId(USAGE_UNIT_ID);
		when(usageUnitConverter.convert(usageUnit)).thenReturn(usageUnitData);
		final TmaProductUsageSpecificationModel usageSpec = new TmaProductUsageSpecificationModel();
		usageSpec.setId(USAGE_SPECIFICATION_ID);
		source.setProductUsageSpec(usageSpec);
		final TmaProductUsageSpecificationData usageSpecData = new TmaProductUsageSpecificationData();
		usageSpecData.setId(USAGE_SPECIFICATION_ID);
		when(productUsageSpecificationConverter.convert(usageSpec)).thenReturn(usageSpecData);
	}

	@Test
	public void testPopulate()
	{
		final TmaUsageProdOfferPriceChargeData target = new TmaUsageProdOfferPriceChargeData();
		usageProdOfferPricePopulator.populate(source, target);
		Assert.assertEquals(TIER_START, target.getTierStart());
		Assert.assertEquals(TIER_END, target.getTierEnd());
		Assert.assertEquals(USAGE_UNIT_ID, target.getUsageUnit().getId());
		Assert.assertEquals(USAGE_SPECIFICATION_ID, target.getProductUsageSpecification().getId());
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}

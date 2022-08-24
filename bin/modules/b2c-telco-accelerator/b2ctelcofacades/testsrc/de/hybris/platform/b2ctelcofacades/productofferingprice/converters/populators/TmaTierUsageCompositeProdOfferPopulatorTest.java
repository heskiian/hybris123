/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaTierUsageChargeCompositePopModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaTierUsageCompositeProdOfferPopulator}.
 *
 * @since 2007.
 */
@UnitTest
public class TmaTierUsageCompositeProdOfferPopulatorTest
{
	private final static String USAGE_CHARGE_TYPE_CODE = "each_respective_tier";
	@Mock
	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	private TmaTierUsageCompositeProdOfferPopulator tierUsageCompositeProdOfferPopulator;

	private TmaTierUsageChargeCompositePopModel source;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		tierUsageCompositeProdOfferPopulator = new TmaTierUsageCompositeProdOfferPopulator(usageChargeTypeConverter);
		source = new TmaTierUsageChargeCompositePopModel();
		setLocale();
		final UsageChargeType usageChargeType = UsageChargeType.EACH_RESPECTIVE_TIER;
		source.setUsageChargeType(usageChargeType);
		final UsageChargeTypeData usageChargeTypeData = new UsageChargeTypeData();
		usageChargeTypeData.setCode(USAGE_CHARGE_TYPE_CODE);
		when(usageChargeTypeConverter.convert(usageChargeType)).thenReturn(usageChargeTypeData);
	}

	@Test
	public void testPopulate()
	{
		final TmaTierUsageCompositeProdOfferPriceData target = new TmaTierUsageCompositeProdOfferPriceData();
		tierUsageCompositeProdOfferPopulator.populate(source, target);
		Assert.assertEquals(USAGE_CHARGE_TYPE_CODE, target.getUsageChargeType().getCode());
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}

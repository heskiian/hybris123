/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaOneTimeProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcoservices.model.*;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.BillingFrequencyModel;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link TmaCompositeProdOfferPricePopulator}.
 *
 * @since 2007.
 */
@IntegrationTest
public class TmaCompositeProdOfferPricePopulatorTest extends ServicelayerTransactionalTest
{

	private static final String ID = "id_1";
	private static final String COMPOSITE_ID = "composite_id_1";
	private static final String NAME = "name_1";
	private static final String DESCRIPTION = "description_1";
	private static final String BILLING_EVENT = "paynow";
	private static final String BILLING_FREQUENCY_EVENT = "monthly";
	private static final Integer TIER_START = 1;
	private static final Integer TIER_END = 10;
	private static final String USAGE_UNIT_ID = "usageUnitId1";
	private static final String USAGE_CHARGE_ID = "usage_charge_id_1";
	private static final String TIER_USAGE_CHARGE_ID = "tier_usage_charge_id_1";
	private static final String CURRENCY_ISO = "USD";

	private TmaCompositeProdOfferPricePopulator compositeProdOfferPricePopulator;
	@Resource
	private Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap;
	private TmaCompositeProdOfferPriceModel source;

	@Before
	public void setUp()
	{
		compositeProdOfferPricePopulator = new TmaCompositeProdOfferPricePopulator(productOfferingPriceConverterMap);
		source = new TmaCompositeProdOfferPriceModel();
		source.setChildren(new HashSet<>());
		source.setId(COMPOSITE_ID);
		setLocale(source);
		setBillingFrequency(source);
		addOTC();
		addUsageCharges();
	}

	@Test
	public void testPopulate()
	{
		final TmaCompositeProdOfferPriceData target = new TmaCompositeProdOfferPriceData();
		compositeProdOfferPricePopulator.populate(source, target);
		Set<TmaProductOfferingPriceData> children = target.getChildren();
		Assert.assertNotNull(children);
		Assert.assertEquals(2, children.size());
		TmaProductOfferingPriceData otcData =
				children.stream().filter(charge -> charge instanceof TmaOneTimeProdOfferPriceChargeData).findFirst().get();
		Assert.assertEquals(ID, otcData.getId());
		TmaTierUsageCompositeProdOfferPriceData usageData =
				(TmaTierUsageCompositeProdOfferPriceData) children.stream()
						.filter(charge -> charge instanceof TmaTierUsageCompositeProdOfferPriceData).findFirst().get();
		Assert.assertEquals(TIER_USAGE_CHARGE_ID, usageData.getId());
		Assert.assertEquals(1, usageData.getChildren().size());
		Assert.assertEquals(USAGE_CHARGE_ID, usageData.getChildren().stream().iterator().next().getId());
	}

	private void addOTC()
	{
		final TmaOneTimeProdOfferPriceChargeModel otc = new TmaOneTimeProdOfferPriceChargeModel();
		setLocale(otc);
		otc.setId(ID);
		otc.setName(NAME);
		otc.setDescription(DESCRIPTION);
		setBillingEvent(otc);
		setCurrency(otc);
		source.getChildren().add(otc);
	}

	private void addUsageCharges()
	{
		final TmaUsageProdOfferPriceChargeModel usageCharge = new TmaUsageProdOfferPriceChargeModel();
		setLocale(usageCharge);
		usageCharge.setTierStart(TIER_START);
		usageCharge.setTierEnd(TIER_END);
		final UsageUnitModel usageUnit = new UsageUnitModel();
		usageUnit.setId(USAGE_UNIT_ID);
		setLocale(usageUnit);
		usageCharge.setUsageUnit(usageUnit);
		usageCharge.setId(USAGE_CHARGE_ID);
		setBillingFrequency(usageCharge);
		setCurrency(usageCharge);

		final TmaTierUsageChargeCompositePopModel tierUsageChargeComposite = new TmaTierUsageChargeCompositePopModel();
		tierUsageChargeComposite.setId(TIER_USAGE_CHARGE_ID);
		setLocale(tierUsageChargeComposite);
		final UsageChargeType usageChargeType = UsageChargeType.EACH_RESPECTIVE_TIER;
		tierUsageChargeComposite.setUsageChargeType(usageChargeType);
		tierUsageChargeComposite.setChildren(new HashSet<>());
		tierUsageChargeComposite.getChildren().add(usageCharge);
		setBillingFrequency(tierUsageChargeComposite);
		source.getChildren().add(tierUsageChargeComposite);
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	private void setBillingEvent(TmaProductOfferingPriceModel pop)
	{
		final BillingEventModel billingEvent = new BillingEventModel();
		billingEvent.setCode(BILLING_EVENT);
		setLocale(billingEvent);
		pop.setPriceEvent(billingEvent);
	}

	private void setBillingFrequency(TmaProductOfferingPriceModel pop)
	{
		final BillingFrequencyModel billingFrequency = new BillingFrequencyModel();
		billingFrequency.setCode(BILLING_FREQUENCY_EVENT);
		setLocale(billingFrequency);
		pop.setPriceEvent(billingFrequency);
	}

	private void setCurrency(TmaComponentProdOfferPriceModel componentPop)
	{
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(CURRENCY_ISO);
		setLocale(currency);
		componentPop.setCurrency(currency);
	}

}

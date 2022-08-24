/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link UpilUsageChargeEntryPopulator}
 * 
 * @since 1911
 */

@UnitTest
public class UpilUsageChargeEntryPopulatorUnitTest
{
	private static final String SPP_CODE = "test";
	private static final String CURRENCY_CODE = "USD";
	private static final Double PRICE = 24.00;
	private static final String ID = "electric_plan_basic_power_101_200";
	public static final String UTILS_PRICE_TIER_TYPE_SCALE = "2";
	public static final String UTILS_PRICE_TIER_TYPE_BLOCK = "1";
	public static final String UTILS_PRICE_TIER_TYPE_STANDARD = "0";
	public static final String SEMANTIC_NAME1 = "Semantic1";
	public static final String SEMANTIC_NAME2 = "Semantic2";

	private UpilUsageChargeEntryPopulator populator;
	private UsageChargeEntryModel source;
	private I_UtilsProdChgUsageType target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new UpilUsageChargeEntryPopulator();
	}

	@Test
	public void testPopulate()
	{
		setData();
		setCodeData();
		setCurrency(false);
		setSemantics(false);
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
	}

	@Test
	public void testPopulateForPerUnitUsageCharge()
	{
		setData();
		setSemantics(true);
		setPerUnitUsageChargeData(true);
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(UTILS_PRICE_TIER_TYPE_SCALE, target.getUtilsPriceTierType());

		setData();
		setPerUnitUsageChargeData(false);
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(UTILS_PRICE_TIER_TYPE_BLOCK, target.getUtilsPriceTierType());
	}

	@Test
	public void testPopulateCurrencyNull()
	{
		setData();
		setCodeData();
		setCurrency(true);
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(null, target.getUtilsPriceCurrency());
	}

	@Test
	public void testTierUsageChargeEntryPopulate()
	{
		setTierUsageChargeEntryData();
		setCodeData();
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
		Assert.assertEquals(((TierUsageChargeEntryModel) source).getTierStart().toString(), target.getUtilsPriceFromBlock());
		Assert.assertEquals(((TierUsageChargeEntryModel) source).getTierEnd().toString(), target.getUtilsPriceToBlock());
	}

	@Test
	public void testOverageUsageChargeEntryPopulate()
	{
		setOverageUsageChargeEntryData(false);
		setCodeData();
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
		Assert.assertEquals(((OverageUsageChargeEntryModel) source).getTierStart().toString(), target.getUtilsPriceFromBlock());
		Assert.assertEquals(((OverageUsageChargeEntryModel) source).getTierEnd().toString(), target.getUtilsPriceToBlock());
	}

	@Test
	public void testOverageUsageChargeEntryTiersNull()
	{
		setOverageUsageChargeEntryData(true);
		setCodeData();
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
		Assert.assertEquals(null, target.getUtilsPriceFromBlock());
		Assert.assertEquals(null, target.getUtilsPriceToBlock());
	}

	@Test
	public void testPopulateForVolumeUsageCharge()
	{
		setData();
		setVolumeUsageChargeData();
		populator.populate(source, target);
		Assert.assertEquals(UTILS_PRICE_TIER_TYPE_STANDARD, target.getUtilsPriceTierType());
	}

	private void setData()
	{
		source = mock(OverageUsageChargeEntryModel.class);
		given(source.getId()).willReturn(ID);
		target = new I_UtilsProdChgUsageType();
	}

	private void setCodeData()
	{
		final SubscriptionPricePlanModel subscriptionPricePlan = mock(SubscriptionPricePlanModel.class);
		given(subscriptionPricePlan.getCode()).willReturn(SPP_CODE);

		final UsageChargeModel usageCharge = mock(UsageChargeModel.class);
		given(usageCharge.getSubscriptionPricePlanUsage()).willReturn(subscriptionPricePlan);

		given(source.getUsageCharge()).willReturn(usageCharge);
	}

	private void setCurrency(final boolean isCurrencyNull)
	{
		final CurrencyModel currency = mock(CurrencyModel.class);
		given(currency.getIsocode()).willReturn(CURRENCY_CODE);

		given(source.getPrice()).willReturn(PRICE);
		if (isCurrencyNull)
		{
			given(source.getCurrency()).willReturn(null);
		}
		else
		{
			given(source.getCurrency()).willReturn(currency);
		}
	}

	private void setTierUsageChargeEntryData()
	{
		source = mock(TierUsageChargeEntryModel.class);
		setCodeData();

		final CurrencyModel currency = mock(CurrencyModel.class);
		given(currency.getIsocode()).willReturn(CURRENCY_CODE);

		given(source.getId()).willReturn(ID);
		given(source.getPrice()).willReturn(PRICE);
		given(source.getCurrency()).willReturn(currency);
		given(((TierUsageChargeEntryModel) source).getTierStart()).willReturn(0);
		given(((TierUsageChargeEntryModel) source).getTierEnd()).willReturn(3);

		target = new I_UtilsProdChgUsageType();
	}

	private void setOverageUsageChargeEntryData(final boolean isTiersNull)
	{
		source = mock(OverageUsageChargeEntryModel.class);
		setCodeData();
		final CurrencyModel currency = mock(CurrencyModel.class);
		given(currency.getIsocode()).willReturn(CURRENCY_CODE);

		given(source.getId()).willReturn(ID);
		given(source.getPrice()).willReturn(PRICE);
		given(source.getCurrency()).willReturn(currency);
		if (isTiersNull)
		{
			given(((OverageUsageChargeEntryModel) source).getTierStart()).willReturn(null);
			given(((OverageUsageChargeEntryModel) source).getTierEnd()).willReturn(null);
		}
		else
		{
			given(((OverageUsageChargeEntryModel) source).getTierStart()).willReturn(0);
			given(((OverageUsageChargeEntryModel) source).getTierEnd()).willReturn(4);
		}

		target = new I_UtilsProdChgUsageType();
	}

	private void setPerUnitUsageChargeData(final boolean is_highest_applicable_tier)
	{
		setData();
		final PerUnitUsageChargeModel usageCharge = mock(PerUnitUsageChargeModel.class);
		final SubscriptionPricePlanModel subscriptionPricePlanRecurring = mock(SubscriptionPricePlanModel.class);
		given(usageCharge.getSubscriptionPricePlanUsage()).willReturn(subscriptionPricePlanRecurring);
		given(usageCharge.getUsageChargeType()).willReturn(
				is_highest_applicable_tier ? UsageChargeType.HIGHEST_APPLICABLE_TIER : UsageChargeType.EACH_RESPECTIVE_TIER);
		given(source.getUsageCharge()).willReturn(usageCharge);
	}

	private void setVolumeUsageChargeData()
	{
		setData();
		final VolumeUsageChargeModel usageCharge = mock(VolumeUsageChargeModel.class);
		final SubscriptionPricePlanModel subscriptionPricePlanRecurring = mock(SubscriptionPricePlanModel.class);
		given(usageCharge.getSubscriptionPricePlanUsage()).willReturn(subscriptionPricePlanRecurring);
		given(source.getUsageCharge()).willReturn(usageCharge);
	}

	private void setSemantics(final boolean isSemanticsNull)
	{
		final UpilSemanticsModel semantics = mock(UpilSemanticsModel.class);

		if (isSemanticsNull)
		{
			given(source.getSemantics()).willReturn(null);
		}
		else
		{
			given(semantics.getSemanticsName1()).willReturn(SEMANTIC_NAME1);
			given(semantics.getSemanticsName2()).willReturn(SEMANTIC_NAME2);
			given(source.getSemantics()).willReturn(semantics);
		}
	}
}

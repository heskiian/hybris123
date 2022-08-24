/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link UpilOneTimeChargesPopulator}
 * 
 * @since 1911
 */

@UnitTest
public class UpilOneTimeChargesPopulatorUnitTest
{
	private static final String SPP_CODE = "test";
	private static final String CURRENCY_CODE = "USD";
	private static final Double PRICE = 34.00;
	public static final String SEMANTIC_NAME1 = "Semantic1";
	public static final String SEMANTIC_NAME2 = "Semantic2";

	private UpilOneTimeChargesPopulator populator;
	private OneTimeChargeEntryModel source;
	private I_UtilsProdChgOneTimeType target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new UpilOneTimeChargesPopulator();
	}

	@Test
	public void testPopulate()
	{
		setData();
		setCurrency(false);
		setSemantics(false);
		populator.populate(source, target);
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
	}

	@Test
	public void testPopulateCurrencyNull()
	{
		setData();
		setCurrency(true);
		setSemantics(true);
		populator.populate(source, target);
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(null, target.getUtilsPriceCurrency());
	}

	private void setData()
	{
		source = mock(OneTimeChargeEntryModel.class);

		final SubscriptionPricePlanModel subscriptionPricePlan = mock(SubscriptionPricePlanModel.class);
		given(subscriptionPricePlan.getCode()).willReturn(SPP_CODE);
		given(source.getSubscriptionPricePlanOneTime()).willReturn(subscriptionPricePlan);

		target = new I_UtilsProdChgOneTimeType();
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

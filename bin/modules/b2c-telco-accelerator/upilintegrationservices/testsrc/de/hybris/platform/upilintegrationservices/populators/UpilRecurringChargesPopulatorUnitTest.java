/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.subscriptionservices.model.BillingFrequencyModel;
import de.hybris.platform.subscriptionservices.model.BillingPlanModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link UpilRecurringChargesPopulator}
 *
 * @since 1911
 */

@UnitTest
public class UpilRecurringChargesPopulatorUnitTest
{
	private static final String SPP_CODE = "test";
	private static final String CURRENCY_CODE = "USD";
	private static final Double PRICE = 24.00;
	private static final String ID = "rce_gas_plan_basic";
	public static final String SEMANTIC_NAME1 = "Semantic1";
	public static final String SEMANTIC_NAME2 = "Semantic2";

	private UpilRecurringChargesPopulator populator;
	private RecurringChargeEntryModel source;
	private I_UtilsProdChgRecurringType target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new UpilRecurringChargesPopulator();
	}

	@Test
	public void testPopulate()
	{
		setData();
		setCurrency(false);
		setSemantics(false);
		setBillingFrequencyData();
		populator.populate(source, target);
		Assert.assertEquals(source.getId(), target.getUtilsPriceID());
		Assert.assertEquals(source.getId(), target.getUtilsPriceName());
		Assert.assertEquals(BigDecimal.valueOf(source.getPrice()), target.getUtilsPriceAmount());
		Assert.assertEquals(source.getCurrency().getIsocode(), target.getUtilsPriceCurrency());
	}

	@Test
	public void testPopulateCurrencyAndSemanticsNull()
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
		source = mock(RecurringChargeEntryModel.class);

		final SubscriptionPricePlanModel subscriptionPricePlan = mock(SubscriptionPricePlanModel.class);
		given(subscriptionPricePlan.getCode()).willReturn(SPP_CODE);
		given(source.getSubscriptionPricePlanRecurring()).willReturn(subscriptionPricePlan);

		target = new I_UtilsProdChgRecurringType();
	}

	private void setCurrency(final boolean isCurrencyNull)
	{
		final CurrencyModel currency = mock(CurrencyModel.class);
		given(currency.getIsocode()).willReturn(CURRENCY_CODE);

		given(source.getId()).willReturn(ID);
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

	private void setBillingFrequencyData()
	{
		final BillingFrequencyModel billingFrequency = mock(BillingFrequencyModel.class);
		given(billingFrequency.getFrequencyUnit()).willReturn("1");
		given(billingFrequency.getFrequencyValue()).willReturn("2");

		final BillingPlanModel billingPlan = mock(BillingPlanModel.class);
		given(billingPlan.getBillingFrequency()).willReturn(billingFrequency);

		final SubscriptionTermModel subscriptionTerm = mock(SubscriptionTermModel.class);
		given(subscriptionTerm.getBillingPlan()).willReturn(billingPlan);

		final Set<SubscriptionTermModel> subscriptionTerms = new HashSet<>();
		subscriptionTerms.add(subscriptionTerm);

		final SubscriptionPricePlanModel subscriptionPricePlanRecurring = mock(SubscriptionPricePlanModel.class);
		given(subscriptionPricePlanRecurring.getSubscriptionTerms()).willReturn(subscriptionTerms);

		given(source.getSubscriptionPricePlanRecurring()).willReturn(subscriptionPricePlanRecurring);

	}

}

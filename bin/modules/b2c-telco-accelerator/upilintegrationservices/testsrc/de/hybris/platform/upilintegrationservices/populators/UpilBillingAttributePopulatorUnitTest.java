/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdBillingAttributeType;
import de.hybris.platform.upilintegrationservices.model.UpilAdditionalAttributesModel;
import de.hybris.platform.upilintegrationservices.model.UpilSemanticsModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link UpilBillingAttributePopulator}
 * 
 * @since 1911
 */

@UnitTest
public class UpilBillingAttributePopulatorUnitTest
{
	private static final String PRODUCT_ENTRY_VALUE = "test";
	public static final String SEMANTIC_NAME1 = "Semantic1";
	public static final String SEMANTIC_NAME2 = "Semantic2";
	private static final String SPP_CODE = "test";

	private UpilBillingAttributePopulator populator;
	private UpilAdditionalAttributesModel source;
	private I_UtilsProdBillingAttributeType target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new UpilBillingAttributePopulator();
	}

	@Test
	public void testPopulate()
	{
		setData();
		setSemantics(false);
		populator.populate(source, target);
		Assert.assertEquals(source.getUtilsProductEntryValue(), target.getUtilsProductEntryValue());
		Assert.assertEquals(source.getSemantics().getSemanticsName1(), target.getUtilsSemanticsName1());
		Assert.assertEquals(source.getSemantics().getSemanticsName2(), target.getUtilsSemanticsName2());
	}

	@Test
	public void testPopulateSemanticsNull()
	{
		setData();
		setSemantics(true);
		populator.populate(source, target);
		Assert.assertEquals(null, target.getUtilsSemanticsName1());
		Assert.assertEquals(null, target.getUtilsSemanticsName2());
	}

	private void setData()
	{
		source = mock(UpilAdditionalAttributesModel.class);
		given(source.getUtilsProductEntryValue()).willReturn(PRODUCT_ENTRY_VALUE);
		final SubscriptionPricePlanModel subscriptionPricePlan = mock(SubscriptionPricePlanModel.class);
		given(subscriptionPricePlan.getCode()).willReturn(SPP_CODE);
		given(source.getPricePlan()).willReturn(subscriptionPricePlan);
		target = new I_UtilsProdBillingAttributeType();
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

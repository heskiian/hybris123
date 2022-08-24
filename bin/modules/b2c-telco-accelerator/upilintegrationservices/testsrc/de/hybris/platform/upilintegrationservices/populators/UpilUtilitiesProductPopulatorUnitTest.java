/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdBillingAttributeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgOneTimeType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgUsageType;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdSalesAttributeType;
import de.hybris.platform.upilintegrationservices.enums.UpilAttributeType;
import de.hybris.platform.upilintegrationservices.model.UpilAdditionalAttributesModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link UpilUsageChargeEntryPopulator}
 * 
 * @since 1911
 */

@UnitTest
public class UpilUtilitiesProductPopulatorUnitTest
{
	public static final String UTILS_PRICE_TIER_TYPE_SCALE = "2";
	public static final String UTILS_PRICE_TIER_TYPE_BLOCK = "1";
	public static final String UTILS_PRICE_TIER_TYPE_STANDARD = "0";
	public static final String SEMANTIC_NAME1 = "Semantic1";
	public static final String SEMANTIC_NAME2 = "Semantic2";
	public static final String PRODUCT_SPEC_TYPE = "ISU_UIL_REFPROD";

	@Mock
	private Converter<UsageChargeEntryModel, I_UtilsProdChgUsageType> upilUsageChargeEntryConverter;
	@Mock
	private Converter<RecurringChargeEntryModel, I_UtilsProdChgRecurringType> upilRecurringChargesConverter;
	@Mock
	private Converter<OneTimeChargeEntryModel, I_UtilsProdChgOneTimeType> upilOneTimeChargesConverter;
	@Mock
	private Converter<UpilAdditionalAttributesModel, I_UtilsProdBillingAttributeType> upilBillingAttributeConverter;
	@Mock
	private Converter<UpilAdditionalAttributesModel, I_UtilsProdSalesAttributeType> upilSalesAttributeConverter;

	private UpilUtilitiesProductPopulator populator;
	private SubscriptionPricePlanModel source;
	private C_UtilitiesProductType target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new UpilUtilitiesProductPopulator();
		populator.setUpilUsageChargeEntryConverter(upilUsageChargeEntryConverter);
		populator.setUpilRecurringChargesConverter(upilRecurringChargesConverter);
		populator.setUpilOneTimeChargesConverter(upilOneTimeChargesConverter);
		populator.setUpilBillingAttributeConverter(upilBillingAttributeConverter);
		populator.setUpilSalesAttributeConverter(upilSalesAttributeConverter);
	}

	@Test
	public void testPopulate()
	{
		setData();
		setProductSpecType();
		setUpilAdditionalAttributes(UpilAttributeType.BILLING);
		setOneTimeChargeEntryData();
		setRecurringChargeEntryData();
		setUsageChargeData();
		populator.populate(source, target);
		Assert.assertNotNull(target.getUtilitiesReferenceProduct());
		Assert.assertNotNull(target.getTo_BillingAttribute());
		Assert.assertNotNull(target.getTo_OneTimeCharge());
		Assert.assertNotNull(target.getTo_RecurringCharge());
		Assert.assertNotNull(target.getTo_UsageCharge());
	}

	@Test
	public void testPopulateSalesAttribute()
	{
		setData();
		setProductSpecType();
		setUpilAdditionalAttributes(UpilAttributeType.SALES);
		setOneTimeChargeEntryData();
		setRecurringChargeEntryData();
		setUsageChargeData();
		populator.populate(source, target);
		Assert.assertNotNull(target.getTo_SalesAttribute());
	}

	@Test
	public void testPopulateEmptySource()
	{
		setData();
		populator.populate(source, target);
		Assert.assertNull(target.getTo_OneTimeCharge());
		Assert.assertNull(target.getTo_RecurringCharge());
		Assert.assertNull(target.getTo_UsageCharge());
	}

	private void setData()
	{
		source = mock(SubscriptionPricePlanModel.class);
		target = new C_UtilitiesProductType();
	}

	private void setProductSpecType()
	{
		final TmaProductSpecTypeModel productSpecType = mock(TmaProductSpecTypeModel.class);
		given(productSpecType.getCode()).willReturn(PRODUCT_SPEC_TYPE);
		given(source.getProductSpecType()).willReturn(productSpecType);
	}

	private void setUpilAdditionalAttributes(final UpilAttributeType attributeType)
	{

		final UpilAdditionalAttributesModel upilAdditionalAttribute = mock(UpilAdditionalAttributesModel.class);
		given(upilAdditionalAttribute.getAttributeType()).willReturn(attributeType);
		final List<UpilAdditionalAttributesModel> upilAdditionalAttributes = new ArrayList<>();
		upilAdditionalAttributes.add(upilAdditionalAttribute);
		given(source.getUpilAdditionalAttributes()).willReturn(upilAdditionalAttributes);
	}

	private void setUsageChargeData()
	{
		final UsageChargeEntryModel usageChargeEntry = mock(UsageChargeEntryModel.class);
		final Collection<UsageChargeEntryModel> usageChargeEntries = new ArrayList<>();
		usageChargeEntries.add(usageChargeEntry);
		final UsageChargeModel usageCharge = mock(UsageChargeModel.class);
		usageCharge.setUsageChargeEntries(usageChargeEntries);
		final List<UsageChargeModel> usageCharges = new ArrayList<>();
		usageCharges.add(usageCharge);
		given(source.getUsageCharges()).willReturn(usageCharges);
	}

	private void setOneTimeChargeEntryData()
	{
		final OneTimeChargeEntryModel oneTimeChargeEntry = mock(OneTimeChargeEntryModel.class);
		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = new ArrayList<>();
		oneTimeChargeEntries.add(oneTimeChargeEntry);
		given(source.getOneTimeChargeEntries()).willReturn(oneTimeChargeEntries);
	}

	private void setRecurringChargeEntryData()
	{
		final RecurringChargeEntryModel recurringChargeEntry = mock(RecurringChargeEntryModel.class);
		final Collection<RecurringChargeEntryModel> recurringChargeEntries = new ArrayList<>();
		recurringChargeEntries.add(recurringChargeEntry);
		given(source.getRecurringChargeEntries()).willReturn(recurringChargeEntries);
	}
}

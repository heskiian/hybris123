/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmSpecData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimePlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringPlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsagePlaSpecModel;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.testframework.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaPricingLogicAlgorithmSpecPopulator}.
 *
 * @since 2102.
 */
@UnitTest
public class TmaPricingLogicAlgorithmSpecPopulatorTest
{
	private TmaPricingLogicAlgorithmSpecPopulator pricingLogicAlgorithmSpecPopulator;
	private LocaleProvider localeProvider;
	private TmaPricingLogicAlgorithmSpecData pricingLogicAlgorithmSpecData;
	private Date date;

	@Mock
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		pricingLogicAlgorithmSpecPopulator = new TmaPricingLogicAlgorithmSpecPopulator(externalIdentifierConverter);
		localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		pricingLogicAlgorithmSpecData = new TmaPricingLogicAlgorithmSpecData();
		date = new Date();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		pricingLogicAlgorithmSpecPopulator.populate(null, pricingLogicAlgorithmSpecData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullTarget()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaOneTimePlaSpecModel._TYPECODE);
		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, null);
	}

	@Test
	public void testPopulate_allFieldsOneTime()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaOneTimePlaSpecModel._TYPECODE);

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmSpecModel.getExternalIds()))
				.thenReturn(getExternalIdDataList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, pricingLogicAlgorithmSpecData);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(pricingLogicAlgorithmSpecData);
	}

	@Test
	public void testPopulate_allFieldsRecurring()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaRecurringPlaSpecModel._TYPECODE);

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmSpecModel.getExternalIds()))
				.thenReturn(getExternalIdDataList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, pricingLogicAlgorithmSpecData);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(pricingLogicAlgorithmSpecData);
	}

	@Test
	public void testPopulate_allFieldsUsage()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaUsagePlaSpecModel._TYPECODE);

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmSpecModel.getExternalIds()))
				.thenReturn(getExternalIdDataList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, pricingLogicAlgorithmSpecData);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(pricingLogicAlgorithmSpecData);
	}

	protected void assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(
			final TmaPricingLogicAlgorithmSpecData pricingLogicAlgorithmSpecData)
	{
		assertEquals("call_duration_pla_spec", pricingLogicAlgorithmSpecData.getId());
		assertEquals("Call Duration Pricing Logic Algorithm Specification", pricingLogicAlgorithmSpecData.getName());
		assertEquals(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.",
				pricingLogicAlgorithmSpecData.getDescription());
		assertEquals(ArticleApprovalStatus.APPROVED.getCode(), pricingLogicAlgorithmSpecData.getApprovalStatus());
		assertEquals(date, pricingLogicAlgorithmSpecData.getOnlineDate());
		assertEquals(date, pricingLogicAlgorithmSpecData.getOfflineDate());

		assertExternalIdFieldsArePopulatedCorrectly(pricingLogicAlgorithmSpecData.getExternalIds());
	}

	protected void assertExternalIdFieldsArePopulatedCorrectly(final List<TmaExternalIdentifierData> externalIdentifierData)
	{
		Assert.assertEquals(1, externalIdentifierData.size());
		Assert.assertEquals("call_duration_pla_spec", externalIdentifierData.get(0).getId());
		Assert.assertEquals("amdocs", externalIdentifierData.get(0).getOwner());
		Assert.assertEquals("PricingLogicAlgorithmSpec", externalIdentifierData.get(0).getResourceType());
	}

	private TmaPricingLogicAlgorithmSpecModel getPricingLogicAlgorithmSpecModel(final String type)
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = createPricingLogicAlgorithmSpecModel(type);

		assert pricingLogicAlgorithmSpecModel != null;

		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) pricingLogicAlgorithmSpecModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		pricingLogicAlgorithmSpecModel.setId("call_duration_pla_spec");
		pricingLogicAlgorithmSpecModel.setName("Call Duration Pricing Logic Algorithm Specification");
		pricingLogicAlgorithmSpecModel.setDescription(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.");
		pricingLogicAlgorithmSpecModel.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		pricingLogicAlgorithmSpecModel.setOnlineDate(date);
		pricingLogicAlgorithmSpecModel.setOfflineDate(date);
		pricingLogicAlgorithmSpecModel.setExternalIds(getExternalIdModels());

		return pricingLogicAlgorithmSpecModel;
	}

	private Set<TmaExternalIdentifierModel> getExternalIdModels()
	{
		final Set<TmaExternalIdentifierModel> externalIdentifierModels = new HashSet<>();

		final TmaExternalIdentifierModel externalIdentifierModel = new TmaExternalIdentifierModel();
		externalIdentifierModel.setBillingSystemId("amdocs");
		externalIdentifierModel.setBillingSystemExtId("call_duration_pla_spec");
		externalIdentifierModel.setResourceType("PricingLogicAlgorithmSpec");
		externalIdentifierModels.add(externalIdentifierModel);

		return externalIdentifierModels;
	}

	private TmaPricingLogicAlgorithmSpecModel createPricingLogicAlgorithmSpecModel(final String type)
	{
		if (type.equals(TmaOneTimePlaSpecModel._TYPECODE))
		{
			return new TmaOneTimePlaSpecModel();
		}

		if (type.equals(TmaRecurringPlaSpecModel._TYPECODE))
		{
			return new TmaRecurringPlaSpecModel();
		}

		if (type.equals(TmaUsagePlaSpecModel._TYPECODE))
		{
			return new TmaUsagePlaSpecModel();
		}

		return null;
	}

	private List<TmaExternalIdentifierData> getExternalIdDataList()
	{
		final List<TmaExternalIdentifierData> externalIdentifierDataList = new ArrayList<>();

		final TmaExternalIdentifierData externalIdentifierData = new TmaExternalIdentifierData();
		externalIdentifierData.setId("call_duration_pla_spec");
		externalIdentifierData.setOwner("amdocs");
		externalIdentifierData.setResourceType("PricingLogicAlgorithmSpec");
		externalIdentifierDataList.add(externalIdentifierData);

		return externalIdentifierDataList;
	}
}

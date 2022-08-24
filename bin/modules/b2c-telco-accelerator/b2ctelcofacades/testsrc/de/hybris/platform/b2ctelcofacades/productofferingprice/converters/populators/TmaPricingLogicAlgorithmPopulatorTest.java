/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaOneTimePricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmSpecData;
import de.hybris.platform.b2ctelcofacades.data.TmaRecurringPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.data.TmaUsagePricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimePlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeRatingPlaModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringPlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringRatingPlaModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsagePlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageRatingPlaModel;
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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Test class for {@link TmaPricingLogicAlgorithmPopulator}.
 *
 * @since 2102.
 */
@UnitTest
public class TmaPricingLogicAlgorithmPopulatorTest
{
	private TmaPricingLogicAlgorithmPopulator pricingLogicAlgorithmPopulator;
	private LocaleProvider localeProvider;
	private TmaPricingLogicAlgorithmData pricingLogicAlgorithmData;
	private Date date;

	@Mock
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	@Mock
	private Converter<TmaPricingLogicAlgorithmSpecModel, TmaPricingLogicAlgorithmSpecData> pricingLogicAlgorithmSpecConverter;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		pricingLogicAlgorithmPopulator = new TmaPricingLogicAlgorithmPopulator(pricingLogicAlgorithmSpecConverter,
				externalIdentifierConverter);
		localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		date = new Date();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		pricingLogicAlgorithmPopulator.populate(null, pricingLogicAlgorithmData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullTarget()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaOneTimeRatingPlaModel._TYPECODE);
		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, null);
	}

	@Test
	public void testPopulate_allFieldsOneTime()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaOneTimeRatingPlaModel._TYPECODE);
		pricingLogicAlgorithmData = new TmaOneTimePricingLogicAlgorithmData();

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getExternalPlaIdDataList());
		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getPricingLogicAlgorithmSpecData());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, pricingLogicAlgorithmData);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(pricingLogicAlgorithmData);
	}

	@Test
	public void testPopulate_allFieldsRecurring()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaRecurringRatingPlaModel._TYPECODE);
		pricingLogicAlgorithmData = new TmaRecurringPricingLogicAlgorithmData();

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getExternalPlaIdDataList());
		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getPricingLogicAlgorithmSpecData());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, pricingLogicAlgorithmData);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(pricingLogicAlgorithmData);
	}

	@Test
	public void testPopulate_allFieldsUsage()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaUsageRatingPlaModel._TYPECODE);
		pricingLogicAlgorithmData = new TmaUsagePricingLogicAlgorithmData();

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getExternalPlaIdDataList());
		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getPricingLogicAlgorithmSpecData());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, pricingLogicAlgorithmData);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(pricingLogicAlgorithmData);
	}

	protected void assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(
			final TmaPricingLogicAlgorithmData pricingLogicAlgorithmData)
	{
		assertEquals("call_duration_pla", pricingLogicAlgorithmData.getId());
		assertEquals("Call Duration Pricing Logic Algorithm", pricingLogicAlgorithmData.getName());
		assertEquals("Pricing Logic Algorithm used for calculating charges for call usage events based on the call duration.",
				pricingLogicAlgorithmData.getDescription());
		assertEquals(date, pricingLogicAlgorithmData.getOnlineDate());
		assertEquals(date, pricingLogicAlgorithmData.getOfflineDate());

		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(pricingLogicAlgorithmData.getPricingLogicAlgorithmSpec());
		assertExternalPlaIdFieldsArePopulatedCorrectly(pricingLogicAlgorithmData.getExternalIds());
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

		assertExternalPlaSpecIdFieldsArePopulatedCorrectly(pricingLogicAlgorithmSpecData.getExternalIds());
	}

	protected void assertExternalPlaIdFieldsArePopulatedCorrectly(final List<TmaExternalIdentifierData> externalIdentifierData)
	{
		assertEquals(1, externalIdentifierData.size(), 1);
		assertEquals("call_duration_pla", externalIdentifierData.get(0).getId());
		assertEquals("amdocs", externalIdentifierData.get(0).getOwner());
		assertEquals("PricingLogicAlgorithm", externalIdentifierData.get(0).getResourceType());
	}

	protected void assertExternalPlaSpecIdFieldsArePopulatedCorrectly(final List<TmaExternalIdentifierData> externalIdentifierData)
	{
		assertEquals(1, externalIdentifierData.size());
		assertEquals("call_duration_pla_spec", externalIdentifierData.get(0).getId());
		assertEquals("amdocs", externalIdentifierData.get(0).getOwner());
		assertEquals("PricingLogicAlgorithmSpec", externalIdentifierData.get(0).getResourceType());
	}

	private TmaPricingLogicAlgorithmModel getPricingLogicAlgorithmModel(final String type)
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = createPricingLogicAlgorithmModel(type);

		assert pricingLogicAlgorithmModel != null;

		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) pricingLogicAlgorithmModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		pricingLogicAlgorithmModel.setId("call_duration_pla");
		pricingLogicAlgorithmModel.setName("Call Duration Pricing Logic Algorithm");
		pricingLogicAlgorithmModel.setDescription(
				"Pricing Logic Algorithm used for calculating charges for call usage events based on the call duration.");
		pricingLogicAlgorithmModel.setOnlineDate(date);
		pricingLogicAlgorithmModel.setOfflineDate(date);
		pricingLogicAlgorithmModel
				.setPricingLogicAlgorithmSpec(getPricingLogicAlgorithmSpecModel(getPricingLogicAlgorithmSpecType(type)));
		pricingLogicAlgorithmModel.setExternalIds(getExternalPlaIdModels());

		return pricingLogicAlgorithmModel;
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
		pricingLogicAlgorithmSpecModel.setExternalIds(getExternalPlaSpecIdModels());

		return pricingLogicAlgorithmSpecModel;
	}

	private Set<TmaExternalIdentifierModel> getExternalPlaIdModels()
	{
		final Set<TmaExternalIdentifierModel> externalIdentifierModels = new HashSet<>();

		final TmaExternalIdentifierModel externalIdentifierModel = new TmaExternalIdentifierModel();
		externalIdentifierModel.setBillingSystemId("amdocs");
		externalIdentifierModel.setBillingSystemExtId("call_duration_pla");
		externalIdentifierModel.setResourceType("PricingLogicAlgorithm");
		externalIdentifierModels.add(externalIdentifierModel);

		return externalIdentifierModels;
	}

	private Set<TmaExternalIdentifierModel> getExternalPlaSpecIdModels()
	{
		final Set<TmaExternalIdentifierModel> externalIdentifierModels = new HashSet<>();

		final TmaExternalIdentifierModel externalIdentifierModel = new TmaExternalIdentifierModel();
		externalIdentifierModel.setBillingSystemId("amdocs");
		externalIdentifierModel.setBillingSystemExtId("call_duration_pla_spec");
		externalIdentifierModel.setResourceType("PricingLogicAlgorithmSpec");
		externalIdentifierModels.add(externalIdentifierModel);

		return externalIdentifierModels;
	}

	private TmaPricingLogicAlgorithmModel createPricingLogicAlgorithmModel(final String type)
	{
		if (type.equals(TmaOneTimeRatingPlaModel._TYPECODE))
		{
			return new TmaOneTimeRatingPlaModel();
		}

		if (type.equals(TmaRecurringRatingPlaModel._TYPECODE))
		{
			return new TmaRecurringRatingPlaModel();
		}

		if (type.equals(TmaUsageRatingPlaModel._TYPECODE))
		{
			return new TmaUsageRatingPlaModel();
		}

		return null;
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

	private String getPricingLogicAlgorithmSpecType(final String type)
	{
		if (type.equals(TmaOneTimeRatingPlaModel._TYPECODE))
		{
			return TmaOneTimePlaSpecModel._TYPECODE;
		}

		if (type.equals(TmaRecurringRatingPlaModel._TYPECODE))
		{
			return TmaRecurringPlaSpecModel._TYPECODE;
		}

		if (type.equals(TmaUsageRatingPlaModel._TYPECODE))
		{
			return TmaUsagePlaSpecModel._TYPECODE;
		}

		return StringUtils.EMPTY;
	}

	private TmaPricingLogicAlgorithmSpecData getPricingLogicAlgorithmSpecData()
	{
		final TmaPricingLogicAlgorithmSpecData pricingLogicAlgorithmSpecData = new TmaPricingLogicAlgorithmSpecData();

		pricingLogicAlgorithmSpecData.setId("call_duration_pla_spec");
		pricingLogicAlgorithmSpecData.setName("Call Duration Pricing Logic Algorithm Specification");
		pricingLogicAlgorithmSpecData.setDescription(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.");
		pricingLogicAlgorithmSpecData.setApprovalStatus(ArticleApprovalStatus.APPROVED.getCode());
		pricingLogicAlgorithmSpecData.setOnlineDate(date);
		pricingLogicAlgorithmSpecData.setOfflineDate(date);
		pricingLogicAlgorithmSpecData.setExternalIds(getExternalPlaSpecIdDataList());

		return pricingLogicAlgorithmSpecData;
	}

	private List<TmaExternalIdentifierData> getExternalPlaIdDataList()
	{
		final List<TmaExternalIdentifierData> externalIdentifierDataList = new ArrayList<>();

		final TmaExternalIdentifierData externalIdentifierData = new TmaExternalIdentifierData();
		externalIdentifierData.setId("call_duration_pla");
		externalIdentifierData.setOwner("amdocs");
		externalIdentifierData.setResourceType("PricingLogicAlgorithm");
		externalIdentifierDataList.add(externalIdentifierData);

		return externalIdentifierDataList;
	}

	private List<TmaExternalIdentifierData> getExternalPlaSpecIdDataList()
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

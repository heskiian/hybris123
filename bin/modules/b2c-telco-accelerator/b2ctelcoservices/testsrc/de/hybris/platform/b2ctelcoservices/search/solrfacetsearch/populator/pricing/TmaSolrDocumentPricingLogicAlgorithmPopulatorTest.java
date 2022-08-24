/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeRatingPlaModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringRatingPlaModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageRatingPlaModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithmSpec;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Default Unit Test for the {@link TmaSolrDocumentPricingLogicAlgorithmPopulator}.
 *
 * @since 2102
 */
@UnitTest
public class TmaSolrDocumentPricingLogicAlgorithmPopulatorTest
{
	private TmaSolrDocumentPricingLogicAlgorithmPopulator pricingLogicAlgorithmPopulator;
	private LocaleProvider localeProvider;
	private TmaSolrDocumentPricingLogicAlgorithm solrDocumentPricingLogicAlgorithm;
	private Date date;

	@Mock
	private Converter<TmaPricingLogicAlgorithmSpecModel, TmaSolrDocumentPricingLogicAlgorithmSpec> pricingLogicAlgorithmSpecConverter;

	@Mock
	private Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		pricingLogicAlgorithmPopulator = new TmaSolrDocumentPricingLogicAlgorithmPopulator(pricingLogicAlgorithmSpecConverter,
				externalIdentifierConverter);
		localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		solrDocumentPricingLogicAlgorithm = new TmaSolrDocumentPricingLogicAlgorithm();
		date = new Date();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		pricingLogicAlgorithmPopulator.populate(null, solrDocumentPricingLogicAlgorithm);
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

		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getSolrDocumentPricingLogicAlgorithmSpec());
		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getSolrDocumentExternalPlaIdList());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, solrDocumentPricingLogicAlgorithm);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithm,
				TmaOneTimeRatingPlaModel._TYPECODE);
	}

	@Test
	public void testPopulate_allFieldsRecurring()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaRecurringRatingPlaModel._TYPECODE);

		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getSolrDocumentPricingLogicAlgorithmSpec());
		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getSolrDocumentExternalPlaIdList());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, solrDocumentPricingLogicAlgorithm);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithm,
				TmaRecurringRatingPlaModel._TYPECODE);
	}

	@Test
	public void testPopulate_allFieldsUsage()
	{
		final TmaPricingLogicAlgorithmModel pricingLogicAlgorithmModel = getPricingLogicAlgorithmModel(
				TmaUsageRatingPlaModel._TYPECODE);

		when(pricingLogicAlgorithmSpecConverter.convert(pricingLogicAlgorithmModel.getPricingLogicAlgorithmSpec()))
				.thenReturn(getSolrDocumentPricingLogicAlgorithmSpec());
		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmModel.getExternalIds()))
				.thenReturn(getSolrDocumentExternalPlaIdList());

		pricingLogicAlgorithmPopulator.populate(pricingLogicAlgorithmModel, solrDocumentPricingLogicAlgorithm);
		assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithm, TmaUsageRatingPlaModel._TYPECODE);
	}

	protected void assertPricingLogicAlgorithmFieldsArePopulatedCorrectly(
			final TmaSolrDocumentPricingLogicAlgorithm solrDocumentPricingLogicAlgorithm, final String type)
	{
		assertEquals("call_duration_pla", solrDocumentPricingLogicAlgorithm.getId());
		assertEquals("Call Duration Pricing Logic Algorithm", solrDocumentPricingLogicAlgorithm.getName());
		assertEquals("Pricing Logic Algorithm used for calculating charges for call usage events based on the call duration.",
				solrDocumentPricingLogicAlgorithm.getDescription());
		assertEquals(date, solrDocumentPricingLogicAlgorithm.getOnlineDate());
		assertEquals(date, solrDocumentPricingLogicAlgorithm.getOfflineDate());
		assertEquals(type, solrDocumentPricingLogicAlgorithm.getType());

		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(
				solrDocumentPricingLogicAlgorithm.getPricingLogicAlgorithmSpec());
		assertExternalPlaIdFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithm.getExternalIds());
	}

	protected void assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(
			final TmaSolrDocumentPricingLogicAlgorithmSpec solrDocumentPricingLogicAlgorithmSpec)
	{
		assertEquals("call_duration_pla_spec", solrDocumentPricingLogicAlgorithmSpec.getId());
		assertEquals("Call Duration Pricing Logic Algorithm Specification", solrDocumentPricingLogicAlgorithmSpec.getName());
		assertEquals(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.",
				solrDocumentPricingLogicAlgorithmSpec.getDescription());
		assertEquals(ArticleApprovalStatus.APPROVED.getCode(), solrDocumentPricingLogicAlgorithmSpec.getApprovalStatus());
		assertEquals(date, solrDocumentPricingLogicAlgorithmSpec.getOnlineDate());
		assertEquals(date, solrDocumentPricingLogicAlgorithmSpec.getOfflineDate());

		assertExternalPlaSpecIdFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithmSpec.getExternalIds());
	}

	protected void assertExternalPlaIdFieldsArePopulatedCorrectly(
			final List<TmaSolrDocumentExternalIdentifier> solrDocumentExternalIdentifierList)
	{
		assertEquals(1, solrDocumentExternalIdentifierList.size(), 1);
		assertEquals("call_duration_pla", solrDocumentExternalIdentifierList.get(0).getId());
		assertEquals("amdocs", solrDocumentExternalIdentifierList.get(0).getOwner());
		assertEquals("PricingLogicAlgorithm", solrDocumentExternalIdentifierList.get(0).getResourceType());
	}

	protected void assertExternalPlaSpecIdFieldsArePopulatedCorrectly(
			final List<TmaSolrDocumentExternalIdentifier> solrDocumentExternalIdentifierList)
	{
		assertEquals(1, solrDocumentExternalIdentifierList.size());
		assertEquals("call_duration_pla_spec", solrDocumentExternalIdentifierList.get(0).getId());
		assertEquals("amdocs", solrDocumentExternalIdentifierList.get(0).getOwner());
		assertEquals("PricingLogicAlgorithmSpec", solrDocumentExternalIdentifierList.get(0).getResourceType());
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
				.setPricingLogicAlgorithmSpec(getPricingLogicAlgorithmSpecModel());
		pricingLogicAlgorithmModel.setExternalIds(getExternalPlaIdModels());

		return pricingLogicAlgorithmModel;
	}

	private TmaPricingLogicAlgorithmSpecModel getPricingLogicAlgorithmSpecModel()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = new TmaPricingLogicAlgorithmSpecModel();

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

	private TmaSolrDocumentPricingLogicAlgorithmSpec getSolrDocumentPricingLogicAlgorithmSpec()
	{
		final TmaSolrDocumentPricingLogicAlgorithmSpec solrDocumentPricingLogicAlgorithmSpec = new TmaSolrDocumentPricingLogicAlgorithmSpec();

		solrDocumentPricingLogicAlgorithmSpec.setId("call_duration_pla_spec");
		solrDocumentPricingLogicAlgorithmSpec.setName("Call Duration Pricing Logic Algorithm Specification");
		solrDocumentPricingLogicAlgorithmSpec.setDescription(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.");
		solrDocumentPricingLogicAlgorithmSpec.setApprovalStatus(ArticleApprovalStatus.APPROVED.getCode());
		solrDocumentPricingLogicAlgorithmSpec.setOnlineDate(date);
		solrDocumentPricingLogicAlgorithmSpec.setOfflineDate(date);
		solrDocumentPricingLogicAlgorithmSpec.setExternalIds(getSolrDocumentExternalPlaSpecIdList());

		return solrDocumentPricingLogicAlgorithmSpec;
	}

	private List<TmaSolrDocumentExternalIdentifier> getSolrDocumentExternalPlaIdList()
	{
		final List<TmaSolrDocumentExternalIdentifier> solrDocumentExternalIdentifiers = new ArrayList<>();

		final TmaSolrDocumentExternalIdentifier solrDocumentExternalIdentifier = new TmaSolrDocumentExternalIdentifier();
		solrDocumentExternalIdentifier.setId("call_duration_pla");
		solrDocumentExternalIdentifier.setOwner("amdocs");
		solrDocumentExternalIdentifier.setResourceType("PricingLogicAlgorithm");
		solrDocumentExternalIdentifiers.add(solrDocumentExternalIdentifier);

		return solrDocumentExternalIdentifiers;
	}

	private List<TmaSolrDocumentExternalIdentifier> getSolrDocumentExternalPlaSpecIdList()
	{
		final List<TmaSolrDocumentExternalIdentifier> solrDocumentExternalIdentifiers = new ArrayList<>();

		final TmaSolrDocumentExternalIdentifier solrDocumentExternalIdentifier = new TmaSolrDocumentExternalIdentifier();
		solrDocumentExternalIdentifier.setId("call_duration_pla_spec");
		solrDocumentExternalIdentifier.setOwner("amdocs");
		solrDocumentExternalIdentifier.setResourceType("PricingLogicAlgorithmSpec");
		solrDocumentExternalIdentifiers.add(solrDocumentExternalIdentifier);

		return solrDocumentExternalIdentifiers;
	}
}


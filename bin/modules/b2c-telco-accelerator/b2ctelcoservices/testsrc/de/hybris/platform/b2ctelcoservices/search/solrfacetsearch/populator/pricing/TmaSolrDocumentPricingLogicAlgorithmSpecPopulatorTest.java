/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimePlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringPlaSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsagePlaSpecModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithmSpec;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.testframework.Assert;

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

import static org.mockito.Mockito.when;


/**
 * Default Unit Test for the {@link TmaSolrDocumentPricingLogicAlgorithmSpecPopulator}.
 *
 * @since 2102
 */
@UnitTest
public class TmaSolrDocumentPricingLogicAlgorithmSpecPopulatorTest
{
	private TmaSolrDocumentPricingLogicAlgorithmSpecPopulator pricingLogicAlgorithmSpecPopulator;
	private LocaleProvider localeProvider;
	private TmaSolrDocumentPricingLogicAlgorithmSpec solrDocumentPricingLogicAlgorithmSpec;
	private Date date;

	@Mock
	private Converter<TmaExternalIdentifierModel, TmaSolrDocumentExternalIdentifier> externalIdentifierConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		pricingLogicAlgorithmSpecPopulator = new TmaSolrDocumentPricingLogicAlgorithmSpecPopulator(externalIdentifierConverter);
		localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		solrDocumentPricingLogicAlgorithmSpec = new TmaSolrDocumentPricingLogicAlgorithmSpec();
		date = new Date();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		pricingLogicAlgorithmSpecPopulator.populate(null, solrDocumentPricingLogicAlgorithmSpec);
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
				.thenReturn(getSolrDocumentExternalIdList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, solrDocumentPricingLogicAlgorithmSpec);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithmSpec);
	}

	@Test
	public void testPopulate_allFieldsRecurring()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaRecurringPlaSpecModel._TYPECODE);

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmSpecModel.getExternalIds()))
				.thenReturn(getSolrDocumentExternalIdList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, solrDocumentPricingLogicAlgorithmSpec);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithmSpec);
	}

	@Test
	public void testPopulate_allFieldsUsage()
	{
		final TmaPricingLogicAlgorithmSpecModel pricingLogicAlgorithmSpecModel = getPricingLogicAlgorithmSpecModel(
				TmaUsagePlaSpecModel._TYPECODE);

		when(externalIdentifierConverter.convertAll(pricingLogicAlgorithmSpecModel.getExternalIds()))
				.thenReturn(getSolrDocumentExternalIdList());

		pricingLogicAlgorithmSpecPopulator.populate(pricingLogicAlgorithmSpecModel, solrDocumentPricingLogicAlgorithmSpec);
		assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithmSpec);
	}

	protected void assertPricingLogicAlgorithmSpecFieldsArePopulatedCorrectly(
			final TmaSolrDocumentPricingLogicAlgorithmSpec solrDocumentPricingLogicAlgorithmSpec)
	{
		Assert.assertEquals("call_duration_pla_spec", solrDocumentPricingLogicAlgorithmSpec.getId());
		Assert.assertEquals("Call Duration Pricing Logic Algorithm Specification", solrDocumentPricingLogicAlgorithmSpec.getName());
		Assert.assertEquals(
				"Pricing Logic Algorithm Specification used for calculating the cost of charges for call usage events based on the call duration.",
				solrDocumentPricingLogicAlgorithmSpec.getDescription());
		Assert.assertEquals(ArticleApprovalStatus.APPROVED.getCode(), solrDocumentPricingLogicAlgorithmSpec.getApprovalStatus());
		Assert.assertEquals(date, solrDocumentPricingLogicAlgorithmSpec.getOnlineDate());
		Assert.assertEquals(date, solrDocumentPricingLogicAlgorithmSpec.getOfflineDate());

		assertExternalIdFieldsArePopulatedCorrectly(solrDocumentPricingLogicAlgorithmSpec.getExternalIds());
	}

	protected void assertExternalIdFieldsArePopulatedCorrectly(
			final List<TmaSolrDocumentExternalIdentifier> solrDocumentExternalIdentifiers)
	{
		org.junit.Assert.assertEquals(1, solrDocumentExternalIdentifiers.size());
		org.junit.Assert.assertEquals("call_duration_pla_spec", solrDocumentExternalIdentifiers.get(0).getId());
		org.junit.Assert.assertEquals("amdocs", solrDocumentExternalIdentifiers.get(0).getOwner());
		org.junit.Assert.assertEquals("PricingLogicAlgorithmSpec", solrDocumentExternalIdentifiers.get(0).getResourceType());
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

	private List<TmaSolrDocumentExternalIdentifier> getSolrDocumentExternalIdList()
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

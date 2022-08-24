/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ObjectUtils;

import static org.junit.Assert.assertTrue;


/**
 * Test class for the @{TmaPriceRegionSolrFilterPopulator}
 *
 * @since 1907
 */
@UnitTest
public class TmaPriceRegionSolrFilterPopulatorTest
{
	private static final String KEY1 = "region";
	private static final String VALUE1 = "JP-30";
	private static final Integer TWO = 2;
	private static final String ALL_REGION_PRICES = " ";

	@Mock
	private TmaRegionService tmaRegionService;

	private TmaPriceRegionSolrFilterPopulator populator;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new TmaPriceRegionSolrFilterPopulator<FacetSearchConfig, IndexedTypeSort>();
		populator.setTmaRegionService(tmaRegionService);
	}

	@Test
	public void testPopulate()
	{

		final SearchQueryPageableData<SolrSearchQueryData> source = null;

		final SolrSearchRequest<FacetSearchConfig, IndexedType, IndexedProperty, SearchQuery, IndexedTypeSort> target = populateTarget();

		populator.populate(source, target);

		assertTrue(target.getSearchQueryData().getFilterTerms().size() == TWO);
		assertTrue(target.getSearchQueryData().getFilterTerms().get(0).getKey().equals(KEY1));
		assertTrue(target.getSearchQueryData().getFilterTerms().get(0).getValue().equals(VALUE1));
		assertTrue(target.getSearchQueryData().getFilterTerms().get(1).getKey().equals(KEY1));
		assertTrue(target.getSearchQueryData().getFilterTerms().get(1).getValue().equals(ALL_REGION_PRICES));
	}

	@Test
	public void testPopulateSolrSearchQueryDataNull()
	{
		final SearchQueryPageableData<SolrSearchQueryData> source = null;

		final SolrSearchRequest<FacetSearchConfig, IndexedType, IndexedProperty, SearchQuery, IndexedTypeSort> target = new SolrSearchRequest<>();
		target.setSearchQueryData(null);

		populator.populate(source, target);

		assertTrue(ObjectUtils.isEmpty(target.getSearchQueryData()));
	}

	@Test
	public void testPopulateFilterTermsNull()
	{
		final SearchQueryPageableData<SolrSearchQueryData> source = null;

		final SolrSearchRequest<FacetSearchConfig, IndexedType, IndexedProperty, SearchQuery, IndexedTypeSort> target = new SolrSearchRequest<>();

		final SolrSearchQueryData solrSearchQueryData = new SolrSearchQueryData();
		solrSearchQueryData.setFilterTerms(null);

		target.setSearchQueryData(solrSearchQueryData);

		populator.populate(source, target);

		assertTrue(ObjectUtils.isEmpty(target.getSearchQueryData().getFilterTerms()));
	}

	protected SolrSearchRequest<FacetSearchConfig, IndexedType, IndexedProperty, SearchQuery, IndexedTypeSort> populateTarget()
	{
		final SolrSearchRequest<FacetSearchConfig, IndexedType, IndexedProperty, SearchQuery, IndexedTypeSort> solrSearchRequest = new SolrSearchRequest<>();

		final SolrSearchQueryTermData solrSearchQueryTermData1 = new SolrSearchQueryTermData();
		solrSearchQueryTermData1.setKey(KEY1);
		solrSearchQueryTermData1.setValue(VALUE1);

		final List<SolrSearchQueryTermData> filterTerms = new ArrayList<>();
		filterTerms.add(solrSearchQueryTermData1);

		final SolrSearchQueryData solrSearchQueryData = new SolrSearchQueryData();
		solrSearchQueryData.setFilterTerms(filterTerms);

		solrSearchRequest.setSearchQueryData(solrSearchQueryData);

		return solrSearchRequest;
	}

}

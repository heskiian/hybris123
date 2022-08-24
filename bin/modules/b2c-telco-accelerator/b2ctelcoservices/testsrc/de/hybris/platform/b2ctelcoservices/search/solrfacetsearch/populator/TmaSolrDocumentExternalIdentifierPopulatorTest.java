/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.data.TmaSolrDocumentExternalIdentifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test class for {@link TmaSolrDocumentExternalIdentifierPopulator}.
 *
 * @since 2102
 */
@UnitTest
public class TmaSolrDocumentExternalIdentifierPopulatorTest
{
	private TmaSolrDocumentExternalIdentifierPopulator externalIdentifierPopulator;
	private TmaSolrDocumentExternalIdentifier solrDocumentExternalIdentifier;

	@Before
	public void setUp()
	{
		externalIdentifierPopulator = new TmaSolrDocumentExternalIdentifierPopulator();
		solrDocumentExternalIdentifier = new TmaSolrDocumentExternalIdentifier();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		externalIdentifierPopulator.populate(null, solrDocumentExternalIdentifier);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullTarget()
	{
		final TmaExternalIdentifierModel externalIdentifierModel = getExternalIdentifierModel();
		externalIdentifierPopulator.populate(externalIdentifierModel, null);
	}

	@Test
	public void testPopulate_allFields()
	{
		final TmaExternalIdentifierModel externalIdentifierModel = getExternalIdentifierModel();
		externalIdentifierPopulator.populate(externalIdentifierModel, solrDocumentExternalIdentifier);
		assertExternalIdentifierFieldsArePopulatedCorrectly(solrDocumentExternalIdentifier);
	}

	protected void assertExternalIdentifierFieldsArePopulatedCorrectly(
			final TmaSolrDocumentExternalIdentifier solrDocumentExternalIdentifier)
	{
		assertEquals("amdocs", solrDocumentExternalIdentifier.getOwner());
		assertEquals("call_duration_pla", solrDocumentExternalIdentifier.getId());
		assertEquals("PricingLogicAlgorithm", solrDocumentExternalIdentifier.getResourceType());
	}

	private TmaExternalIdentifierModel getExternalIdentifierModel()
	{
		final TmaExternalIdentifierModel externalIdentifierModel = new TmaExternalIdentifierModel();

		externalIdentifierModel.setBillingSystemId("amdocs");
		externalIdentifierModel.setBillingSystemExtId("call_duration_pla");
		externalIdentifierModel.setResourceType("PricingLogicAlgorithm");

		return externalIdentifierModel;
	}
}

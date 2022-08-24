/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test class for {@link TmaExternalIdentifierPopulator}.
 *
 * @since 2102
 */
@UnitTest
public class TmaExternalIdentifierPopulatorTest
{
	private TmaExternalIdentifierPopulator externalIdentifierPopulator;
	private TmaExternalIdentifierData externalIdentifierData;

	@Before
	public void setUp()
	{
		externalIdentifierPopulator = new TmaExternalIdentifierPopulator();
		externalIdentifierData = new TmaExternalIdentifierData();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulate_nullSource()
	{
		externalIdentifierPopulator.populate(null, externalIdentifierData);
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
		externalIdentifierPopulator.populate(externalIdentifierModel, externalIdentifierData);
		assertExternalIdentifierFieldsArePopulatedCorrectly(externalIdentifierData);
	}

	protected void assertExternalIdentifierFieldsArePopulatedCorrectly(
			final TmaExternalIdentifierData externalIdentifierData)
	{
		assertEquals("amdocs", externalIdentifierData.getOwner());
		assertEquals("call_duration_pla", externalIdentifierData.getId());
		assertEquals("PricingLogicAlgorithm", externalIdentifierData.getResourceType());
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
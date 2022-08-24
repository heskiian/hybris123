/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Integration test for {@link DefaultTmaRegionService}
 *
 * @since 1907
 */
@IntegrationTest
public class DefaultTmaRegionServiceTest extends ServicelayerTest
{
	private static final String TEST_ISOCODE = "IN-ND";
	private static final String NON_EXISTENT_ID = "non_existent_id";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Resource
	private TmaRegionService tmaRegionService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_regions.impex", "utf-8");
	}

	@Test
	public void testExistingDataModel()
	{
		final RegionModel regionModel = tmaRegionService.findRegionByIsocode(TEST_ISOCODE);
		assertNotNull(regionModel.getIsocode());
		assertEquals(TEST_ISOCODE, regionModel.getIsocode());
	}

	@Test
	public void testNonExistingDataModel()
	{
		thrown.expect(ModelNotFoundException.class);
		tmaRegionService.findRegionByIsocode(NON_EXISTENT_ID);
	}
}

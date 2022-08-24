/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaRegionDao;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Test class for {@link DefaultTmaRegionDao}
 *
 * @since 1907
 */
@IntegrationTest
public class DefaultTmaRegionDaoTest extends ServicelayerTest
{
	private static final String NON_EXISTENT_ID = "non_existent_id";
	private static final String TEST_ISOCODE = "IN-MY";

	@Resource
	private TmaRegionDao tmaRegionDao;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_regions.impex", "utf-8");
	}

	@Test
	public void testExistingDataModel()
	{
		final RegionModel regionModel = this.tmaRegionDao.findRegionByIsocode(TEST_ISOCODE);
		assertNotNull(regionModel.getIsocode());
		assertEquals(TEST_ISOCODE, regionModel.getIsocode());
	}

	@Test
	public void testNonExistingDataModel()
	{
		thrown.expect(ModelNotFoundException.class);
		tmaRegionDao.findRegionByIsocode("non_exist_isocode");
	}
}

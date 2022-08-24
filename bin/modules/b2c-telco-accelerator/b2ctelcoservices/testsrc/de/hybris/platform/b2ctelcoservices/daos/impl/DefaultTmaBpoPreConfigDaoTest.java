/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaBpoPreConfigDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Test class for {@link DefaultTmaBpoPreConfigDao}
 */
@IntegrationTest
public class DefaultTmaBpoPreConfigDaoTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaBpoPreConfigDaoTest.class);

	private static final String NON_EXISTENT_ID = "non_existent_id";

	private static final String TEST_PRECONFIG_MOBILE_DEAL = "testPreconfigMobileDeal";

	private static final String TEST_BASESITE_UID = "testSite";

	private static final String CATALOG_ID = "testCatalog";

	private static final String CATALOG_VERSION = "Online";

	@Resource
	private TmaBpoPreConfigDao tmaBpoPreConfigDao;

	@Resource
	private BaseSiteService baseSiteService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setUp() throws ImpExException
	{
		LOG.debug("Preparing test data");
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		try
		{
			importCsv("/b2ctelcofacades/test/testTmaBpoPreConfigData.impex", "utf-8");
			baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		}
		finally
		{
			Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
		}

	}

	@Test
	public void testExistingDataModel()
	{
		final TmaBpoPreConfigModel preConfigModel = this.tmaBpoPreConfigDao.findBpoPreconfigByCode(TEST_PRECONFIG_MOBILE_DEAL,
				fetchCatalogVersion());
		assertNotNull(preConfigModel.getCode());
		assertEquals(TEST_PRECONFIG_MOBILE_DEAL, preConfigModel.getCode());
	}

	@Test
	public void testNonExistingDataModel()
	{
		thrown.expect(ModelNotFoundException.class);
		tmaBpoPreConfigDao.findBpoPreconfigByCode(NON_EXISTENT_ID, fetchCatalogVersion());
	}

	@Test
	public void testCodeAsNull()
	{
		thrown.expect(IllegalArgumentException.class);
		this.tmaBpoPreConfigDao.findBpoPreconfigByCode(null, fetchCatalogVersion());
	}

	@Test
	public void testCatalogAsNull()
	{
		thrown.expect(IllegalArgumentException.class);
		this.tmaBpoPreConfigDao.findBpoPreconfigByCode(NON_EXISTENT_ID, null);
	}

	private CatalogVersionModel fetchCatalogVersion()
	{
		return catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION);
	}
}

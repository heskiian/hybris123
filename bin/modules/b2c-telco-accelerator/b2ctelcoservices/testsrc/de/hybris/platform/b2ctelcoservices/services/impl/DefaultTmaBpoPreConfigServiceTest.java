/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBpoPreConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.impex.constants.ImpExConstants;
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
 * Integration test for {@link DefaultTmaBpoPreConfigService}
 */
@IntegrationTest
public class DefaultTmaBpoPreConfigServiceTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaBpoPreConfigServiceTest.class);

	private static final String TEST_PRECONFIG_MOBILE_DEAL = "testPreconfigMobileDeal";

	private static final String TEST_BASESITE_UID = "testSite";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Resource
	private TmaBpoPreConfigService tmaBpoPreConfigService;

	@Resource
	private BaseSiteService baseSiteService;

	@Before
	public void setUp() throws Exception
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
		final TmaBpoPreConfigModel preConfigModel = this.tmaBpoPreConfigService.getBpoPreConfigForCode(TEST_PRECONFIG_MOBILE_DEAL);
		assertNotNull(preConfigModel.getCode());
		assertEquals(TEST_PRECONFIG_MOBILE_DEAL, preConfigModel.getCode());
	}

	@Test
	public void testNonExistingDataModel()
	{
		thrown.expect(ModelNotFoundException.class);
		tmaBpoPreConfigService.getBpoPreConfigForCode("non_existent_id");
	}

	@Test
	public void testBaseSiteEmpty()
	{
		final BaseSiteModel baseSite = null;
		baseSiteService.setCurrentBaseSite(baseSite, false);
		thrown.expect(IllegalArgumentException.class);
		tmaBpoPreConfigService.getBpoPreConfigForCode("non_existent_id");
	}
}

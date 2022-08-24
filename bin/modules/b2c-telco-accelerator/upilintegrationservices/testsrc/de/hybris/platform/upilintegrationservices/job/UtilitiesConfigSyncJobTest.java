/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.job;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.upilintegrationservices.model.IsuConfigSyncCronJobModel;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link IsuConfigSyncJob}.
 *
 * @since 1911
 */
@IntegrationTest
public class UtilitiesConfigSyncJobTest extends ServicelayerTransactionalTest
{
	private static final String TEST_CATALOG = "testCatalog";
	private static final String TEST_CATALOG_VERRSION = "Staged";

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private UtilitiesConfigSyncJob utilitiesConfigSyncJob;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_product-prices.impex", "utf-8");
	}

	@Test
	public void performItemSyncTest()
	{
		final IsuConfigSyncCronJobModel isuConfigSyncCronJob = getIsuConfigSyncCronJob();
		final PerformResult result = utilitiesConfigSyncJob.perform(isuConfigSyncCronJob);
		Assert.assertNotNull(result);
	}

	private IsuConfigSyncCronJobModel getIsuConfigSyncCronJob()
	{
		final IsuConfigSyncCronJobModel isuConfigSyncCronJob = new IsuConfigSyncCronJobModel();
		isuConfigSyncCronJob
				.setAppliedCatalogVersion(catalogVersionService.getCatalogVersion(TEST_CATALOG, TEST_CATALOG_VERRSION));
		return isuConfigSyncCronJob;
	}
}

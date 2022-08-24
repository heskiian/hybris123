/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;
import de.hybris.platform.upilintegrationservices.service.UpilintegrationservicesService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link DefaultUpilintegrationservicesServiceIntegration}.
 *
 * @since 1911
 */
@IntegrationTest
public class DefaultUpilintegrationservicesServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String TEST_CATALOG = "testCatalog";
	private static final String TEST_CATALOG_VERRSION = "Staged";

	@Resource
	private UpilintegrationservicesService upilintegrationservicesService;
	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_product-prices.impex", "utf-8");
	}

	@Test
	public void testSyncIsuConfigSuccessWithHybris() throws UpilintegrationservicesException
	{
		final Boolean isIsuConfigSyncWithHybris = upilintegrationservicesService
				.syncIsuConfigWithTua(catalogVersionService.getCatalogVersion(TEST_CATALOG, TEST_CATALOG_VERRSION));
		if (isIsuConfigSyncWithHybris)
		{
			Assert.assertTrue(isIsuConfigSyncWithHybris);
		}
		else
		{
			Assert.assertFalse(isIsuConfigSyncWithHybris);
		}
	}

	@Test
	public void testSyncIsuConfigFailWithHybris() throws UpilintegrationservicesException
	{
		final Boolean isIsuConfigSyncWithHybris = upilintegrationservicesService
				.syncIsuConfigWithTua(null);
		Assert.assertNotNull(isIsuConfigSyncWithHybris);
	}

}

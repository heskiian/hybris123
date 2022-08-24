/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.ProductsFeaturesDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTest;


@IntegrationTest
public class DefaultProductsFeaturesDaoIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultProductsFeaturesDaoIntegrationTest.class);

	@Resource
	private ProductsFeaturesDao productsFeaturesDao;

	@Before
	public void setUp() throws Exception
	{
		LOG.info("Creating data for DefaultProductsFeaturesDaoIntegrationTest ...");
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		importCsv("/test/impex/test_feature-compatibility.impex", "utf-8");
		LOG.info("Finished data for DefaultProductsFeaturesDaoIntegrationTest " + (System.currentTimeMillis() - startTime)
				+ "ms");
	}

	@Test
	public void testVendorCompatibleCodeProducts()
	{
		final List<ProductModel> vendorCompatibleProducts = productsFeaturesDao.findProductsByVendorCompatibility("Apple",
				"accessoryclassification", "vendorcompatibility");
		Assert.assertEquals(1, vendorCompatibleProducts.size());
	}
}

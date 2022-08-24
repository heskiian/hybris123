/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test class for {@link DefaultTmaProductDao}
 *
 * @since 2007
 */
@IntegrationTest
public class DefaultTmaProductDaoIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PRODUCT_OFFERING_CODE = "iPhone_8";
	private static final String PRODUCT_OFFERING_GROUP_CODE = "mobile_devices";

	@Resource
	private DefaultTmaProductDao defaultTmaProductDao;


	@Before
	public void setup() throws Exception
	{
		importCsv("/test/impex/test_productOfferings.impex", "utf-8");
	}

	@Test
	public void testProductOfferingsByPattern()
	{
		final List<TmaProductOfferingModel> productOfferingList = defaultTmaProductDao
				.findProductOfferingsByPattern(TmaProductOfferingModel.CODE, PRODUCT_OFFERING_CODE);
		assertEquals(1, productOfferingList.size());
	}

	@Test
	public void testProductOfferingByPattern()
	{
		final List<TmaProductOfferingGroupModel> productOfferingGroupList = defaultTmaProductDao
				.findProductOfferingGroupsByPattern(TmaProductOfferingGroupModel.CODE, PRODUCT_OFFERING_GROUP_CODE);
		assertEquals(1, productOfferingGroupList.size());
	}

}

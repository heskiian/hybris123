/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaAddressDao;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test class for {@link DefaultTmaAddressDao}
 *
 * @since 1911
 */
@IntegrationTest
public class DefaultTmaAddressDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource(name = "tmaAddressDao")
	private TmaAddressDao addressDao;

	private static final String ADDRESS_ID = "788867AB";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_address.impex", "utf-8");
	}

	@Test
	public void testFindAddress()
	{
		AddressModel addressModel = addressDao.findAddress(ADDRESS_ID);
		assertNotNull("The address is null", addressModel);
		assertEquals("The address does not have the expected id", addressModel.getId(), ADDRESS_ID);
	}
}

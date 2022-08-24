/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.address.impl.DefaultTmaAddressService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for {@link DefaultTmaAddressService}
 *
 * @since 1911
 */
@IntegrationTest
public class DefaultTmaAddressServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource(name = "addressService")
	private TmaAddressService addressService;

	private static final String ADDRESS_ID = "788867AB";
	private static final String ADDRESS_ID_NONEXISTENT = "19987ABC";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_address.impex", "utf-8");
	}

	@Test
	public void testFindAddress()
	{
		AddressModel addressModel = addressService.findAddress(ADDRESS_ID);
		assertNotNull("The address is null", addressModel);
		assertEquals("The address does not have the expected id", addressModel.getId(), ADDRESS_ID);
	}

	@Test
	public void testDoesAddressExist()
	{
		assertTrue("This address should exist", addressService.doesAddressExist(ADDRESS_ID));
	}

	@Test
	public void testDoesAddressExistNotFound()
	{
		assertFalse("This address should not exist", addressService.doesAddressExist(ADDRESS_ID_NONEXISTENT));
	}
}

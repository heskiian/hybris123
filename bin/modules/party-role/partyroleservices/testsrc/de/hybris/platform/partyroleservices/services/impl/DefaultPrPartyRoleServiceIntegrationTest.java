/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroleservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroleservices.service.PrPartyRoleService;
import de.hybris.platform.partyroleservices.service.impl.DefaultPrPartyRoleService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for testing {@link DefaultPrPartyRoleService} methods.
 *
 * @since 2108
 */
@IntegrationTest
public class DefaultPrPartyRoleServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String PARTY_ROLE_ID = "test_employee";
	private static final String INVALID_PARTY_ROLE_ID = "invalid_party_role";

	@Resource
	private PrPartyRoleService prPartyRoleService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_partyRole.impex", "utf-8");
	}

	@Test
	public void testGetPartyRole_correctId()
	{
		final PrPartyRoleModel partyRole = getPrPartyRoleService().getPartyRole(PARTY_ROLE_ID);
		Assert.assertEquals(PARTY_ROLE_ID, partyRole.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetPartyRole_incorrectId()
	{
		getPrPartyRoleService().getPartyRole(INVALID_PARTY_ROLE_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetPartyRole_nullId()
	{
		getPrPartyRoleService().getPartyRole(null);
	}

	protected PrPartyRoleService getPrPartyRoleService()
	{
		return prPartyRoleService;
	}
}

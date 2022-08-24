/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partyservices.services.PmOrganizationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for testing {@link DefaultPmOrganizationService} methods.
 *
 * @since 2108
 */
@IntegrationTest
public class DefaultPmOrganizationServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String ORGANIZATION_ID = "test_org";
	private static final String ORGANIZATION_NAME = "Test Organization";
	private static final String INVALID_ORGANIZATION_ID = "invalid_org";

	@Resource
	private PmOrganizationService pmOrganizationService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_party.impex", "utf-8");
	}

	@Test
	public void testGetOrganization_correctId()
	{
		final PmOrganizationModel organization = getPmOrganizationService().getOrganization(ORGANIZATION_ID);
		Assert.assertEquals(ORGANIZATION_ID, organization.getId());
		Assert.assertEquals(ORGANIZATION_NAME, organization.getName());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetOrganization_incorrectId()
	{
		getPmOrganizationService().getOrganization(INVALID_ORGANIZATION_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetOrganization_nullId()
	{
		getPmOrganizationService().getOrganization(null);
	}

	protected PmOrganizationService getPmOrganizationService()
	{
		return pmOrganizationService;
	}
}

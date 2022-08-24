/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partyservices.services.PmIndividualService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Integration test class for {@link PmIndividualService}
 *
 * @since 2108
 */
public class DefaultPmIndividualServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String INDIVIDUAL_PARTY_ID = "test_empl";
	private static final String INDIVIDUAL_PARTY_GIVEN_NAME = "John";
	private static final String INDIVIDUAL_PARTY_FAMILY_NAME = "Doe";
	private static final String INVALID_PARTY_ID = "invalid_party";

	@Resource
	private PmIndividualService pmIndividualService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_party.impex", "utf-8");
	}

	@Test
	public void testFindIndividualParty()
	{
		final PmIndividualModel individualParty = getPmIndividualService().findIndividualParty(INDIVIDUAL_PARTY_ID);

		assertNotNull(individualParty);
		Assert.assertEquals(INDIVIDUAL_PARTY_ID, individualParty.getId());
		Assert.assertEquals(INDIVIDUAL_PARTY_GIVEN_NAME, individualParty.getGivenName());
		Assert.assertEquals(INDIVIDUAL_PARTY_FAMILY_NAME, individualParty.getFamilyName());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testFindIndividualPartyForIncorrectId()
	{
		getPmIndividualService().findIndividualParty(INVALID_PARTY_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindIndividualPartyNullId()
	{
		getPmIndividualService().findIndividualParty(null);
	}

	protected PmIndividualService getPmIndividualService()
	{
		return pmIndividualService;
	}
}

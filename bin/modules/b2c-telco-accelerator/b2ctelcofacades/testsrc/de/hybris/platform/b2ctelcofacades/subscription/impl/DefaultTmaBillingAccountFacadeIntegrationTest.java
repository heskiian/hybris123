/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import org.junit.Test;
import javax.annotation.Resource;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.CreateTmaBillingAccountRequest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaBillingAccountFacade;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultTmaBillingAccountFacadeIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String NEW_BILLING_ACCOUNT = "newBillingAccount";

	@Resource
	private TmaBillingAccountFacade tmaBillingAccountFacade;
	private CreateTmaBillingAccountRequest createTmaBillingAccountRequest;
	private TmaBillingAccountData tmaBillingAccountData;

	@Test
	public void testCreateBillingAccount()
	{
		givenCreateBillingAccountRequest(true);
		whenCreateBillingAccount();
		assertBillingAccountDataIsCreated();
	}

	@Test
	public void testCreateBillingAccountWithoutParentBillingAccountId()
	{
		givenCreateBillingAccountRequest(false);
		whenCreateBillingAccount();
		assertBillingAccountDataIsCreated();
	}

	@Test(expected = ModelNotFoundException.class)
	public void testBillingAccount()
	{
		deleteBillingAccount();
		deleteBillingAccount();
	}

	@Test
	public void testGenerateBillingAccount()
	{
		final TmaBillingAccountData tmaBillingAccountData = tmaBillingAccountFacade.generateBillingAccount();
		assertTrue(null != tmaBillingAccountData.getBillingAccountId());
	}
	private void deleteBillingAccount()
	{
		tmaBillingAccountFacade.deleteBillingAccount(BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
	}

	private void givenCreateBillingAccountRequest(final boolean isParentBillingAccountIdPresent)
	{
		createTmaBillingAccountRequest = new CreateTmaBillingAccountRequest();
		createTmaBillingAccountRequest.setBillingSystemId(BILLING_SYSTEM_ID);
		createTmaBillingAccountRequest.setBillingAccountId(NEW_BILLING_ACCOUNT);
		if (isParentBillingAccountIdPresent)
		{
			createTmaBillingAccountRequest.setParentBillingAccountId(BILLING_ACCOUNT_ID);
		}
	}

	private void whenCreateBillingAccount()
	{
		tmaBillingAccountData = tmaBillingAccountFacade.createBillingAccount(createTmaBillingAccountRequest);
	}

	private void assertBillingAccountDataIsCreated()
	{
		assertTrue(BILLING_SYSTEM_ID.equals(tmaBillingAccountData.getBillingSystemId()));
		assertTrue(NEW_BILLING_ACCOUNT.equals(tmaBillingAccountData.getBillingAccountId()));
	}
}

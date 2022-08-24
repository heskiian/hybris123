/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingAccountService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTmaBillingAccountServiceIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String CREATE_BILLING_ACCOUNT_ID = "BA-45499";
	private static final String PARENT_BILLING_ACCOUNT_ID = "BA-24564";

	@Resource
	private TmaBillingAccountService tmaBillingAccountService;

	@Test
	public void testFindBillingAccount()
	{
		final TmaBillingAccountModel billingAccountModel = tmaBillingAccountService.findBillingAccount(BILLING_SYSTEM_ID,
				BILLING_ACCOUNT_ID);
		assertTmaBillingAccountModel(billingAccountModel, BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindBillingAccountForNonExistingIds()
	{
		tmaBillingAccountService.findBillingAccount(NON_EXISTENT_ID, NON_EXISTENT_ID);
	}

	@Test
	public void testCreateBillingAccount()
	{
		final TmaBillingAccountModel billingAccountModel = tmaBillingAccountService.createBillingAccount(BILLING_SYSTEM_ID,
				CREATE_BILLING_ACCOUNT_ID);
		assertTmaBillingAccountModel(billingAccountModel, BILLING_SYSTEM_ID, CREATE_BILLING_ACCOUNT_ID);
	}

	@Test
	public void testCreateBillingAccountWithParent()
	{
		final TmaBillingAccountModel billingAccountModel = createTmaBillingAccountModelWithParent();
		assertTmaBillingAccountModel(billingAccountModel, BILLING_SYSTEM_ID, CREATE_BILLING_ACCOUNT_ID);
		assertEquals(PARENT_BILLING_ACCOUNT_ID, billingAccountModel.getParent().getBillingAccountId());
	}

	@Test(expected = ModelSavingException.class)
	public void testCreateDuplicateBillingAccount()
	{
		createTmaBillingAccountModelWithParent();
		createTmaBillingAccountModelWithParent();
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteBillingAccount()
	{
		final TmaBillingAccountModel existingBillingAccount = tmaBillingAccountService.findBillingAccount(BILLING_SYSTEM_ID,
				BILLING_ACCOUNT_ID);
		assertNotNull(existingBillingAccount.getBillingAccountId());
		tmaBillingAccountService.deleteBillingAccount(BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
		tmaBillingAccountService.findBillingAccount(BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteNotExistentBillingAccount()
	{
		tmaBillingAccountService.deleteBillingAccount(NON_EXISTENT_ID, NON_EXISTENT_ID);
	}

	@Test
	public void testGenerateBillingAccount()
	{
		final TmaBillingAccountModel tmaBillingAccountModel = tmaBillingAccountService.generateBillingAccount();
		assertTrue(null != tmaBillingAccountModel.getBillingAccountId());
	}


	private TmaBillingAccountModel createTmaBillingAccountModelWithParent()
	{
		return tmaBillingAccountService.createBillingAccountWithParentAccount(BILLING_SYSTEM_ID, CREATE_BILLING_ACCOUNT_ID,
				PARENT_BILLING_ACCOUNT_ID);
	}

	private void assertTmaBillingAccountModel(final TmaBillingAccountModel billingAccountModel, final String billingSystemId,
			final String billingAccountId)
	{
		assertNotNull(billingAccountModel);
		assertEquals(billingSystemId, billingAccountModel.getBillingSystemId());
		assertEquals(billingAccountId, billingAccountModel.getBillingAccountId());
	}
}

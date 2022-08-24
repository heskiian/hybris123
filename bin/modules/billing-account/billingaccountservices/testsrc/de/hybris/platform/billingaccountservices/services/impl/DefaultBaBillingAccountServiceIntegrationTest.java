/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccountservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.services.BaBillingAccountService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultBaBillingAccountServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String BILLING_ACCOUNT_ID = "000001";
	private static final String INVALID_BILLING = "123456";
	private static final String RELATED_PARTY_ID = "party_10823";
	private static final String INVALID_RELATED_PARTY_ID = "etta.berg";

	@Resource
	private BaBillingAccountService baBillingAccountService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_billingaccount.impex", "utf-8");
	}

	@Test
	public void testGetBillingAccount()
	{
		final BaAccountModel billingAccount = baBillingAccountService.getBillingAccount(BILLING_ACCOUNT_ID);
		assertBillingAccountModel(billingAccount, BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetBillingAccountForIncorrectId()
	{
		final BaAccountModel billingAccount = baBillingAccountService.getBillingAccount(INVALID_BILLING);
	}

	@Test
	public void testGetBillingAccounts()
	{
		BaBillingAccountContext billingAccountContext = new BaBillingAccountContext();
		billingAccountContext.setRelatedPartyId(null);
		List<BaAccountModel> billingAccounts = baBillingAccountService.getBillingAccounts(billingAccountContext, 0, 100);
		assertNotNull(billingAccounts);
		assertEquals(3, billingAccounts.size());
	}
	
	@Test
	public void testGetBillingAccountsForCustomer()
	{
		BaBillingAccountContext billingAccountContext = new BaBillingAccountContext();
		billingAccountContext.setRelatedPartyId(RELATED_PARTY_ID);
		List<BaAccountModel> billingAccounts = baBillingAccountService.getBillingAccounts(billingAccountContext, 0, 100);
		assertNotNull(billingAccounts);
		assertEquals(2, billingAccounts.size());
	}
	
	@Test
	public void testGetBillingAccountsForInvalidCustomer()
	{
		BaBillingAccountContext billingAccountContext = new BaBillingAccountContext();
		billingAccountContext.setRelatedPartyId(INVALID_RELATED_PARTY_ID);
		List<BaAccountModel> billingAccounts = baBillingAccountService.getBillingAccounts(billingAccountContext, 0, 100);
		assertEquals(0, billingAccounts.size());
	}

	
	private void assertBillingAccountModel(final BaAccountModel billingAccount, final String billingAccountId)
	{
		assertNotNull(billingAccount);
		assertEquals(billingAccountId, billingAccount.getId());
	}
}

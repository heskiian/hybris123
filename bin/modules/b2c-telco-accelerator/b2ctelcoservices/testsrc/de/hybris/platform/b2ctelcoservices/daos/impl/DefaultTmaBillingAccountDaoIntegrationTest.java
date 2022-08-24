/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Resource;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaBillingAccountDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


@IntegrationTest
public class DefaultTmaBillingAccountDaoIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String BILLING_ACCOUNT_ID = "BA-24564";

	private static final String BILLING_SYSTEM_ID_INVALID = "BA";
	private static final String BILLING_ACCOUNT_ID_INVALID = "BA-7652";

	@Resource
	private TmaBillingAccountDao tmaBillingAccountDao;

	@Test
	public void testFindBillingAccount()
	{
		TmaBillingAccountModel billingAccountModel = tmaBillingAccountDao.findBillingAccount(BILLING_SYSTEM_ID,
				BILLING_ACCOUNT_ID);
		Assert.assertNotNull(billingAccountModel);
		Assert.assertEquals(billingAccountModel.getBillingSystemId(), BILLING_SYSTEM_ID);
		Assert.assertEquals(billingAccountModel.getBillingAccountId(), BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindBillingAccountForWrongBillingSystem()
	{
		tmaBillingAccountDao.findBillingAccount(BILLING_SYSTEM_ID_INVALID, BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindBillingAccountForWrongBillingAccount()
	{
		tmaBillingAccountDao.findBillingAccount(BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID_INVALID);
	}
}

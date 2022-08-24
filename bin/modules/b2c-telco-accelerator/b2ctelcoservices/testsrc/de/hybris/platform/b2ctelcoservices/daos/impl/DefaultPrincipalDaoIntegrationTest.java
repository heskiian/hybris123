/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import org.junit.Test;
import javax.annotation.Resource;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.PrincipalDao;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@IntegrationTest
public class DefaultPrincipalDaoIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	@Resource
	private PrincipalDao principalDao;

	@Test(expected = ModelNotFoundException.class)
	public void testFoundNothing()
	{
		principalDao.findPrincipalByUid(NON_EXISTENT_ID);
	}

	@Test
	public void testFoundOnePrincipal()
	{
		final PrincipalModel principal = principalDao.findPrincipalByUid(CUSTOMER_UID);
		assertNotNull("Is null", principal);
		assertEquals("The customer hasn't the expected uid", CUSTOMER_UID, principal.getUid());
	}
}

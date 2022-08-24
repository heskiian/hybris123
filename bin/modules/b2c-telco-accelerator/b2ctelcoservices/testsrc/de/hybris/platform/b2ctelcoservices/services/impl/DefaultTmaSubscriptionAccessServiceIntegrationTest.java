/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionAccessServiceIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String NEW_SUBSCRIBER_IDENTITY = "02015554321";
	private static final TmaAccessType UPDATE_ACCESS_TYPE = TmaAccessType.BENEFICIARY;

	@Resource
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;
	private TmaSubscriptionAccessModel model;

	@Test
	public void testUpdate()
	{
		final TmaSubscriptionAccessModel expectedModel = updateSubscriptionAccess(CUSTOMER_UID, BILLING_SYSTEM_ID,
				SUBSCRIBER_IDENTITY, UPDATE_ACCESS_TYPE);
		assertTrue(UPDATE_ACCESS_TYPE.equals(expectedModel.getAccessType()));
	}

	@Test(expected = ModelNotFoundException.class)
	public void testUpdateWhenNotFoundSubscriptionAccess()
	{
		updateSubscriptionAccess(CUSTOMER_UID, BILLING_SYSTEM_ID, NON_EXISTENT_ID, ACCESS_TYPE);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDelete()
	{
		deleteSubscriptionAccess(CUSTOMER_UID, BILLING_SYSTEM_ID, NEW_SUBSCRIBER_IDENTITY);
		model = findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID, BILLING_SYSTEM_ID, NEW_SUBSCRIBER_IDENTITY);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteWhenNotFoundSubscriptionAccess()
	{
		deleteSubscriptionAccess(CUSTOMER_UID, NON_EXISTENT_ID, NEW_SUBSCRIBER_IDENTITY);
	}

	private TmaSubscriptionAccessModel findSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity)
	{
		return tmaSubscriptionAccessService.getSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId,
				subscriberIdentity);
	}

	private TmaSubscriptionAccessModel updateSubscriptionAccess(final String principalUid, final String billingSystemId,
			final String subscriberIdentity, final TmaAccessType updateAccessType)
	{
		return tmaSubscriptionAccessService.updateSubscriptionAccess(principalUid, billingSystemId, subscriberIdentity,
				updateAccessType);
	}

	private void deleteSubscriptionAccess(final String principalUid, final String billingSystemId, final String subscriberIdentity)
	{
		tmaSubscriptionAccessService.deleteSubscriptionAccess(principalUid, billingSystemId, subscriberIdentity);
	}
}

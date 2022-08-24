/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.commons;

import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;


public class BaseCustomerInventoryIntegrationTest extends ServicelayerTransactionalTest
{
	protected static final String CUSTOMER_UID = "customer001";
	protected static final String BILLING_SYSTEM_ID = "IN";
	protected static final String BILLING_SUBSCRIPTION_ID = "billingSubscriptionId1";
	protected static final String SUBSCRIBER_IDENTITY = "02012344321";
	protected static final String BILLING_ACCOUNT_ID = "BA-24564";
	protected static final String NON_EXISTENT_ID = "NON_EXISTENT_ID";
	protected static final TmaAccessType ACCESS_TYPE = TmaAccessType.OWNER;
	protected static final String SUBSCRIBED_PRODUCT_ID = "1110000007";
	protected static final String PSCV_VALUE = "-1";
	protected static final String SUBSCRIBER_IDENTITY_TEST = "TEST_ID";
	protected static final ArrayList<String> SUBSCRIPTION_BASES = new ArrayList<>(
			Arrays.asList("02012344325", " 02012344321", "02015554321", "01319845376", " 0201234432111"));
	protected static final String SUBSCRIBER_IDENTITY1 = "01319845376";
	protected static final String SUBSCRIBER_IDENTITY2 = "02012344325";
	protected static final String SUBSCRIBER_IDENTITY3 = "0201234432111";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_customerInventory.impex", "utf-8");
	}
}

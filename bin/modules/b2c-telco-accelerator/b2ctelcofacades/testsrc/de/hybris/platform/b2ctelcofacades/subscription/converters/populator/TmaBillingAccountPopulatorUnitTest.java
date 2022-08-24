/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;

import static org.junit.Assert.assertTrue;


@UnitTest
public class TmaBillingAccountPopulatorUnitTest
{
	private TmaBillingAccountModel source;
	private TmaBillingAccountData target;
	private TmaBillingAccountPopulator populator;

	@Before
	public void before()
	{
		populator = new TmaBillingAccountPopulator();
		target = new TmaBillingAccountData();
	}

	@Test
	public void testPopulateBillingAccount()
	{
		setUpSource("BA-24564", "IN");
		populator.populate(source, target);

		assertTrue("Has the expected billing account id", source.getBillingAccountId().equals(target.getBillingAccountId()));
		assertTrue("Has the expected system id", source.getBillingSystemId().equals(target.getBillingSystemId()));
	}

	private void setUpSource(String billingAccountId, String billingSystemId)
	{
		source = new TmaBillingAccountModel();
		source.setBillingAccountId(billingAccountId);
		source.setBillingSystemId(billingSystemId);
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class TmaSubscriptionBasePopulatorUnitTest
{
	private TmaSubscriptionBaseModel source;
	private TmaSubscriptionBaseData target;
	private TmaSubscriptionBasePopulator populator;
	@Mock
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter;
	@Mock
	private Converter<TmaSubscriptionBaseModel, ProductData> tmaSubscriptionBasePoRefConverter;

	@Before
	public void before()
	{
		populator = new TmaSubscriptionBasePopulator(tmaSubscriptionAccessRefConverter, tmaSubscriptionBasePoRefConverter);
		target = new TmaSubscriptionBaseData();
	}

	@Test
	public void testPopulateBillingAccount()
	{
		setUpSource("BA-24564", "IN", "02012344321");
		populator.populate(source, target);

		assertTrue("Has the expected subscriberIdentity: ",
				source.getSubscriberIdentity().equals(target.getSubscriberIdentity()));
		assertTrue("Has the expected billingSystemId: ",
				source.getBillingSystemId().equals(target.getBillingSystemId()));
		assertTrue("Has the expected billing account id: ",
				source.getBillingAccount().getBillingAccountId().equals(target.getTmaBillingAccountData().getBillingAccountId()));
		assertTrue("Has the expected system id: ",
				source.getBillingSystemId().equals(target.getTmaBillingAccountData().getBillingSystemId()));
	}

	@Test
	public void testPopulateBillingAccountNegative()
	{
		setUpSource(null, "IN", "02012344321");
		populator.populate(source, target);
		assertTrue("Has the expected subscriberIdentity: ",
				source.getSubscriberIdentity().equals(target.getSubscriberIdentity()));
		assertTrue("Has the expected billingSystemId: ",
				source.getBillingSystemId().equals(target.getBillingSystemId()));
		assertTrue("Has the expected billing account: ",
				(null == source.getBillingAccount()));
	}

	private void setUpSource(final String billingAccountId, final String billingSystemId, final String subscriberIdentity)
	{
		source = new TmaSubscriptionBaseModel();
		source.setSubscriberIdentity(subscriberIdentity);
		source.setBillingSystemId(billingSystemId);
		if (StringUtils.isNotEmpty(billingAccountId))
		{
			final TmaBillingAccountModel tmaBillingAccountModel = new TmaBillingAccountModel();
			tmaBillingAccountModel.setBillingAccountId(billingAccountId);
			tmaBillingAccountModel.setBillingSystemId(billingSystemId);
			source.setBillingAccount(tmaBillingAccountModel);
		}
	}


}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionSelectionData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class TmaSubscriptionSelectionPopulatorUnitTest
{
	private static final String BPO_CODE = "bpo_code";
	private static final String PRODUCT_CODE = "product_code";
	private static final String SUBSCRIBER_ID = "subscriber_id";

	private TmaSubscriptionSelectionPopulator populator;
	private TmaSubscriptionBaseModel source;
	private TmaSubscriptionSelectionData target;

	@Before
	public void setUp()
	{
		this.populator = new TmaSubscriptionSelectionPopulator();
		this.target = new TmaSubscriptionSelectionData();
	}

	@Test
	public void testPopulateWithoutServices()
	{
		givenSourceWithServices(Collections.emptySet());
		whenPopulateIsRun();
		thenDataIsPopulated(null);
	}

	@Test
	public void testPopulateWithServices()
	{
		TmaSubscribedProductModel addonService = createService(TmaServiceType.ADD_ON);
		TmaSubscribedProductModel mainTariff = createService(TmaServiceType.TARIFF_PLAN);

		givenSourceWithServices(Arrays.asList(addonService, mainTariff).stream().collect(Collectors.toSet()));
		whenPopulateIsRun();
		thenDataIsPopulated(mainTariff);
	}

	private TmaSubscribedProductModel createService(final TmaServiceType serviceType)
	{
		TmaSubscribedProductModel subscribedProductModel = new TmaSubscribedProductModel();
		subscribedProductModel.setServiceType(serviceType);
		subscribedProductModel.setBundledProductCode(BPO_CODE);
		subscribedProductModel.setProductCode(PRODUCT_CODE);
		return subscribedProductModel;
	}

	private void givenSourceWithServices(final Set<TmaSubscribedProductModel> services)
	{
		source = new TmaSubscriptionBaseModel();
		source.setSubscriberIdentity(SUBSCRIBER_ID);
		source.setSubscribedProducts(services);
	}

	private void whenPopulateIsRun()
	{
		populator.populate(source, target);
	}

	private void thenDataIsPopulated(final TmaSubscribedProductModel mainTariff)
	{
		assertEquals(source.getSubscriberIdentity(), target.getSubscriberIdentity());
		assertEquals(mainTariff == null ? "" : mainTariff.getBundledProductCode(), target.getBpoCode());
		assertEquals(mainTariff == null ? "" : mainTariff.getProductCode(), target.getProductCode());
	}
}

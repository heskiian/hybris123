/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionTermServiceIntegrationTest extends ServicelayerTest
{
	private static final String SPO_CODE1 = "sombreroS";
	private static final String BPO_CODE1 = "homeDeal";
	private static final int SUBSCRIPTION_TERMS_COUNT1 = 2;

	private static final String SPO_CODE2 = "tapasL";
	private static final String BPO_CODE2 = "mobileDeal";
	private static final int SUBSCRIPTION_TERMS_COUNT2 = 3;

	private static final int ALL_SUBSCRIPTION_TERMS_COUNT = 3;

	@Resource
	private TmaSubscriptionTermService tmaSubscriptionTermService;

	@Resource
	private DefaultTmaPoService tmaPoService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_subscriptionTerms.impex", "utf-8");
	}

	@Test
	public void testGetAllSubscriptionTerms()
	{
		assertEquals(ALL_SUBSCRIPTION_TERMS_COUNT, tmaSubscriptionTermService.getAllSubscriptionTerms().size());
	}

	@Test
	public void testGetApplicableSubscriptionTermsFromSpoOnly()
	{
		Set<SubscriptionTermModel> applicableSubscriptionTerms = getSubscriptionTermsFor(SPO_CODE1, BPO_CODE1);
		assertEquals(SUBSCRIPTION_TERMS_COUNT1, applicableSubscriptionTerms.size());
	}

	@Test
	public void testGetApplicableSubscriptionTermsFromSpoAndBpo()
	{
		Set<SubscriptionTermModel> applicableSubscriptionTerms = getSubscriptionTermsFor(SPO_CODE2, BPO_CODE2);
		assertEquals(SUBSCRIPTION_TERMS_COUNT2, applicableSubscriptionTerms.size());
	}

	private Set<SubscriptionTermModel> getSubscriptionTermsFor(final String spoCode, final String bpoCode)
	{
		TmaProductOfferingModel spo = tmaPoService.getPoForCode(spoCode);
		TmaBundledProductOfferingModel bpo = (TmaBundledProductOfferingModel) tmaPoService.getPoForCode(bpoCode);
		return tmaSubscriptionTermService
				.getApplicableSubscriptionTerms(spo, bpo, TmaProcessType.ACQUISITION);
	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Test class for {@link TmaFixedBpoPrepareInterceptor}
 *
 * @since 2105
 */
@UnitTest
public class TmaFixedBpoPrepareInterceptorUnitTest
{
	private static final String FIXED_BPO_ID = "fixedBPO";
	private static final String SPO_ID_1 = "spo1";
	private static final String SPO_ID_2 = "spo2";
	private static final int BPO_OPTIONS_SIZE = 2;
	private static final long DEFAULT_BPO_OPTIONS_VALUE = 1;
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	@Mock
	private InterceptorContext interceptorContext;
	@Mock
	private SessionService sessionService;
	@Mock
	private Session session;

	private TmaFixedBpoPrepareInterceptor fixedBpoPrepareInterceptor;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.fixedBpoPrepareInterceptor = new TmaFixedBpoPrepareInterceptor(sessionService);
		when(sessionService.getCurrentSession()).thenReturn(session);
		when(session.getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE)).thenReturn(Boolean.FALSE);
	}

	@Test
	public void testPrepareInterceptor()
	{
		final TmaFixedBundledProductOfferingModel fixedBundledProductOffering = new TmaFixedBundledProductOfferingModel();
		fixedBundledProductOffering.setCode(FIXED_BPO_ID);
		fixedBundledProductOffering.setChildren(new HashSet<>());
		fixedBundledProductOffering.getChildren().add(buildProductOffering(SPO_ID_1));
		fixedBundledProductOffering.getChildren().add(buildProductOffering(SPO_ID_2));

		fixedBpoPrepareInterceptor.onPrepare(fixedBundledProductOffering, interceptorContext);
		Assert.assertEquals(BPO_OPTIONS_SIZE, fixedBundledProductOffering.getBpoOptions().size());
		
		fixedBundledProductOffering.getBpoOptions().forEach(bundledProdOfferOption ->
				{
					Assert.assertEquals(DEFAULT_BPO_OPTIONS_VALUE, (long) bundledProdOfferOption.getLowerLimit());
					Assert.assertEquals(DEFAULT_BPO_OPTIONS_VALUE, (long) bundledProdOfferOption.getUpperLimit());
					Assert.assertEquals(DEFAULT_BPO_OPTIONS_VALUE, (long) bundledProdOfferOption.getDefault());
				}
		);

		final Set<String> productIds = fixedBundledProductOffering.getBpoOptions().stream()
				.map(tmaBundledProdOfferOption -> tmaBundledProdOfferOption.getProductOffering().getCode())
				.collect(Collectors.toSet());
		Assert.assertTrue(productIds.contains(SPO_ID_1));
		Assert.assertTrue(productIds.contains(SPO_ID_2));
	}

	private TmaProductOfferingModel buildProductOffering(final String poId)
	{
		final TmaProductOfferingModel productOffering = new TmaProductOfferingModel();
		productOffering.setCode(poId);
		return productOffering;
	}
}

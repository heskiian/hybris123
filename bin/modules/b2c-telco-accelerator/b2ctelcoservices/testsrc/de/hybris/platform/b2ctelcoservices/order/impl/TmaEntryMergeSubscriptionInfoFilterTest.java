/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@UnitTest
public class TmaEntryMergeSubscriptionInfoFilterTest
{

	private TmaEntryMergeSubscriptionInfoFilter productsFilter;

	@Mock
	private AbstractOrderEntryModel candidateCartEntry;
	@Mock
	private AbstractOrderEntryModel targetCartEntry;
	@Mock
	private TmaCartSubscriptionInfoModel subscriptionInfo;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		productsFilter = new TmaEntryMergeSubscriptionInfoFilter();
	}

	@Test
	public void cartEntriesShouldNotBeMerged()
	{
		when(targetCartEntry.getSubscriptionInfo()).thenReturn(subscriptionInfo);
		when(subscriptionInfo.getSubscribedProductAction()).thenReturn(TmaSubscribedProductAction.ADD);

		Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);

		assertFalse(apply);
	}

	@Test
	public void cartEntriesShouldBeMerged()
	{
		Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);

		assertTrue(apply);
	}
}

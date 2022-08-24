/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test for @{TmaEntryMergeRegionFilter}
 *
 * @since 2003
 */
@UnitTest
public class TmaEntryMergeRegionFilterTest
{
	private TmaEntryMergeRegionFilter productsFilter;
	private static String INVALID_ISO_CODE = "invalidIsoCode";

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		productsFilter = new TmaEntryMergeRegionFilter();
	}

	@Test
	public void cartEntriesShouldNotBeMerged()
	{
		final AbstractOrderEntryModel candidateCartEntry = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel targetCartEntry = new AbstractOrderEntryModel();
		final RegionModel newcartentryRegion = new RegionModel();
		final RegionModel oldcartentryRegion = new RegionModel();
		candidateCartEntry.setRegion(newcartentryRegion);
		targetCartEntry.setRegion(oldcartentryRegion);
		newcartentryRegion.setIsocode(INVALID_ISO_CODE);
		final Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);
		assertFalse(apply);
	}

	@Test
	public void cartEntriesShouldBeMerged()
	{
		final AbstractOrderEntryModel candidateCartEntry = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel targetCartEntry = new AbstractOrderEntryModel();
		final RegionModel newcartentryRegion = new RegionModel();
		final RegionModel oldcartentryRegion = new RegionModel();
		candidateCartEntry.setRegion(newcartentryRegion);
		targetCartEntry.setRegion(oldcartentryRegion);
		final Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);
		assertTrue(apply);
	}
}

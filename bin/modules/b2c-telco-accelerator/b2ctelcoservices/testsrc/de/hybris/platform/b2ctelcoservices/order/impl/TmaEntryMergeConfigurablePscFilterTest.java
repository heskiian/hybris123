/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@UnitTest
public class TmaEntryMergeConfigurablePscFilterTest
{

	private TmaEntryMergeConfigurablePscFilter productsFilter;

	@Mock
	private TmaPoService poService;
	@Mock
	private AbstractOrderEntryModel candidateCartEntry;
	@Mock
	private AbstractOrderEntryModel targetCartEntry;
	@Mock
	private TmaProductOfferingModel product;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		productsFilter = new TmaEntryMergeConfigurablePscFilter(poService);
		when(targetCartEntry.getProduct()).thenReturn(product);
	}

	@Test
	public void cartEntriesShouldNotBeMerged()
	{
		final TmaProductSpecCharacteristicModel productSpecCharacteristic = mock(TmaProductSpecCharacteristicModel.class);
		when(poService.getConfigurableProductSpecCharacteristics(product))
				.thenReturn(Collections.singleton(productSpecCharacteristic));

		Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);

		assertFalse(apply);
	}

	@Test
	public void cartEntriesShouldBeMerged()
	{
		when(poService.getConfigurableProductSpecCharacteristics(product)).thenReturn(Collections.emptySet());

		Boolean apply = productsFilter.apply(candidateCartEntry, targetCartEntry);

		assertTrue(apply);
	}
}

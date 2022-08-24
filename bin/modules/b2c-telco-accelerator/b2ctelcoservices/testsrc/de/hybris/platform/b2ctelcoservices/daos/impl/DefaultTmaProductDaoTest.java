/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link DefaultTmaProductDao}
 *
 * @since 2007
 */
@UnitTest
public class DefaultTmaProductDaoTest
{
	private static final String PRODUCT_OFFERING = "productOffering";
	private static final String PRODUCT_GROUP = "productGroup";

	@InjectMocks
	private DefaultTmaProductDao tmaProductDao;
	@Mock
	FlexibleSearchService flexibleSearchService;
	@Mock
	SearchResult result;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		tmaProductDao.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testProductOfferingsByPatternh()
	{
		final TmaProductOfferingModel tmaProductOffering = Mockito.mock(TmaProductOfferingModel.class);
		Mockito.when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(result);
		Mockito.when(result.getResult()).thenReturn(Arrays.asList(tmaProductOffering));
		final List<TmaProductOfferingModel> productOfferings = tmaProductDao
				.findProductOfferingsByPattern(TmaProductOfferingModel.CODE, PRODUCT_OFFERING);
		assertEquals(1, productOfferings.size());
	}

	@Test
	public void testProductOfferingGroupsByPattern()
	{
		final TmaProductOfferingGroupModel tmaProductOffering = Mockito.mock(TmaProductOfferingGroupModel.class);
		Mockito.when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(result);
		Mockito.when(result.getResult()).thenReturn(Arrays.asList(tmaProductOffering));
		final List<TmaProductOfferingGroupModel> productOfferingGroups = tmaProductDao
				.findProductOfferingGroupsByPattern(TmaProductOfferingGroupModel.CODE, PRODUCT_GROUP);
		assertEquals(1, productOfferingGroups.size());
	}
}

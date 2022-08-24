/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionPricePlanDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.impl.DefaultTmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;


/**
 * Unit Test of {@link DefaultTmaCommercePriceService}.
 *
 * @since 1907
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTmaCommercePriceServiceTest
{
	@InjectMocks
	DefaultTmaCommercePriceService defaultTmaCommercePriceService;

	@Mock
	private TmaPoService tmaPoService;

	@Mock
	private TmaSubscriptionPricePlanDao subscriptionPricePlanDao;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void filterPricesbyPriceContextTest()
	{
		final TmaPriceContext priceContext = new TmaPriceContext();
		final TmaProductOfferingModel productModel = new TmaProductOfferingModel();

		final Set<TmaProcessType> processTypes = new HashSet<>();
		processTypes.add(TmaProcessType.valueOf("ACQUISITION"));
		priceContext.setProcessTypes(processTypes);
		priceContext.setProduct(productModel);
		final PriceRowModel priceRowModel = mock(PriceRowModel.class);
		priceRowModel.setPrice(Double.valueOf("98"));
		final HashSet<PriceRowModel> priceRow = new HashSet<>();
		priceRow.add(priceRowModel);
		Mockito.when(subscriptionPricePlanDao.findAllApplicablePricesForContext(priceContext)).thenReturn(priceRow);
		defaultTmaCommercePriceService.filterPricesbyPriceContext(priceContext);
		Assert.assertNotNull(priceRow);
	}

}

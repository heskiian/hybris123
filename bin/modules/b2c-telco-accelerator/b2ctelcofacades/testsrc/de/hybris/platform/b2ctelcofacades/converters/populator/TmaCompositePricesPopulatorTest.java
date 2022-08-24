/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Test suite for {@link TmaCompositePricesPopulator}
 *
 * @since 2007
 */
@UnitTest
public class TmaCompositePricesPopulatorTest
{
	@Mock
	private Converter<PriceRowModel, PriceData> tmaPriceDataConverter;

	private TmaCompositePricesPopulator compositePricesPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		compositePricesPopulator = new TmaCompositePricesPopulator(tmaPriceDataConverter);
	}

	@Test
	public void testPopulate()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		final Collection<PriceRowModel> priceRows = new ArrayList<>();
		final PriceRowModel price = mock(PriceRowModel.class);
		priceRows.add(price);
		given(source.getEurope1Prices()).willReturn(priceRows);
		final ProductData target = new ProductData();
		compositePricesPopulator.populate(source, target);
		Assert.assertEquals(1, target.getPrices().size());
	}

	@Test
	public void testEmptyPriceRows()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		given(source.getEurope1Prices()).willReturn(null);
		final ProductData target = new ProductData();
		compositePricesPopulator.populate(source, target);
		Assert.assertEquals(null, target.getPrices());
	}

	@Test
	public void testSourceNotInstaceofPO()
	{
		final ProductModel source = mock(ProductModel.class);
		final ProductData target = new ProductData();
		compositePricesPopulator.populate(source, target);
		Assert.assertEquals(null, target.getPrices());
	}
}

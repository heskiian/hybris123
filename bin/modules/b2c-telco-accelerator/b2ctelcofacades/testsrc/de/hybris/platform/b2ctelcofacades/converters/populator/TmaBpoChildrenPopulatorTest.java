/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaBundledProdOfferOptionData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test suite for {@link TmaBpoChildrenPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaBpoChildrenPopulatorTest
{
	@Mock
	private Converter<ProductModel, ProductData> productConverter;

	@Mock
	private Converter<TmaBundledProdOfferOptionModel, TmaBundledProdOfferOptionData> bpoOptionConverter;

	@Mock
	private TmaPoService tmaPoService;

	private TmaBpoChildrenPopulator bpoChildrenPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		bpoChildrenPopulator = new TmaBpoChildrenPopulator(bpoOptionConverter, tmaPoService);
		bpoChildrenPopulator.setProductConverter(productConverter);
	}

	@Test
	public void testPopulate()
	{
		final TmaBundledProductOfferingModel source = mock(TmaBundledProductOfferingModel.class);
		final Set<TmaProductOfferingModel> childProducts = new HashSet<>();
		final TmaProductOfferingModel childProduct1 = mock(TmaProductOfferingModel.class);
		childProducts.add(childProduct1);
		given(source.getChildren()).willReturn(childProducts);
		when(tmaPoService.getBundledProdOfferOptionFor(source, childProduct1)).thenReturn(Optional.empty());
		final ProductData target = new ProductData();
		bpoChildrenPopulator.populate(source, target);
		Assert.assertEquals(1, target.getChildren().size());
	}

	@Test
	public void testEmptyChildrenOfBPO()
	{
		final TmaBundledProductOfferingModel source = mock(TmaBundledProductOfferingModel.class);
		given(source.getChildren()).willReturn(null);
		final ProductData target = new ProductData();
		bpoChildrenPopulator.populate(source, target);
		Assert.assertEquals(null, target.getChildren());
	}

	@Test
	public void testSourceNotInstaceofBPO()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		final ProductData target = new ProductData();
		bpoChildrenPopulator.populate(source, target);
		Assert.assertEquals(null, target.getChildren());
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants.options;

import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


/**
 * Unit test for {@link TmaVariantOptionDataUrlPopulator}.
 *
 * @since 1810
 */
@UnitTest
public class TmaVariantOptionDataUrlPopulatorTest
{
	private TmaVariantOptionDataUrlPopulator urlPopulator;
	private VariantOptionData variantOptionData;
	@Mock
	private TmaPoVariantModel poVariant;
	@Mock
	private UrlResolver<ProductModel> urlResolver;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.urlPopulator = new TmaVariantOptionDataUrlPopulator();
		this.urlPopulator.setProductModelUrlResolver(urlResolver);
		this.variantOptionData = new VariantOptionData();
	}

	@Test
	public void testUrlIsPopulated()
	{
		final String url = "/variant/v1";
		givenUrl(url);
		whenUrlIsPopulated();
		thenUrlIsSet(url);
	}

	private void givenUrl(final String url)
	{
		Mockito.when(urlResolver.resolve(poVariant)).thenReturn(url);
	}

	private void whenUrlIsPopulated()
	{
		this.urlPopulator.populate(poVariant, variantOptionData);
	}

	private void thenUrlIsSet(final String expectedUrl)
	{
		Assert.assertEquals(expectedUrl, variantOptionData.getUrl());
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.url;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit tests for {@link TmaProductOfferingModelUrlResolver}.
 *
 * @since 1810
 */
@UnitTest
public class TmaProductOfferingModelUrlResolverTest
{
	private TmaProductOfferingModelUrlResolver urlResolver;
	@Mock
	private TmaSimpleProductOfferingModel spo;
	@Mock
	private TmaPoVariantModel variant;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.urlResolver = new TmaProductOfferingModelUrlResolver();
	}

	@Test
	public void givenProductOffering_thenProductOfferingIsBaseProduct()
	{
		Assert.assertEquals(spo, urlResolver.getBaseProduct(spo));
	}

	@Test
	public void givenVariant_thenVariantBasePoIsBaseProduct()
	{
		Mockito.when(variant.getTmaBasePo()).thenReturn(spo);
		Assert.assertEquals(spo, urlResolver.getBaseProduct(variant));
	}

	@Test
	public void givenVariantWithoutBasePo_thenVariantIsBaseProduct()
	{
		Assert.assertEquals(variant, urlResolver.getBaseProduct(variant));
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.product.DefaultProductFacadeIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.acceleratorfacades.cart.action.impl.DefaultCartEntryActionFacadeIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultProductFacadeIntegrationTest.class)
public class TmaProductFacadeIntegrationTest extends DefaultProductFacadeIntegrationTest
{
	@Test
	@Override
	public void testGetProductForCodeImagesAndCategories()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetProductForCodeBasic()
	{
		assertTrue("this should NOT fail", true);
	}
}

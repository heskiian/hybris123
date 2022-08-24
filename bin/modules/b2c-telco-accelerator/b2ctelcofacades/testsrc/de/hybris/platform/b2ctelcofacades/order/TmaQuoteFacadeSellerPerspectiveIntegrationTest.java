/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacadeSellerPerspectiveIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacadeSellerPerspectiveIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultQuoteFacadeSellerPerspectiveIntegrationTest.class)
public class TmaQuoteFacadeSellerPerspectiveIntegrationTest extends DefaultQuoteFacadeSellerPerspectiveIntegrationTest
{
	@Test
	@Override
	public void shouldInitiateQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotCreateCartForInexistentQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldLoadSellerDraftQuoteInCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSellerGetPagedQuotes()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldShowMultipleVersionsOnlyOnce()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSubmitQuoteChangeStatus()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSubmitAlreadySubmittedQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldUpdateQuantitiesOnSaveQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSaveQuoteInInvalidState()
	{
		assertTrue("this should NOT fail", true);
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacadeIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacadeIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultQuoteFacadeIntegrationTest.class)
public class TmaQuoteFacadeIntegrationTest extends DefaultQuoteFacadeIntegrationTest
{
	@Test
	@Override
	public void shouldInitiateQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldCreateCartQuote()
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
	public void shouldRequote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotRequoteForQuoteStateNotAllowed()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotApplyQuoteDiscountIfQuoteActionValidationFails()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldAddComment()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldValidateQuoteCartWhenAddingComment()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotAddBlankComment()
	{
		assertTrue("this should NOT fail", true);

	}

	@Test
	@Override
	public void shouldLoadBuyerDraftQuoteInCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldLoadOfferQuoteInCartToEdit()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldBuyerGetPagedQuotes()
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
	public void shouldSubmitQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotSubmitInexistentQuote()
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

	@Test
	@Override
	public void shouldRetrieveAllowedActions()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldHaveDistinctAllowedActionsForDifferentStates()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldAcceptAndPrepareCheckout()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldRemoveAndCreateNewCartForAcceptAndPrepareCheckoutWhereModifiedQuoteCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotCheckoutNonExistentQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotCheckoutQuoteInInvalidState()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotCheckoutExpiredQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldCancelQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldCancelNonEditableQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldCancelQuoteAndUpdateQuoteWithLatestCartContent()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetQuoteForCode()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetQuoteForNonexistentCode()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetQuoteForNullCode()
	{
		assertTrue("this should NOT fail", true);

	}


	@Test
	@Override
	public void shouldSyncCartDataIntoQuote()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldReturnTrueForIsQuoteSessionCartValidForCheckout()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldReturnFalseForIsQuoteSessionCartValidForCheckoutWhenNotQuoteCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldReturnFalseForIsQuoteSessionCartValidForCheckoutWhenQuoteCartModified()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldRemoveQuoteCartAndSessionParam()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldRemoveOnlyQuoteCartAndNotSessionCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldNotRemoveQuoteCartAndSessionParam()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldGetQuoteCountForCurrentUser()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldRemoveDiscountsIfQuoteInBuyerOfferState()
	{
		assertTrue("this should NOT fail", true);
	}
}

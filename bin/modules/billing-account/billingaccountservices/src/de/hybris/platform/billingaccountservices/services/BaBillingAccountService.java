/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccountservices.services;

import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Service responsible for handling {@link BaAccountModel}  related operations.
 *
 * @since 2105
 */
public interface BaBillingAccountService
{
	/**
	 * Returns a {@link BaAccountModel} for the given id.
	 *
	 * @param billingAccountId
	 * 		identifier of {@link BaAccountModel}
	 * @return the {@link BaAccountModel} found.
	 * @throws {@link
	 *      ModelNotFoundException}
	 * 		if no billing account is found.
	 */
	BaAccountModel getBillingAccount(final String billingAccountId);

	/**
	 * Returns a list of {@link BaAccountModel}s for the given context.
	 *
	 * @param billingAccountContext
	 * 		the billing account context, used for filtering of accounts.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned billing accounts.
	 * @return the List of {@link BaAccountModel}s found for the given context.
	 */
	List<BaAccountModel> getBillingAccounts(final BaBillingAccountContext billingAccountContext, final Integer offset,
			final Integer limit);

	/**
	 * Retrieves the total number of billing accounts found for a given billingAccountContext.
	 *
	 * @param billingAccountContext
	 * 		the billing account context.
	 * @return the number of products found.
	 */
	Integer getNumberOfBillingAccountsFor(final BaBillingAccountContext billingAccountContext);

	/**
	 * Saves the given {@link BaAccountModel}.
	 *
	 * @param billingAccountModel
	 * 		the given billing account.
	 */
	void saveBillingAccount(final BaAccountModel billingAccountModel);

	/**
	 * Creates an instance of {@link BaAccountModel} subtype.
	 *
	 * @param billingAccountClass
	 * 		the class of the {@link BaAccountModel} subtype.
	 * @return the newly created model.
	 */
	BaAccountModel createBillingAccount(final Class billingAccountClass);

	/**
	 * Removes the given {@link BaAccountModel}
	 *
	 * @param billingAccount
	 * 		the billingAccount.
	 */
	void removeBillingAccount(final BaAccountModel billingAccount);
}

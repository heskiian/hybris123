/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.daos;

import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * Data access object for {@link BaAccountModel}s.
 *
 * @since 2105
 */
public interface BaBillingAccountDao extends GenericDao<BaBillingAccountModel>
{
	/**
	 * Returns a list of {@link BaAccountModel}s for the given context.
	 *
	 * @param billingAccountContext
	 * 		the billing account context
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
}

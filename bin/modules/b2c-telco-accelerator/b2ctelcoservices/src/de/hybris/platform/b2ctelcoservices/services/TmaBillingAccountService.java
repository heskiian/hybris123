/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;


/**
 * Service responsible for manipulating {@link TmaBillingAccountModel} objects.
 *
 * @since 6.6
 */
public interface TmaBillingAccountService
{
	/**
	 * Retrieves the {@link TmaBillingAccountModel} associated to a given billing account id and a billing system id.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 * @return the {@link TmaBillingAccountModel} if any exists
	 */
	TmaBillingAccountModel findBillingAccount(final String billingSystemId, final String billingAccountId);

	/**
	 * Creates a new {@link TmaBillingAccountModel} with given details.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 * @return the newly created {@link TmaBillingAccountModel}
	 */
	TmaBillingAccountModel createBillingAccount(final String billingSystemId, final String billingAccountId);

	/**
	 * Creates a new {@link TmaBillingAccountModel} with given details and attaches it to the given parent account.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 * @param parentBillingAccountId
	 *           the billing account identifier of the parent billing account from the billing system, to which the newly
	 *           created billing account is associated
	 * @return the newly created {@link TmaBillingAccountModel}
	 */
	TmaBillingAccountModel createBillingAccountWithParentAccount(final String billingSystemId, final String billingAccountId,
			final String parentBillingAccountId);

	/**
	 * Deletes the {@link TmaBillingAccountModel} and it's assciated {@link TmaSubscriptionBaseModel}s. The billing
	 * account is identified by the given billing system id and the billing account id.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 */
	void deleteBillingAccount(final String billingSystemId, final String billingAccountId);


	/**
	 * Creates a new {@link TmaBillingAccountModel} with key generators
	 *
	 * @return the newly created {@link TmaBillingAccountModel}
	 */
	TmaBillingAccountModel generateBillingAccount();
}

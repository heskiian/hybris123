/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription;


import de.hybris.platform.b2ctelcofacades.data.CreateTmaBillingAccountRequest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;


/**
 * Facade responsible for manipulating billing accounts.
 *
 * @since 6.6
 */
public interface TmaBillingAccountFacade
{
	/**
	 * Creates a new {@link TmaBillingAccountData} with given details.
	 *
	 * @param billingAccountRequest
	 *           the billing account details used for creating a new {@link TmaBillingAccountData}
	 * @return the newly created {@link TmaBillingAccountData}
	 */
	TmaBillingAccountData createBillingAccount(final CreateTmaBillingAccountRequest billingAccountRequest);

	/**
	 * Deletes the {@link TmaBillingAccountModel} associated to a given billing account id and a billing system id.
	 *
	 * @param billingSystemId
	 *           the billing system identifier
	 * @param billingAccountId
	 *           the billing account identifier from the billing system
	 */
	void deleteBillingAccount(final String billingSystemId, final String billingAccountId);


	/**
	 * Creates a new {@link TmaBillingAccountData} .
	 *
	 *
	 * @return the newly created {@link TmaBillingAccountData}
	 */
	TmaBillingAccountData generateBillingAccount();

}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


/**
 * Data Access Object for operations related to the {@link TmaBillingAccountModel} type.
 *
 * @since 6.6
 */
public interface TmaBillingAccountDao
{
	/**
	 * Finds the {@link TmaBillingAccountModel} for a given billing account id and billing system id.
	 *
	 * @param billingSystemId  the billing system identifier
	 * @param billingAccountId the billing account identifier
	 * @return the {@link TmaBillingAccountModel} for the given details if found.
	 * @throws ModelNotFoundException if no billing account is found
	 */
	TmaBillingAccountModel findBillingAccount(final String billingSystemId, final String billingAccountId);
}

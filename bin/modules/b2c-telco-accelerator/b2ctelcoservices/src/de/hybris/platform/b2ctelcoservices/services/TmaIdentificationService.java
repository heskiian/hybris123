/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;


/**
 * Service for operations related to the {@link TmaIdentificationModel}.
 *
 * @since 1911
 */
public interface TmaIdentificationService
{
	/**
	 * Searches for the {@link TmaIdentificationModel} with the given identificationType and identificationNumber.
	 *
	 * @param identificationType
	 *           the identificationType of the {@link TmaIdentificationModel}
	 * @param identificationNumber
	 *           the identificationNumber of the {@link TmaIdentificationModel}
	 *
	 * @return {@link TmaIdentificationModel}
	 */
	TmaIdentificationModel findIdentificationByTypeAndNumber(final String identificationType, final String identificationNumber);
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;


/**
 * Data Access object for operations related to the {@link TmaIdentificationModel}.
 *
 * @since 1911
 */
public interface TmaIdentificationDao
{
	/**
	 * Returns {@link TmaIdentificationModel} based on the identificationType and identificationNumber.
	 *
	 * @param identificationType
	 *           the identificationType of the {@link TmaIdentificationModel}
	 * @param identificationNumber
	 *           the identificationNumber of the {@link TmaIdentificationModel}
	 *
	 * @return {@link TmaIdentificationModel}
	 */
	TmaIdentificationModel getTmaIdentificationModelForTypeAndNumber(final String identificationType,
			final String identificationNumber);
}

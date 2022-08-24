/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.core.model.security.PrincipalModel;


/**
 * Service for operations related to the {@link PrincipalModel}.
 *
 * @since 6.6
 */
public interface PrincipalService
{
	/**
	 * Searches for the {@link PrincipalModel} with the given uid.
	 *
	 * @param uid
	 * 		unique identifier
	 * @return the {@link PrincipalModel} with the uid given or null if it cannot be found
	 */
	PrincipalModel findPrincipalByUid(final String uid);
}

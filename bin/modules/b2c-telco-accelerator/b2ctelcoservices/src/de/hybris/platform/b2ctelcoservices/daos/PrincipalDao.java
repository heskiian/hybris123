/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.security.PrincipalModel;


/**
 * Data Access Object for the {@link PrincipalModel} object.
 *
 * @since 6.6
 */
public interface PrincipalDao
{
	/**
	 * Searches for the principal having the provided identifier.
	 *
	 * @param uid
	 * 		the unique identifier of the {@link PrincipalModel}
	 * @return the {@link PrincipalModel} for the given identifier
	 */
	PrincipalModel findPrincipalByUid(final String uid);
}

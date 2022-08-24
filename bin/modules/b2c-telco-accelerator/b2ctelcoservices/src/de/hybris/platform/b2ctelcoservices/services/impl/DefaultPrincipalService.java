/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.PrincipalDao;
import de.hybris.platform.b2ctelcoservices.services.PrincipalService;
import de.hybris.platform.core.model.security.PrincipalModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link PrincipalService}.
 *
 * @since 6.6
 */
public class DefaultPrincipalService implements PrincipalService
{
	private PrincipalDao principalDao;

	@Override
	public PrincipalModel findPrincipalByUid(final String uid)
	{
		return getPrincipalDao().findPrincipalByUid(uid);
	}

	@Required
	public void setPrincipalDao(final PrincipalDao principalDao)
	{
		this.principalDao = principalDao;
	}

	protected PrincipalDao getPrincipalDao()
	{
		return principalDao;
	}
}

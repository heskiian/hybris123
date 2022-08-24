/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.PrincipalDao;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collection;
import java.util.Collections;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of the {@link PrincipalDao}.
 *
 * @since 6.6
 */
public class DefaultPrincipalDao extends DefaultGenericDao<PrincipalModel> implements PrincipalDao
{
	public DefaultPrincipalDao()
	{
		super(PrincipalModel._TYPECODE);
	}

	@Override
	public PrincipalModel findPrincipalByUid(final String uid)
	{
		validateParameterNotNullStandardMessage(PrincipalModel.UID, uid);
		final Collection<PrincipalModel> principals = find(Collections.singletonMap(PrincipalModel.UID, uid));
		if (principals.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + principals.size() + " principals with the unique uid '" + uid + "'");
		}
		if (principals.isEmpty())
		{
			throw new ModelNotFoundException("Error on retrieving the " + PrincipalModel._TYPECODE + " object for the "
					+ PrincipalModel.UID + " parameter: " + uid);
		}
		return principals.iterator().next();
	}
}

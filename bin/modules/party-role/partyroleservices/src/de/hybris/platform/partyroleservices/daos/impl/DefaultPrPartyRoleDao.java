/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroleservices.daos.impl;

import de.hybris.platform.partyroleservices.daos.PrPartyRoleDao;
import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link PrPartyRoleDao}
 *
 * @since 2108
 */
public class DefaultPrPartyRoleDao extends DefaultGenericDao<PrPartyRoleModel> implements PrPartyRoleDao
{
	public DefaultPrPartyRoleDao()
	{
		super(PrPartyRoleModel._TYPECODE);
	}

	@Override
	public PrPartyRoleModel findPartyRole(final Map<String, ? extends Object> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<PrPartyRoleModel> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + PrPartyRoleModel._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}

}

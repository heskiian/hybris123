/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroleservices.service.impl;

import de.hybris.platform.partyroleservices.daos.PrPartyRoleDao;
import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroleservices.service.PrPartyRoleService;

import java.util.HashMap;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link PrPartyRoleService}.
 *
 * @since 2108
 */
public class DefaultPrPartyRoleService implements PrPartyRoleService
{
	private PrPartyRoleDao partyRoleDao;

	public DefaultPrPartyRoleService(final PrPartyRoleDao prPartyRoleDao)
	{
		this.partyRoleDao = prPartyRoleDao;
	}

	@Override
	public PrPartyRoleModel getPartyRole(final String id)
	{
		validateParameterNotNull(id, "Parameter id can not be null");

		final Map parameters = new HashMap();
		parameters.put(PrPartyRoleModel.ID, id);
		return getPartyRoleDao().findPartyRole(parameters);
	}

	protected PrPartyRoleDao getPartyRoleDao()
	{
		return partyRoleDao;
	}
}

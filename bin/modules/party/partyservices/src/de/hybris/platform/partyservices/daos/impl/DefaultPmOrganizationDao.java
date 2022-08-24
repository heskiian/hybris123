/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.daos.impl;

import de.hybris.platform.partyservices.daos.PmOrganizationDao;
import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link PmOrganizationDao}.
 *
 * @since 2108
 */
public class DefaultPmOrganizationDao extends DefaultGenericDao<PmOrganizationModel> implements PmOrganizationDao
{
	public DefaultPmOrganizationDao()
	{
		super(PmOrganizationModel._TYPECODE);
	}

	@Override
	public List<PmOrganizationModel> findOrganization(String id)
	{
		validateParameterNotNull(id, "Organization id must not be null!");

		return find(Collections.singletonMap(PmOrganizationModel.ID, (Object) id));
	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.partyservices.daos.PmIndividualDao;
import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.Collections;
import java.util.List;


/**
 * Default Implementation of {@link PmIndividualDao}
 *
 * @since 2108
 */
public class DefaultPmIndividualDao extends DefaultGenericDao<PmIndividualModel> implements PmIndividualDao
{
	public DefaultPmIndividualDao()
	{
		super(PmIndividualModel._TYPECODE);
	}

	@Override
	public List<PmIndividualModel> findIndividualParty(final String id)
	{
		validateParameterNotNull(id, "Individual Party id must not be null!");

		return find(Collections.singletonMap(PmIndividualModel.ID, (Object) id));

	}
}
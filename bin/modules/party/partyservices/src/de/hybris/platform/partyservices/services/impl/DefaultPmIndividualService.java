/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static java.lang.String.format;

import de.hybris.platform.partyservices.daos.PmIndividualDao;
import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.partyservices.services.PmIndividualService;

import java.util.List;


/**
 * Default implementation of {@link PmIndividualService}
 *
 * @since 2108
 */
public class DefaultPmIndividualService implements PmIndividualService
{
	private final PmIndividualDao pmIndividualDao;

	public DefaultPmIndividualService(final PmIndividualDao pmIndividualDao)
	{
		this.pmIndividualDao = pmIndividualDao;
	}

	@Override
	public PmIndividualModel findIndividualParty(final String id)
	{
		final List<PmIndividualModel> individualParties = pmIndividualDao.findIndividualParty(id);

		validateIfSingleResult(individualParties, format("Individual Party with id '%s' not found!", id),
				format("Individual Party id  '%s' is not unique, %d Individual Parties found!", id, individualParties.size()));
		return individualParties.get(0);
	}
}

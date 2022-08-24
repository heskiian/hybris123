/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services.impl;

import de.hybris.platform.partyservices.daos.PmOrganizationDao;
import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partyservices.services.PmOrganizationService;

import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;


/**
 * Default implementation of the {@link PmOrganizationService}.
 *
 * @since 2108
 */
public class DefaultPmOrganizationService implements PmOrganizationService
{
	private PmOrganizationDao organizationDao;

	public DefaultPmOrganizationService(final PmOrganizationDao organizationDao)
	{
		this.organizationDao = organizationDao;
	}

	@Override
	public PmOrganizationModel getOrganization(final String id)
	{
		final List<PmOrganizationModel> organizations = organizationDao.findOrganization(id);

		validateIfSingleResult(organizations, format("Organization with id '%s' not found!", id),
				format("Organization id '%s' is not unique, %d organizations found!", id, organizations.size()));

		return organizations.get(0);
	}
}

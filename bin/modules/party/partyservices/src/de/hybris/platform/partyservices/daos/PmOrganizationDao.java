/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.daos;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * Data access object for {@link PmOrganizationModel}s.
 *
 * @since 2108
 */
public interface PmOrganizationDao extends GenericDao<PmOrganizationModel>
{
	/**
	 * Returns the organization for the provided id.
	 *
	 * @param id
	 * 		The unique identifier of the organization
	 * @return List of {@link PmOrganizationModel} for the provided id
	 */
	List<PmOrganizationModel> findOrganization(final String id);
}

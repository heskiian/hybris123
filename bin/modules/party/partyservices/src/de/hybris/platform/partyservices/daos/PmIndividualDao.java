/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.daos;

import de.hybris.platform.partyservices.model.PmIndividualModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * Data access object for {@link PmIndividualModel}
 *
 * @since 2108
 */
public interface PmIndividualDao extends GenericDao<PmIndividualModel>
{
	/**
	 * Returns the individual party for the id.
	 *
	 * @param id
	 *           The unique identifier of the individual party
	 * @return List of {@link PmIndividualModel} for the provided id
	 */
	List<PmIndividualModel> findIndividualParty(final String id);
}

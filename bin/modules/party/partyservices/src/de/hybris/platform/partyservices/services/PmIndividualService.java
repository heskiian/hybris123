/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services;

import de.hybris.platform.partyservices.model.PmIndividualModel;


/**
 * Service responsible for handling {@link PmIndividualModel} related operations.
 *
 * @since 2108
 */
public interface PmIndividualService
{
	/**
	 * Returns the individual party for the id.
	 *
	 * @param id
	 *           The unique identifier of the individual party
	 *
	 * @return {@link PmIndividualModel} for the provided id
	 */
	PmIndividualModel findIndividualParty(final String id);
}

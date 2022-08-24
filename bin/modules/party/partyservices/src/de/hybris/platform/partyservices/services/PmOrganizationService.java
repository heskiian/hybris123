/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.services;

import de.hybris.platform.partyservices.model.PmOrganizationModel;


/**
 * Service responsible for handling {@link PmOrganizationModel} related operations.
 *
 * @since 2108
 */
public interface PmOrganizationService
{
	/**
	 * Returns the organization for the provided id.
	 *
	 * @param id
	 * 		The unique identifier of the organization
	 * @return The {@link PmOrganizationModel} for the provided id
	 */
	PmOrganizationModel getOrganization(final String id);
}

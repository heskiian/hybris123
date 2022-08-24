/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroleservices.service;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


/**
 * Service responsible for handling {@link PrPartyRoleModel}  related operations.
 *
 * @since 2108
 */
public interface PrPartyRoleService
{
	/**
	 * Returns a {@link PrPartyRoleModel} for the given id.
	 *
	 * @param id
	 * 		identifier of {@link PrPartyRoleModel}
	 * @return the {@link PrPartyRoleModel} found.
	 * @throws ModelNotFoundException
	 * 		if no subscribed product is found.
	 */
	PrPartyRoleModel getPartyRole(final String id);
}

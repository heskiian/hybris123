/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcoservices.customer;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.Set;


/**
 * Service for customer account related operations for B2B entities.
 *
 * @since 2105
 */
public interface TmaB2bCustomerAccountService extends CustomerAccountService
{
	/**
	 * Returns the users part of the provided unit and its subunits.
	 *
	 * @param unit
	 * 		The business unit
	 * @return List of users part of the unit
	 */
	Set<PrincipalModel> getUsersFrom(final B2BUnitModel unit);

	/**
	 * Returns the units and all its subunits the provided user is part of.
	 *
	 * @param user
	 * 		The user
	 * @return The units and its subunits for the provided principal
	 */
	Set<PrincipalModel> getUnits(final PrincipalModel user);

	/**
	 * Returns the lists of users part of the organizations where the given principal is part of.
	 *
	 * @param principal
	 * 		the principal
	 * @return the list of users part of the given principal's organizations
	 */
	Set<PrincipalModel> getUsersInSameOrganizationWith(final PrincipalModel principal);

	/**
	 * Checks if the provided principal is a member of the organization.
	 *
	 * @param principalId
	 * 		The identifier of the principal
	 * @param usersInOrganization
	 * 		The users in the organization
	 * @param unitsInOrganization
	 * 		The units in the organization
	 * @return True if the principal is member of the organization, otherwise false
	 */
	boolean isPrincipalMemberOfOrganizations(final String principalId, final Set<PrincipalModel> usersInOrganization,
			final Set<PrincipalModel> unitsInOrganization);

}

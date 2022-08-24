/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcoservices.customer.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2btelcoservices.customer.TmaB2bCustomerAccountService;
import de.hybris.platform.b2ctelcoservices.customer.DefaultTmaCustomerAccountService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link TmaB2bCustomerAccountService}.
 *
 * @since 2105
 */
public class DefaultTmaB2bCustomerAccountService extends DefaultTmaCustomerAccountService implements TmaB2bCustomerAccountService
{
	@Override
	public Set<PrincipalModel> getUsersFrom(final B2BUnitModel unit)
	{
		final Set<PrincipalGroupModel> units = getAllSubUnits(unit);

		final Set<PrincipalModel> users = new HashSet<>();

		if (CollectionUtils.isNotEmpty(unit.getMembers()))
		{
			users.addAll(unit.getMembers().stream().filter((PrincipalModel principalModel) -> principalModel instanceof UserModel)
					.map((PrincipalModel principalModel) -> ((UserModel) principalModel)).collect(Collectors.toSet()));
		}

		if (CollectionUtils.isNotEmpty(units))
		{
			units.forEach((PrincipalGroupModel principalGroup) -> users.addAll(
					principalGroup.getMembers().stream().filter((PrincipalModel principalModel) -> principalModel instanceof UserModel)
							.collect(Collectors.toSet())));
		}

		return users;
	}

	@Override
	public Set<PrincipalModel> getUnits(final PrincipalModel user)
	{
		if (CollectionUtils.isEmpty(user.getGroups()))
		{
			return Collections.emptySet();
		}

		final Set<PrincipalGroupModel> principalGroups = user.getGroups().stream()
				.filter((PrincipalGroupModel principalGroup) -> principalGroup instanceof B2BUnitModel)
				.collect(Collectors.toSet());

		final Set<PrincipalModel> unitsInOrganization = new HashSet<>(principalGroups);

		principalGroups.forEach(principalGroup -> {
			final Set<PrincipalGroupModel> subUnits = getAllSubUnits(principalGroup);
			unitsInOrganization.addAll(subUnits);
		});

		return unitsInOrganization;
	}

	@Override
	public Set<PrincipalModel> getUsersInSameOrganizationWith(final PrincipalModel principal)
	{
		final Set<PrincipalGroupModel> principalGroups = principal.getGroups().stream()
				.filter((PrincipalGroupModel principalGroup) -> principalGroup instanceof CompanyModel).collect(Collectors.toSet());

		final Set<PrincipalModel> users = new HashSet<>();

		principalGroups.forEach((PrincipalGroupModel principalGroup) -> {
			if (principalGroup instanceof B2BUnitModel)
			{
				users.addAll(getUsersFrom((B2BUnitModel) principalGroup));
			}
		});

		return users;
	}

	@Override
	public boolean isPrincipalMemberOfOrganizations(final String principalId, final Set<PrincipalModel> usersInOrganization,
			final Set<PrincipalModel> unitsInOrganization)
	{
		for (PrincipalModel organizationUser : usersInOrganization)
		{
			if (organizationUser.getUid().equalsIgnoreCase(principalId))
			{
				return true;
			}
		}


		for (PrincipalModel organizationUnit : unitsInOrganization)
		{
			if (organizationUnit.getUid().equalsIgnoreCase(principalId))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns all the sub-units for the provided unit.
	 *
	 * @param unit
	 * 		The unit
	 * @return The sub-units for the provided unit
	 */
	protected Set<PrincipalGroupModel> getAllSubUnits(final PrincipalGroupModel unit)
	{
		final Set<PrincipalGroupModel> subUnits = new HashSet<>();

		if (CollectionUtils.isNotEmpty(unit.getMembers()))
		{
			subUnits.addAll(unit.getMembers().stream().filter((PrincipalModel principal) -> principal instanceof B2BUnitModel)
					.map((PrincipalModel principalModel) -> (PrincipalGroupModel) principalModel).collect(Collectors.toSet()));
		}

		if (CollectionUtils.isEmpty(subUnits))
		{
			return subUnits;
		}

		subUnits.forEach((PrincipalModel principal) -> {
			if (principal instanceof B2BUnitModel)
			{
				subUnits.addAll(getAllSubUnits((B2BUnitModel) principal));
			}
		});

		return subUnits;
	}
}

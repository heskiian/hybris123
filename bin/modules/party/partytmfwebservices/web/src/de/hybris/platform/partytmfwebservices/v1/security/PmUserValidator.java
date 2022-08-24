/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.security;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.partyservices.services.PmIndividualService;
import de.hybris.platform.partyservices.services.PmOrganizationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.ObjectUtils;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2108
 */
public class PmUserValidator
{
	private final UserService userService;
	private final PmIndividualService individualService;
	private final PmOrganizationService organizationService;

	public PmUserValidator(final UserService userService, final PmIndividualService individualService,
			final PmOrganizationService organizationService)
	{
		this.userService = userService;
		this.individualService = individualService;
		this.organizationService = organizationService;
	}

	/**
	 * Checks if the authorized client is allowed to access the requested individual.
	 *
	 * @param authentication
	 * 		The authenticated client
	 * @param individualId
	 * 		The unique identifier of the individual
	 * @return True if the authorized client is able to access the resource, otherwise false
	 */
	public boolean isOwnerOfIndividual(final Authentication authentication, final String individualId)
	{
		if (StringUtils.isBlank(individualId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}

		final boolean isAnonymous = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId()
				.equals(authentication.getPrincipal());

		if (isAnonymous)
		{
			return Boolean.FALSE;
		}

		final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
		final Set<PrincipalModel> principals = getIndividualService().findIndividualParty(individualId).getPrincipals();

		if (CollectionUtils.isNotEmpty(principals))
		{
			return principals.contains(user);
		}

		return Boolean.FALSE;
	}

	/**
	 * Checks if the authorized client is allowed to access the requested organization.
	 *
	 * @param authentication
	 * 		The authenticated client
	 * @param organizationId
	 * 		The unique identifier of the organization
	 * @return True if the authorized client is able to access the resource, otherwise false
	 */
	public boolean isOwnerOfOrganization(final Authentication authentication, final String organizationId)
	{
		if (StringUtils.isBlank(organizationId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
		{
			return Boolean.FALSE;
		}

		final boolean isAnonymous = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId()
				.equals(authentication.getPrincipal());

		if (isAnonymous)
		{
			return Boolean.FALSE;
		}

		final UserModel user = getUserService().getUserForUID(authentication.getPrincipal().toString());
		final Set<PrincipalModel> principals = getOrganizationService().getOrganization(organizationId).getPrincipals();

		if (CollectionUtils.isNotEmpty(principals))
		{
			return principals.contains(user);
		}

		return Boolean.FALSE;
	}


	protected UserService getUserService()
	{
		return userService;
	}


	protected PmIndividualService getIndividualService()
	{
		return individualService;
	}

	protected PmOrganizationService getOrganizationService()
	{
		return organizationService;
	}
}

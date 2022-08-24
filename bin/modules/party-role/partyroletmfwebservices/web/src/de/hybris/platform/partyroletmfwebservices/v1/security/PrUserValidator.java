/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.security;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.partyroleservices.service.PrPartyRoleService;
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
public class PrUserValidator
{
	private UserService userService;
	private PrPartyRoleService partyRoleService;

	public PrUserValidator(final UserService userService, final PrPartyRoleService partyRoleService)
	{
		this.userService = userService;
		this.partyRoleService = partyRoleService;
	}

	/**
	 * Checks if the authorized client is allowed to access the requested party role.
	 *
	 * @param authentication
	 * 		The authenticated client
	 * @param partyRoleId
	 * 		The unique identifier of the party role
	 * @return True if the authorized client is able to access the resource, otherwise false
	 */
	public boolean isOwnerOfPartyRole(final Authentication authentication, final String partyRoleId)
	{
		if (StringUtils.isBlank(partyRoleId) || ObjectUtils.isEmpty(authentication.getPrincipal()))
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
		final Set<PrincipalModel> principals = getPartyRoleService().getPartyRole(partyRoleId).getParty().getPrincipals();

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

	protected PrPartyRoleService getPartyRoleService()
	{
		return partyRoleService;
	}
}

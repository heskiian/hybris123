/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptiontmfwebservices.v1.security;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 2108
 */
public class UcUserValidator
{
	private static final String ANONYMOUS = "anonymous";
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	private UserService userService;

	public UcUserValidator(UserService userService)
	{
		this.userService = userService;
	}

	public boolean isClientNotAnonymous(final Authentication authentication, final String relatedPartyId)
	{
		if (((OAuth2Authentication) authentication).isClientOnly() || ANONYMOUS.equals(authentication.getPrincipal().toString()))
		{
			return Boolean.FALSE;
		}

		if (SecurityContextHolder.getContext().getAuthentication() != null)
		{
			if (StringUtils.isNotEmpty(relatedPartyId))
			{
				final UserModel user = getUserService()
						.getUserForUID(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

				if (relatedPartyId.equals(user.getUcParty().getId()))
				{
					return Boolean.TRUE;
				}
			}
			else
			{
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	protected UserService getUserService()
	{
		return userService;
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;


import de.hybris.platform.b2ctelcocommercewebservicescommons.exception.TmaInvalidUserException;
import de.hybris.platform.b2ctelcofacades.user.TmaUserFacade;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates if the authenticated user is authorized to access a resource
 *
 * @since 1907
 */
public class TmaUserAccessValidator implements Validator
{
	private static final Logger LOG = Logger.getLogger(TmaUserAccessValidator.class);
	private static final String INSUFFICEIENT_ACCESS_USER = "tmaGetProduct.invalidUser";
	private static final String DEFAULT_ERROR_MESSAGE = "[{0}] Loggedin User has insufficient access";
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private TmaUserFacade userFacade;

	public TmaUserAccessValidator(final TmaUserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.equals(arg0);
	}

	/**
	 * Validates if the given User is valid to see customer details
	 *
	 * @param target
	 *           the User ID
	 * @param errors
	 *           the list of errors
	 */
	@Override
	public void validate(final Object target, final Errors errors)
	{
		final String userId = (String) target;
		final Authentication authentication = getAuth();
		final boolean isLoggedinUserAnonymous = userFacade.isAnonymousUser();
		try
		{
			if (StringUtils.isNotEmpty(userId))
			{
				if (!isLoggedinUserAnonymous && !isResourceOwner(authentication, userId))
				{
					errors.reject(INSUFFICEIENT_ACCESS_USER, new String[]
					{ userId }, DEFAULT_ERROR_MESSAGE);
				}
				if (isLoggedinUserAnonymous && !hasRole(ROLE_TRUSTED_CLIENT, authentication))
				{
					errors.reject(INSUFFICEIENT_ACCESS_USER, new String[]
					{ userId }, DEFAULT_ERROR_MESSAGE);
				}
			}
		}
		catch (final TmaInvalidUserException ex)
		{
			LOG.info(ex);
			LOG.error(ex.getMessage());
			errors.reject(INSUFFICEIENT_ACCESS_USER, new String[]
			{ userId }, DEFAULT_ERROR_MESSAGE);
		}
	}

	protected Authentication getAuth()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}


	/**
	 * Checks if the user isResourceOwner.
	 *
	 * @param userId
	 *           The userId provided.
	 * @return False if user is not logged in user, otherwise true.
	 */
	public boolean isResourceOwner(Authentication authentication, final String userId)
	{
		if (authentication == null)
		{
			authentication = getAuth();
		}
		if (userId == null)
		{
			throw new TmaInvalidUserException();
		}
		if (authentication.getPrincipal() == null)
		{
			return Boolean.FALSE;
		}
		return authentication.getPrincipal().equals(userId);
	}

	/**
	 * Checks if the user has given role.
	 *
	 * @param role
	 *           The role is provided as string
	 * @return False if logged in user doesn't have given role, otherwise true.
	 */
	protected boolean hasRole(final String role, Authentication authentication)
	{
		if (authentication == null)
		{
			authentication = getAuth();
		}
		for (final GrantedAuthority ga : authentication.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	protected TmaUserFacade getUserFacade()
	{
		return userFacade;
	}

	public void setUserFacade(final TmaUserFacade userFacade)
	{
		this.userFacade = userFacade;
	}
}

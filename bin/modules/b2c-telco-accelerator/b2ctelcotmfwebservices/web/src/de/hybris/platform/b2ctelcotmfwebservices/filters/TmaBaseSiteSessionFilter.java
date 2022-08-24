/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.filters;


import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaInvalidResourceException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter that creates a new session for the request and sets current baseSite. if request
 * url contains baseSite id, that value is used to set session baseSite
 * otherwise the default value is picked from project.properties.
 *
 * @since 1810
 */
public class TmaBaseSiteSessionFilter extends OncePerRequestFilter
{
	private static final Logger LOG = Logger.getLogger(TmaBaseSiteSessionFilter.class);
	private static final String REGEX = ",";
	private static final String APPLY_ALLOWED_PRINCIPAL_FLAG = "applyAllowedPrincipalFlag";

	private BaseSiteService baseSiteService;
	private final SessionService sessionService;
	private final ConfigurationService configurationService;
	private final UserFacade userFacade;
	private final TmaCustomerFacade tmaCustomerFacade;

	public TmaBaseSiteSessionFilter(final ConfigurationService configurationService, final UserFacade userFacade,
			final TmaCustomerFacade tmaCustomerFacade, final SessionService sessionService)
	{
		this.configurationService = configurationService;
		this.userFacade = userFacade;
		this.tmaCustomerFacade = tmaCustomerFacade;
		this.sessionService = sessionService;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		final String baseSiteId = request.getParameter(B2ctelcotmfwebservicesConstants.HTTP_REQUEST_PARAM_BASESITE);
		String relatedPartyId = !StringUtils.isEmpty(request.getParameter(B2ctelcotmfwebservicesConstants.RELATED_PARTY_ID))
				? request.getParameter(B2ctelcotmfwebservicesConstants.RELATED_PARTY_ID)
				: request.getParameter(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_RELATED_PARTY_ID);
		if (StringUtils.isEmpty(relatedPartyId))
		{
			relatedPartyId = request.getParameter(B2ctelcotmfwebservicesConstants.PRICE_CONTEXT_RELATED_PARTY_ID);
		}
		if (StringUtils.isNotBlank(baseSiteId))
		{
			final BaseSiteModel requestedBaseSite = getBaseSiteService().getBaseSiteForUID(baseSiteId);

			if (requestedBaseSite != null)
			{
				final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

				if (!requestedBaseSite.equals(currentBaseSite))
				{
					getBaseSiteService().setCurrentBaseSite(requestedBaseSite, true);
				}
			}
			else
			{
				final TmaInvalidResourceException ex = new TmaInvalidResourceException(YSanitizer.sanitize(baseSiteId));
				LOG.debug(ex.getMessage());
				throw ex;
			}
		}
		else
		{
			getBaseSiteService().setCurrentBaseSite(Config.getParameter(B2ctelcotmfwebservicesConstants.SESSION_DEFAULT_BASESITE),
					true);
		}

		if (StringUtils.isNotEmpty(relatedPartyId) || hasRole() || StringUtils
				.isNotEmpty(getTmaCustomerFacade().getCurrentCustomerUid()))
		{
			final String applicableUid = getApplicableUid(relatedPartyId);
			if (StringUtils.isNotEmpty(applicableUid))
			{
				getTmaCustomerFacade().updateEligibilityContextsByCustomer(applicableUid);
				getUserFacade().setCurrentUser(applicableUid);
			}
		}

		getSessionService().setAttribute(APPLY_ALLOWED_PRINCIPAL_FLAG, !hasRole());
		filterChain.doFilter(request, response);
	}

	protected String getApplicableUid(final String userId)
	{

		if (StringUtils.isEmpty(userId))
		{
			final String currentUid = getTmaCustomerFacade().getCurrentCustomerUid();
			if (!hasRole() || !getUserFacade().isAnonymousUser())
			{
				return currentUid;
			}
		}
		return userId;
	}

	protected boolean hasRole()
	{
		final List<String> eligibilityAdminRoles = Arrays.asList(
				getConfigurationService().getConfiguration().getString(B2ctelcotmfwebservicesConstants.ELIGIBILTY_ROLES)
						.split(REGEX));

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null)
		{
			for (final GrantedAuthority ga : authentication.getAuthorities())
			{
				if (eligibilityAdminRoles.contains(ga.getAuthority()))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	public TmaCustomerFacade getTmaCustomerFacade()
	{
		return tmaCustomerFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}

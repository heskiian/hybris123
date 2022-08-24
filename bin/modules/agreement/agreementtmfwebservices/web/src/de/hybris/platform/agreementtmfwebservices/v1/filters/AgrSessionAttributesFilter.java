/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.filters;

import de.hybris.platform.agreementtmfwebservices.v1.context.AgrContextInformationLoader;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter used for setting the session context based on request parameters:<br>
 * <ul>
 * <li><b>lang</b> - set current {@link LanguageModel}</li>
 * </ul>
 *
 * @since 2111
 */
public class AgrSessionAttributesFilter extends OncePerRequestFilter
{
	private AgrContextInformationLoader contextInformationLoader;

	public AgrSessionAttributesFilter(final AgrContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		getContextInformationLoader().setLanguageFromRequest(request);
		filterChain.doFilter(request, response);

	}

	protected AgrContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

}

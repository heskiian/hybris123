/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.filters;


import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.context.BaContextInformationLoader;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * Filter used for setting the session context based on request parameters:<br>
 * <ul>
 * <li><b>lang</b> - set current {@link LanguageModel}</li>
 * </ul>
 *
 * @since 2111
 */
public class BaSessionAttributesFilter extends OncePerRequestFilter
{
	private BaContextInformationLoader contextInformationLoader;

	public BaSessionAttributesFilter(final BaContextInformationLoader contextInformationLoader)
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

	protected BaContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

}

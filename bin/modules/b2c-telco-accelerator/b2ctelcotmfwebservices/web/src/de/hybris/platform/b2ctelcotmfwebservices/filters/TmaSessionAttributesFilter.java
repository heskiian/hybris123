/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.filters;

import de.hybris.platform.b2ctelcotmfwebservices.context.TmaContextInformationLoader;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter sets session context basing on request parameters:<br>
 * <ul>
 * <li><b>lang</b> - set current {@link LanguageModel}</li>
 * <li><b>curr</b> - set current {@link CurrencyModel}</li>
 * </ul>
 *
 * @since 1810
 *
 */
public class TmaSessionAttributesFilter extends OncePerRequestFilter
{
	private TmaContextInformationLoader contextInformationLoader;
	private BaseStoreService baseStoreService;
	private SessionService sessionService;


	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		setUserTaxGroupAttribute();
		getContextInformationLoader().setLanguageFromRequest(request);
		getContextInformationLoader().setCurrencyFromRequest(request);

		filterChain.doFilter(request, response);
	}

	protected void setUserTaxGroupAttribute()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		if (currentBaseStore != null)
		{
			final UserTaxGroup taxGroup = currentBaseStore.getTaxGroup();
			if (taxGroup != null)
			{
				getSessionService().setAttribute(Europe1Constants.PARAMS.UTG, taxGroup);
			}
		}
	}

	protected TmaContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

	@Required
	public void setContextInformationLoader(final TmaContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

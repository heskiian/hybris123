/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.context.impl;

import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.context.AgrContextInformationLoader;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Default context information loader implementation.
 *
 * @since 2111
 */
public class AgrDefaultContextInformationLoader implements AgrContextInformationLoader
{
	private static final Logger LOG = Logger.getLogger(AgrDefaultContextInformationLoader.class);

	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;


	public AgrDefaultContextInformationLoader(final CommonI18NService commonI18NService,
			final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commonI18NService = commonI18NService;
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	@Override
	public LanguageModel setLanguageFromRequest(final HttpServletRequest request)
	{
		LanguageModel languageToSet = getCommerceCommonI18NService().getDefaultLanguage();

		final String acceptLanguageValue = request.getHeader(AgreementtmfwebservicesConstants.HTTP_REQUEST_HEADER_LANGUAGE);
		if (StringUtils.isNotBlank(acceptLanguageValue))
		{
			final Locale requestLocale = request.getLocale();
			if (requestLocale != null)
			{
				final String languageString = requestLocale.getLanguage();
				try
				{
					languageToSet = getCommonI18NService().getLanguage(languageString);
				}
				catch (final UnknownIdentifierException e)
				{
					LOG.warn("Language " + languageString + " is not supported", e);
				}
			}
		}

		if (languageToSet != null && !languageToSet.equals(getCommerceCommonI18NService().getCurrentLanguage()))
		{
			getCommerceCommonI18NService().setCurrentLanguage(languageToSet);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(languageToSet + " set as current language");
			}
		}
		return languageToSet;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

}

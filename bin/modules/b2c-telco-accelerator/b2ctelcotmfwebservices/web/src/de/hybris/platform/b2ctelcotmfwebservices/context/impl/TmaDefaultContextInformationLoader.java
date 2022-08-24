/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.context.impl;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.context.TmaContextInformationLoader;
import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaUnsupportedCurrencyException;
import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaUnsupportedLanguageException;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default context information loader
 *
 * @since 1810
 */
public class TmaDefaultContextInformationLoader implements TmaContextInformationLoader
{
	private static final Logger LOG = Logger.getLogger(TmaDefaultContextInformationLoader.class);

	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private BaseStoreService baseStoreService;

	@Override
	public LanguageModel setLanguageFromRequest(final HttpServletRequest request) throws TmaUnsupportedLanguageException
	{
		String languageString = null;

		final String acceptLanguageValue = request.getHeader(B2ctelcotmfwebservicesConstants.HTTP_REQUEST_HEADER_LANGUAGE);
		if (StringUtils.isNotBlank(acceptLanguageValue))
		{
			final Locale requestLocale = request.getLocale();
			if (requestLocale != null)
			{
				languageString = requestLocale.getLanguage();
			}
		}

		LanguageModel languageToSet = null;

		if (!StringUtils.isBlank(languageString))
		{
			try
			{
				languageToSet = getCommonI18NService().getLanguage(languageString);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Language " + languageString + " is not supported", e);
				languageToSet = getCommerceCommonI18NService().getDefaultLanguage();
			}
		}

		if (languageToSet == null)
		{
			languageToSet = getCommerceCommonI18NService().getDefaultLanguage();
		}

		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore != null)
		{
			final Collection<LanguageModel> storeLanguages = getStoresLanguages(currentBaseStore);

			if (CollectionUtils.isEmpty(storeLanguages))
			{
				LOG.warn("Base store " + currentBaseStore + " does not support any language " + languageToSet);
				return getCommerceCommonI18NService().getDefaultLanguage();
			}

			if (!storeLanguages.contains(languageToSet))
			{
				LOG.warn("Base store " + currentBaseStore + " does not support language " + languageToSet);
				languageToSet = currentBaseStore.getDefaultLanguage();
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


	protected Collection<LanguageModel> getStoresLanguages(final BaseStoreModel currentBaseStore)
	{
		if (currentBaseStore == null)
		{
			throw new IllegalStateException("No current base store was set!");
		}
		return currentBaseStore.getLanguages() == null ? Collections.<LanguageModel> emptySet() : currentBaseStore.getLanguages();
	}

	@Override
	public CurrencyModel setCurrencyFromRequest(final HttpServletRequest request) throws TmaUnsupportedCurrencyException
	{
		final String currencyString = request.getParameter(B2ctelcotmfwebservicesConstants.HTTP_REQUEST_PARAM_CURRENCY);
		CurrencyModel currencyToSet = null;

		if (!StringUtils.isBlank(currencyString))
		{
			try
			{
				currencyToSet = getCommonI18NService().getCurrency(currencyString);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new TmaUnsupportedCurrencyException("Currency " + YSanitizer.sanitize(currencyString) + " is not supported", e);
			}
		}

		if (currencyToSet == null)
		{
			currencyToSet = getCommerceCommonI18NService().getDefaultCurrency();
		}

		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore != null)
		{
			final List<CurrencyModel> storeCurrencies = getCommerceCommonI18NService().getAllCurrencies();

			if (CollectionUtils.isEmpty(storeCurrencies))
			{
				throw new TmaUnsupportedCurrencyException("Current base store does not support any currency!");
			}

			if (!storeCurrencies.contains(currencyToSet))
			{
				throw new TmaUnsupportedCurrencyException(currencyToSet);
			}
		}

		if (currencyToSet != null && !currencyToSet.equals(getCommerceCommonI18NService().getCurrentCurrency()))
		{
			getCommerceCommonI18NService().setCurrentCurrency(currencyToSet);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(currencyToSet + " set as current currency");
			}
		}

		return currencyToSet;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

}

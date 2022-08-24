/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.context;

import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaUnsupportedCurrencyException;
import de.hybris.platform.b2ctelcotmfwebservices.exception.TmaUnsupportedLanguageException;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for context information loader
 * 
 * @since 1810
 */
public interface TmaContextInformationLoader
{
	/**
	 * Method set current language base on information from request
	 *
	 * @param request
	 *           - request from which we should get language information
	 * @return language set as current
	 * @throws TmaUnsupportedLanguageException
	 */
	LanguageModel setLanguageFromRequest(final HttpServletRequest request) throws TmaUnsupportedLanguageException;

	/**
	 * Method set current currency based on information from request
	 *
	 * @param request
	 *           - request from which we should get currency information
	 * @return currency set as current
	 * @throws TmaUnsupportedCurrencyException
	 */
	CurrencyModel setCurrencyFromRequest(final HttpServletRequest request) throws TmaUnsupportedCurrencyException;

}

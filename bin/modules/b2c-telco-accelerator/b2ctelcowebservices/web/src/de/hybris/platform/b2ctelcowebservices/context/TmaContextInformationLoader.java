/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.context;

import de.hybris.platform.b2ctelcowebservices.exceptions.TmaInvalidResourceException;
import de.hybris.platform.b2ctelcowebservices.exceptions.TmaRecalculationException;
import de.hybris.platform.b2ctelcowebservices.exceptions.TmaUnsupportedCurrencyException;
import de.hybris.platform.b2ctelcowebservices.exceptions.TmaUnsupportedLanguageException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
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
	 * Method resolves base site uid from URL and set it as current site i.e<br>
	 * <i>/rest/v1/mysite/cart</i>, or <br>
	 * <i>/rest/v1/mysite/customers/current</i><br>
	 * would try to set base site with uid=mysite as a current site.<br>
	 *
	 * @param request
	 *           - request from which we should get base site uid
	 *
	 * @return baseSite set as current site or null
	 * @throws InvalidResourceException
	 */
	BaseSiteModel initializeSiteFromRequest(final HttpServletRequest request) throws TmaInvalidResourceException;

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
	 * Method set current currency based on information from request and recalculate cart for current session
	 *
	 * @param request
	 *           - request from which we should get currency information
	 * @return currency set as current
	 * @throws TmaUnsupportedCurrencyException
	 * @throws TmaRecalculationException
	 */
	CurrencyModel setCurrencyFromRequest(final HttpServletRequest request) throws TmaUnsupportedCurrencyException,
			TmaRecalculationException;

}

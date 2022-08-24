/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.context;

import de.hybris.platform.core.model.c2l.LanguageModel;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for context information loader.
 *
 * @since 2111
 */
public interface AgrContextInformationLoader
{
	/**
	 * Sets current language based on information from request.
	 *
	 * @param request
	 * 		should contain language information
	 * @return language set as current
	 */
	LanguageModel setLanguageFromRequest(final HttpServletRequest request);

}

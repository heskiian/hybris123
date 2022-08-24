/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoaddon.controllers.util;


import javax.servlet.http.HttpServletRequest;


/**
 * Helper that contains product data related utility methods.
 */
public class ProductDataHelper
{
	public static final String CURRENT_PRODUCT = "currentProductCode";

	private ProductDataHelper()
	{
		// Deliberately left empty.
	}

	/**
	 * Get current product's code from a request.
	 *
	 * @param request
	 * @return
	 */
	public static String getCurrentProduct(final HttpServletRequest request)
	{
		return (String) request.getAttribute(CURRENT_PRODUCT);
	}

	/**
	 * Set current product's code to a request.
	 *
	 * @param request
	 * @param currentProductCode
	 */
	public static void setCurrentProduct(final HttpServletRequest request, final String currentProductCode)
	{
		request.setAttribute(CURRENT_PRODUCT, currentProductCode);
	}
}

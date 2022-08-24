/*
 *
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.constants;

import de.hybris.platform.util.Config;


@SuppressWarnings(
{ "deprecation", "squid:CallToDeprecatedMethod" })
public class B2ctelcocommercewebservicescommonsConstants extends GeneratedB2ctelcocommercewebservicescommonsConstants
{
	public static final String EXTENSIONNAME = "b2ctelcocommercewebservicescommons";

	public static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";

	public static final String TMA_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";

	public static final String TMA_PRODUCT_OFFERING_API_VERSION = EXTENSIONNAME + ".tmaProductOfferingApi.version";

	public static final String PRODUCT_SPECIFICATION_API_HREF = EXTENSIONNAME + ".productSpecificationApi.href";

	public static final String PRODUCT_OFFERING_API_HREF = EXTENSIONNAME + ".productOfferingApi.href";

	public static final String PRODUCT_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION)
			+ Config.getParameter(PRODUCT_SPECIFICATION_API_HREF);

	public static final String PRODUCT_OFFERING_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION) + Config.getParameter(PRODUCT_OFFERING_API_HREF);

	private static final String TMA_USAGE_SPECIFICATION_API_HREF = EXTENSIONNAME + ".usageSpecificationApi.href";
	private static final String TMA_USAGE_SPECIFICATION_API_VERSION = EXTENSIONNAME + ".usageSpecificationApi.version";
	public static final String TMA_USAGE_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_USAGE_SPECIFICATION_API_VERSION) + Config.getParameter(TMA_USAGE_SPECIFICATION_API_HREF);

	private static final String TMA_PRODUCT_OFFERING_PRICE_API_HREF = EXTENSIONNAME + ".tmaProductOfferingPriceApi.href";
	private static final String TMA_PRODUCT_OFFERING_PRICE_API_VERSION = EXTENSIONNAME + ".tmaProductOfferingPriceApi.version";
	public static final String TMA_PRODUCT_OFFERING_PRICE_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
					+ Config.getParameter(TMA_PRODUCT_OFFERING_PRICE_API_VERSION) + Config.getParameter(TMA_PRODUCT_OFFERING_PRICE_API_HREF);

	private B2ctelcocommercewebservicescommonsConstants()
	{
		//empty
	}


}

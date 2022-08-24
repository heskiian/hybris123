/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoocc.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all b2ctelcoocc constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings(
{ "deprecation", "squid:CallToDeprecatedMethod" })
public final class B2ctelcooccConstants extends GeneratedB2ctelcooccConstants
{
	public static final String EXTENSIONNAME = "b2ctelcoocc"; //NOSONAR

	public static final String OPTIONS_SEPARATOR = ",";

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

	public static final String ELIGIBILTY_ROLES = "b2ctelcoocc.eligibility.admin.roles";

	public static final String ADMIN_ROLES = "b2ctelcoocc.admin.roles";

	private B2ctelcooccConstants()
	{
		//empty to avoid instantiating this constant class
	}

}

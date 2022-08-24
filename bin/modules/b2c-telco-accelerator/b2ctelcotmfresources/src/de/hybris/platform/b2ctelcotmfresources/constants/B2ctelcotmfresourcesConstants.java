/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfresources.constants;

import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;


/**
 * Global class for all B2ctelcotmfresources constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("deprecation")
public final class B2ctelcotmfresourcesConstants extends GeneratedB2ctelcotmfresourcesConstants
{
	public static final String EXTENSIONNAME = "b2ctelcotmfresources";

	// implement here constants used by this extension
	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String TMA_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String TMA_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String TMA_ORDER_API_VERSION = EXTENSIONNAME + ".tmaOrderApi.version";

	public static final String TMA_PRODUCT_OFFERING_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaProductOffering.defaultReferredType";

	private static final String TMA_PRODUCT_OFFERING_API_VERSION_V3 = EXTENSIONNAME + ".tmaProductOfferingApi.v3.version";
	private static final String PRODUCT_OFFERING_API_HREF = EXTENSIONNAME + ".productOfferingApi.href";
	public static final String PRODUCT_OFFERING_API_URL_V3 =
			Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
					+ Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION_V3) + Config.getParameter(PRODUCT_OFFERING_API_HREF);

	public static final String TMA_PRODUCT_OFFERING_PRICE_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaProductOfferingPrice"
			+ ".defaultReferredType";

	private static final String TMA_PRODUCT_OFFERING_PRICE_API_VERSION_V3 = EXTENSIONNAME + ".tmaProductOfferingPriceApi.v3"
			+ ".version";
	private static final String PRODUCT_OFFERING_PRICE_API_HREF = EXTENSIONNAME + ".productOfferingPriceApi.href";
	public static final String PRODUCT_OFFERING_PRICE_API_URL_V3 =
			Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT) + Config
					.getParameter(TMA_PRODUCT_OFFERING_PRICE_API_VERSION_V3) + Config
					.getParameter(PRODUCT_OFFERING_PRICE_API_HREF);

	public static final String TMA_API_SCHEMA = Config.getString(B2ctelcotmfresourcesConstants.API_BASE_URL, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfresourcesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfresourcesConstants.TMA_ORDER_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfresourcesConstants.TMA_API_DOC_LOCATION, StringUtils.EMPTY);

	public static final String TMA_API_SCHEMA_V3 = Config.getString(B2ctelcotmfresourcesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfresourcesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfresourcesConstants.TMA_PRODUCT_OFFERING_API_VERSION_V3, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfresourcesConstants.TMA_API_DOC_LOCATION, StringUtils.EMPTY);

	public static final String TMA_PRODUCT_ORDER_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaOrder.defaultReferredType";
	private static final String PRODUCT_ORDER_API_HREF = EXTENSIONNAME + ".orderApi.href";
	public static final String PRODUCT_ORDER_API_URL_V2 =
			Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT) + Config
					.getParameter(TMA_ORDER_API_VERSION) + Config.getParameter(PRODUCT_ORDER_API_HREF);

	private B2ctelcotmfresourcesConstants()
	{
		//empty to avoid instantiating this constant class
	}

}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptiontmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Usageconsumptiontmfwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings({ "deprecation", "squid:CallToDeprecatedMethod" })
public final class UsageconsumptiontmfwebservicesConstants extends GeneratedUsageconsumptiontmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "usageconsumptiontmfwebservices";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";

	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String UC_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String UC_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String UC_API_VERSION = EXTENSIONNAME + ".api.version";

	public static final String UC_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(UC_API_VERSION) + Config.getParameter(UC_API_DOC_LOCATION);

	public static final String UC_CONSUMPTION_SUMMARY_TYPE = Config.getParameter(EXTENSIONNAME + ".ucConsumptionSummary.type");

	private static final String NETWORK_PRODUCT_API_HREF = EXTENSIONNAME + ".networkProductApi.href";
	public static final String NETWORK_PRODUCT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(UC_API_WEBROOT) + Config.getParameter(UC_API_VERSION) + Config
					.getParameter(NETWORK_PRODUCT_API_HREF);

	public static final String UC_NETWORK_PRODUCT_TYPE = Config.getParameter(EXTENSIONNAME + ".ucNetworkProduct.type");
	public static final String UC_NETWORK_PRODUCT_BASE_TYPE = Config.getParameter(EXTENSIONNAME + ".ucNetworkProduct.baseType");

	private static final String RELATED_PARTY_URL = EXTENSIONNAME + ".relatedParty.url";

	private static final String INDIVIDUAL_RELATED_PARTY_API_HREF = EXTENSIONNAME + ".individualRelatedPartyApi.href";
	public static final String INDIVIDUAL_RELATED_PARTY_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(UC_API_WEBROOT) + Config.getParameter(UC_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(INDIVIDUAL_RELATED_PARTY_API_HREF);

	private static final String ORGANIZATION_RELATED_PARTY_API_HREF = EXTENSIONNAME + ".organizationRelatedPartyApi.href";
	public static final String ORGANIZATION_RELATED_PARTY_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(UC_API_WEBROOT) + Config.getParameter(UC_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(ORGANIZATION_RELATED_PARTY_API_HREF);

	public static final String UC_USAGE_VOLUME_BALANCE_TYPE = Config.getParameter(EXTENSIONNAME + ".ucUsageVolumeBalance.type");

	public static final String UC_USAGE_VOLUME_PRODUCT_TYPE = Config.getParameter(EXTENSIONNAME + ".ucUsageVolumeProduct.type");
	public static final String UC_USAGE_VOLUME_PRODUCT_BASE_TYPE = Config
			.getParameter(EXTENSIONNAME + ".ucUsageVolumeProduct.baseType");

	public static final String UC_NETWORK_PRODUCT_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".ucNetworkProduct"
			+ ".referredType");



	private UsageconsumptiontmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}

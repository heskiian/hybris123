/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Partytmfwebservices constants. You can add global constants for your extension into this class.
 *
 * @since 2108
 */
public final class PartytmfwebservicesConstants extends GeneratedPartytmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "partytmfwebservices";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";
	@SuppressWarnings("squid:S2068")
	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";

	private static final String PARTY_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String PARTY_API_VERSION = EXTENSIONNAME + ".api.version";
	private static final String PARTY_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	public static final String PARTY_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(PARTY_API_VERSION) + Config.getParameter(PARTY_API_DOC_LOCATION);

	private static final String ATTACHMENT_API_HREF = EXTENSIONNAME + ".AttachmentApi.href";
	public static final String ATTACHMENT_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_API_WEBROOT)
			+ Config.getParameter(PARTY_API_VERSION)
			+ Config.getParameter(ATTACHMENT_API_HREF);
	public static final String ATTACHMENT_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".attachment.defaultReferredType");

	private static final String INDIVIDUAL_API_HREF = EXTENSIONNAME + ".IndividualApi.href";
	public static final String INDIVIDUAL_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_API_WEBROOT)
			+ Config.getParameter(PARTY_API_VERSION)
			+ Config.getParameter(INDIVIDUAL_API_HREF);
	public static final String INDIVIDUAL_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".individual.defaultReferredType");

	private static final String ORGANIZATION_API_HREF = EXTENSIONNAME + ".OrganizationApi.href";
	public static final String ORGANIZATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_API_WEBROOT)
			+ Config.getParameter(PARTY_API_VERSION)
			+ Config.getParameter(ORGANIZATION_API_HREF);
	public static final String ORGANIZATION_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".organization.defaultReferredType");

	private static final String PARTY_API_HREF = EXTENSIONNAME + ".PartyApi.href";
	public static final String PARTY_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_API_WEBROOT)
			+ Config.getParameter(PARTY_API_VERSION)
			+ Config.getParameter(PARTY_API_HREF);
	public static final String PARTY_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".party.defaultReferredType");

	private PartytmfwebservicesConstants()
	{

	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroletmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Partyroletmfwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings({ "deprecation", "squid:CallToDeprecatedMethod" })
public final class PartyroletmfwebservicesConstants extends GeneratedPartyroletmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "partyroletmfwebservices";
	public static final String AUTHORIZATION_SCOPE_PROPERTY = EXTENSIONNAME + ".oauth.scope";
	public static final String LICENSE_URL_PROPERTY = EXTENSIONNAME + ".license.url";
	public static final String TERMS_OF_SERVICE_URL_PROPERTY = EXTENSIONNAME + ".terms.of.service.url";
	public static final String LICENSE_PROPERTY = EXTENSIONNAME + ".licence";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";
	public static final String API_VERSION = "1.0.0";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";
	@SuppressWarnings("squid:S2068")
	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";

	public static final String SAMPLE_MAP_STRING_KEY = "StringKey";
	public static final String SAMPLE_MAP_STRING_VALUE = "StringValue";
	public static final String SAMPLE_MAP_INTEGER_KEY = "integerKey";
	public static final int SAMPLE_MAP_INTEGER_VALUE = 10001;

	public static final String SAMPLE_LIST_STRING_VALUE = "new String";
	public static final double SAMPLE_LIST_DOUBLE_VALUE = 0.123d;

	public static final String HOST = "partyroletmfwebservices.host";
	public static final String HOST_DEFAULT = "hostname";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";

	private static final String PARTY_ROLE_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String PARTY_ROLE_API_VERSION = EXTENSIONNAME + ".api.version";

	private static final String ACCOUNT_API_HREF = EXTENSIONNAME + ".AccountApi.href";
	public static final String ACCOUNT_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(ACCOUNT_API_HREF);
	public static final String ACCOUNT_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".account.defaultReferredType");

	private static final String AGREEMENT_API_HREF = EXTENSIONNAME + ".AgreementApi.href";
	public static final String AGREEMENT_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(AGREEMENT_API_HREF);
	public static final String AGREEMENT_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".agreement.defaultReferredType");

	private static final String INDIVIDUAL_API_HREF = EXTENSIONNAME + ".IndividualApi.href";
	public static final String INDIVIDUAL_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(INDIVIDUAL_API_HREF);
	public static final String INDIVIDUAL_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".individual.defaultReferredType");

	private static final String ORGANIZATION_API_HREF = EXTENSIONNAME + ".OrganizationApi.href";
	public static final String ORGANIZATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(ORGANIZATION_API_HREF);
	public static final String ORGANIZATION_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".organization.defaultReferredType");

	private static final String PAYMENT_METHOD_API_HREF = EXTENSIONNAME + ".PaymentMethodApi.href";
	public static final String PAYMENT_METHOD_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(PAYMENT_METHOD_API_HREF);
	public static final String PAYMENT_METHOD_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".paymentMethod.defaultReferredType");

	private static final String PARTY_API_HREF = EXTENSIONNAME + ".PartyApi.href";
	public static final String PARTY_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(PARTY_API_HREF);
	public static final String PARTY_DEFAULT_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".party.defaultReferredType");

	private static final String PARTY_ROLE_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String PARTY_ROLE_API_HREF = EXTENSIONNAME + ".PartyRoleApi.href";
	public static final String PARTY_ROLE_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(PARTY_ROLE_API_VERSION) + Config
					.getParameter(PARTY_ROLE_API_DOC_LOCATION);
	public static final String PARTY_ROLE_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(PARTY_ROLE_API_WEBROOT)
			+ Config.getParameter(PARTY_ROLE_API_VERSION)
			+ Config.getParameter(PARTY_ROLE_API_HREF);


	private PartyroletmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}

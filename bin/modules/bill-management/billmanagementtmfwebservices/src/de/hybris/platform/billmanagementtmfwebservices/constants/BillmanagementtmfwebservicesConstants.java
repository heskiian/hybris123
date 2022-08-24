/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Billmanagementtmfwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings({ "deprecation", "squid:CallToDeprecatedMethod" })
public final class BillmanagementtmfwebservicesConstants extends GeneratedBillmanagementtmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "billmanagementtmfwebservices";
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

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String BM_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String BM_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String BM_API_VERSION = EXTENSIONNAME + ".api.version";

	public static final String BM_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_VERSION) + Config.getParameter(BM_API_DOC_LOCATION);

	private static final String RELATED_PARTY_URL = EXTENSIONNAME + ".relatedParty.url";

	public static final String RELATED_PARTY_REFERRED_TYPE_INDIVIDUAL =
			EXTENSIONNAME + ".individualRelatedParty.defaultReferredType";
	public static final String RELATED_PARTY_REFERRED_TYPE_ORGANIZATION =
			EXTENSIONNAME + ".organizationRelatedParty.defaultReferredType";

	private static final String RELATED_PARTY_API_HREF_INDIVIDUAL = EXTENSIONNAME + ".relatedPartyRefApi.individualHref";
	public static final String RELATED_PARTY_API_URL_INDIVIDUAL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(RELATED_PARTY_API_HREF_INDIVIDUAL);

	private static final String RELATED_PARTY_API_HREF_ORGANIZATION = EXTENSIONNAME + ".relatedPartyRefApi.organizationHref";
	public static final String RELATED_PARTY_API_URL_ORGANIZATION =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(RELATED_PARTY_API_HREF_ORGANIZATION);

	public static final String BM_ATTACHMENT_REFERRED = Config.getParameter(EXTENSIONNAME + ".attachment.referredType");

	private static final String CUSTOMER_BILL_API_HREF = EXTENSIONNAME + ".customerBillApi.href";
	public static final String CUSTOMER_BILL_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(CUSTOMER_BILL_API_HREF);

	private static final String BILLING_ACCOUNT_API_HREF = EXTENSIONNAME + ".billingAccountApi.href";
	public static final String BILLING_ACCOUNT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(BILLING_ACCOUNT_API_HREF);
	public static final String BILLING_ACCOUNT_DEFAULT_REFERRED = EXTENSIONNAME + ".billingAccountRef.defaultReferredType";
	public static final String BILLING_ACCOUNT_BASE_TYPE = Config.getParameter(EXTENSIONNAME + ".billingAccountRef.baseType");

	private static final String FINANCIAL_ACCOUNT_REF_API_HREF = EXTENSIONNAME + ".financialAccountRefApi.href";
	public static final String FINANCIAL_ACCOUNT_REF_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(FINANCIAL_ACCOUNT_REF_API_HREF);
	public static final String FINANCIAL_ACCOUNT_REF_DEFAULT_REFERRED = EXTENSIONNAME + ".financialAccountRef.defaultReferredType";

	private static final String PAYMENT_REF_API_HREF = EXTENSIONNAME + ".paymentRefApi.href";
	public static final String PAYMENT_REF_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BM_API_WEBROOT) + Config.getParameter(BM_API_VERSION) + Config
					.getParameter(PAYMENT_REF_API_HREF);
	private BillmanagementtmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}

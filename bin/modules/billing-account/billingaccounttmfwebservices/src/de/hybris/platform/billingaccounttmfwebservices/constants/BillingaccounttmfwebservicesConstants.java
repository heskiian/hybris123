/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccounttmfwebservices.constants;

import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;


/**
 * Global class for all billingaccounttmfwebservices constants. You can add global constants for your extension into this class.
 *
 * @since 2105
 */
public final class BillingaccounttmfwebservicesConstants extends GeneratedBillingaccounttmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "billingaccounttmfwebservices";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";
	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";
	public static final String HTTP_REQUEST_HEADER_LANGUAGE = "Accept-Language";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String BA_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String BA_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String BA_API_VERSION = EXTENSIONNAME + ".api.version";

	public static final String BA_API_SCHEMA =
			Config.getString(API_BASE_URL, StringUtils.EMPTY)
					+ Config.getString(BA_API_WEBROOT, StringUtils.EMPTY)
					+ Config.getString(BA_API_VERSION, StringUtils.EMPTY)
					+ Config.getString(BA_API_DOC_LOCATION, StringUtils.EMPTY);

	private static final String RELATED_PARTY_URL = EXTENSIONNAME + ".relatedParty.url";

	public static final String INDIVIDUAL_RELATED_PARTY_REFERRED = EXTENSIONNAME + ".individualRelatedParty.defaultReferredType";
	private static final String INDIVIDUAL_RELATED_PARTY_API_HREF = EXTENSIONNAME + ".individualRelatedPartyApi.href";
	public static final String INDIVIDUAL_RELATED_PARTY_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(INDIVIDUAL_RELATED_PARTY_API_HREF);

	public static final String ORGANIZATION_RELATED_PARTY_REFERRED =
			EXTENSIONNAME + ".organizationRelatedParty.defaultReferredType";
	private static final String ORGANIZATION_RELATED_PARTY_API_HREF = EXTENSIONNAME + ".organizationRelatedPartyApi.href";
	public static final String ORGANIZATION_RELATED_PARTY_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(RELATED_PARTY_URL) + Config.getParameter(ORGANIZATION_RELATED_PARTY_API_HREF);

	public static final String FINANCIAL_ACCOUNT_DEFAULT_REFERRED =
			EXTENSIONNAME + ".financialAccountRef.defaultReferredType";
	private static final String FINANCIAL_ACCOUNT_API_HREF = EXTENSIONNAME + ".financialAccountApi.href";
	public static final String FINANCIAL_ACCOUNT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(FINANCIAL_ACCOUNT_API_HREF);

	public static final String PARTY_ACCOUNT_DEFAULT_REFERRED =
			EXTENSIONNAME + ".partyAccountRef.defaultReferredType";
	private static final String PARTY_ACCOUNT_API_HREF = EXTENSIONNAME + ".partyAccountApi.href";
	public static final String PARTY_ACCOUNT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(PARTY_ACCOUNT_API_HREF);

	public static final String BILLING_ACCOUNT_DEFAULT_REFERRED =
			EXTENSIONNAME + ".billingAccountRef.defaultReferredType";
	private static final String BILLING_ACCOUNT_API_HREF = EXTENSIONNAME + ".billingAccountApi.href";
	public static final String BILLING_ACCOUNT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(BILLING_ACCOUNT_API_HREF);

	private static final String PAYMENT_METHOD_REF_API_HREF = EXTENSIONNAME + ".paymentMethodRef.href";
	public static final String PAYMENT_METHOD_REF_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(PAYMENT_METHOD_REF_API_HREF);

	private static final String BILL_FORMAT_API_HREF = EXTENSIONNAME + ".billFormatApi.href";
	public static final String BILL_FORMAT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(BILL_FORMAT_API_HREF);
	public static final String BILL_FORMAT_REFERRED = EXTENSIONNAME + ".billFormat.defaultReferredType";

	private static final String BILL_PRESENTATION_MEDIA_API_HREF = EXTENSIONNAME + ".billPresentationMediaApi.href";
	public static final String BILL_PRESENTATION_MEDIA_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(BILL_PRESENTATION_MEDIA_API_HREF);
	public static final String BILL_PRESENTATION_MEDIA_REFERRED = EXTENSIONNAME + ".billPresentationMedia.defaultReferredType";

	private static final String BILLING_CYCLE_SPECIFICATION_HREF = EXTENSIONNAME + ".billingCycleSpecificationApi.href";
	public static final String BILLING_CYCLE_SPECIFICATION_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(BA_API_WEBROOT) + Config.getParameter(BA_API_VERSION) + Config
					.getParameter(BILLING_CYCLE_SPECIFICATION_HREF);
	public static final String BILLING_CYCLE_SPECIFICATION_REFERRED =
			EXTENSIONNAME + ".billingCycleSpecification.defaultReferredType";

	private BillingaccounttmfwebservicesConstants()
	{
	}
}

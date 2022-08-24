/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Agreementtmfwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings({ "deprecation", "squid:CallToDeprecatedMethod" })
public final class AgreementtmfwebservicesConstants extends GeneratedAgreementtmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "agreementtmfwebservices";
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

	public static final String HOST = "agreementtmfwebservices.host";
	public static final String HOST_DEFAULT = "hostname";

	public static final String HTTP_REQUEST_HEADER_LANGUAGE = "Accept-Language";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String AGR_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String AGR_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String AGR_API_VERSION = EXTENSIONNAME + ".api.version";

	public static final String AGR_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(AGR_API_VERSION) + Config.getParameter(AGR_API_DOC_LOCATION);

	public static final String AGREEMENT_SPECIFICATION_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".AgreementSpecification.defaultReferredType";
	private static final String AGREEMENT_SPECIFICATION_API_HREF = EXTENSIONNAME + ".AgreementSpecificationApi.href";
	public static final String AGREEMENT_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT)
			+ Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(AGREEMENT_SPECIFICATION_API_HREF);

	private static final String ATTACHMENT_API_HREF = EXTENSIONNAME + ".AttachmentApi.href";
	public static final String ATTACHMENT_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT)
			+ Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(ATTACHMENT_API_HREF);

	public static final String CATEGORY_REF_DEFAULT_REFERRED = EXTENSIONNAME + ".CategoryRef.defaultReferredType";
	private static final String CATEGORY_REF_API_HREF = EXTENSIONNAME + ".CategoryRefApi.href";
	public static final String CATEGORY_REF_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT) + Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(CATEGORY_REF_API_HREF);

	private static final String RELATED_PARTY_API_INDIVIDUAL_HREF = EXTENSIONNAME + ".relatedPartyApi.IndividualHref";
	private static final String RELATED_PARTY_API_ORGANIZATION_HREF = EXTENSIONNAME + ".relatedPartyApi.OrganizationHref";
	public static final String RELATED_PARTY_API_URL_INDIVIDUAL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(AGR_API_WEBROOT) + Config.getParameter(AGR_API_VERSION) + Config
					.getParameter(RELATED_PARTY_API_INDIVIDUAL_HREF);
	public static final String RELATED_PARTY_API_URL_ORGANIZATION =
			Config.getParameter(API_BASE_URL) + Config.getParameter(AGR_API_WEBROOT) + Config.getParameter(AGR_API_VERSION) + Config
					.getParameter(RELATED_PARTY_API_ORGANIZATION_HREF);

	private static final String AGREEMENT_SPECIFICATION_RELATIONSHIP_API_HREF = EXTENSIONNAME +
			".AgreementSpecificationRelationshipApi.href";
	public static final String AGREEMENT_SPECIFICATION_RELATIONSHIP_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT) + Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(AGREEMENT_SPECIFICATION_RELATIONSHIP_API_HREF);

	public static final String AGR_ATTACHMENT_REFERRED = Config.getParameter(EXTENSIONNAME +
			".attachment.referredType");

	public static final String AGREEMENT_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".Agreement.defaultReferredType";
	private static final String AGREEMENT_API_HREF = EXTENSIONNAME + ".AgreementApi.href";
	public static final String AGREEMENT_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT)
			+ Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(AGREEMENT_API_HREF);

	public static final String PRODUCT_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".Product.defaultReferredType";
	private static final String PRODUCT_API_HREF = EXTENSIONNAME + ".ProductApi.href";
	public static final String PRODUCT_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT)
			+ Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(PRODUCT_API_HREF);

	public static final String PRODUCT_OFFERING_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".ProductOffering.defaultReferredType";
	private static final String PRODUCT_OFFERING_API_HREF = EXTENSIONNAME + ".ProductOfferingApi.href";
	public static final String PRODUCT_OFFERING_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(AGR_API_WEBROOT)
			+ Config.getParameter(AGR_API_VERSION) + Config
			.getParameter(PRODUCT_OFFERING_API_HREF);

	private AgreementtmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}

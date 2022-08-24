/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproducttmfwebservices.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all subscribedproducttmfwebservices constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings({ "deprecation", "squid:CallToDeprecatedMethod" })
public final class SubscribedproducttmfwebservicesConstants extends GeneratedSubscribedproducttmfwebservicesConstants
{
	public static final String EXTENSIONNAME = "subscribedproducttmfwebservices";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";
	@SuppressWarnings("squid:S2068")
	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";

	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String SPI_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String SPI_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";
	private static final String SPI_API_VERSION = EXTENSIONNAME + ".api.version";

	public static final String SPI_API_SCHEMA =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_VERSION) + Config.getParameter(SPI_API_DOC_LOCATION);

	public static final String HTTP_REQUEST_HEADER_LANGUAGE = "Accept-Language";

	private static final String AGREEMENT_ITEM_API_HREF = EXTENSIONNAME + ".agreementItemApi.href";
	public static final String AGREEMENT_ITEM_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(AGREEMENT_ITEM_API_HREF);
	public static final String SPI_AGREEMENT_ITEM_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".spiAgreementItem"
			+ ".referredType");

	private static final String PRODUCT_API_HREF = EXTENSIONNAME + ".productApi.href";
	public static final String PRODUCT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(PRODUCT_API_HREF);
	public static final String SPI_PRODUCT_TYPE = Config.getParameter(EXTENSIONNAME + ".spiProduct.type");

	public static final String SPI_PRODUCT_TERM_TYPE = Config.getParameter(EXTENSIONNAME + ".spiProductTerm.type");

	private static final String BILLING_ACCOUNT_API_HREF = EXTENSIONNAME + ".billingAccountApi.href";
	public static final String BILLING_ACCOUNT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(BILLING_ACCOUNT_API_HREF);
	public static final String SPI_BILLING_ACCOUNT_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".spiBillingAccount"
			+ ".referredType");

	public static final String SPI_CHARACTERISTIC_TYPE = Config.getParameter(EXTENSIONNAME + ".spiCharacteristic.type");

	private static final String PRODUCT_OFFERING_API_HREF = EXTENSIONNAME + ".productOfferingApi.href";
	public static final String PRODUCT_OFFERING_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(PRODUCT_OFFERING_API_HREF);
	public static final String SPI_PRODUCT_OFFERING_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".spiProductOffering"
			+ ".referredType");

	private static final String RELATED_PRODUCT_ORDER_ITEM_API_HREF =
			EXTENSIONNAME + ".relatedProductOrderItemApi.productOrderHref";
	public static final String RELATED_PRODUCT_ORDER_ITEM_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(RELATED_PRODUCT_ORDER_ITEM_API_HREF);
	public static final String SPI_RELATED_PRODUCT_ORDER_ITEM_TYPE = Config.getParameter(EXTENSIONNAME +
			".spiRelatedProductOrderItem.type");

	private static final String PRODUCT_SPECIFICATION_API_HREF = EXTENSIONNAME + ".productSpecificationApi.href";
	public static final String PRODUCT_SPECIFICATION_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(PRODUCT_SPECIFICATION_API_HREF);
	public static final String SPI_PRODUCT_SPECIFICATION_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".spiProductSpecification.referredType");

	private static final String RESOURCE_API_HREF = EXTENSIONNAME + ".resourceApi.href";
	public static final String RESOURCE_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(RESOURCE_API_HREF);
	public static final String SPI_RESOURCE_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".spiResource.referredType");

	private static final String SERVICE_API_HREF = EXTENSIONNAME + ".serviceApi.href";
	public static final String SERVICE_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(SERVICE_API_HREF);
	public static final String SPI_SERVICE_REFERRED_TYPE = Config.getParameter(EXTENSIONNAME + ".spiService.referredType");

	private static final String RELATED_PARTY_API_INDIVIDUAL_HREF = EXTENSIONNAME + ".relatedPartyApi.IndividualHref";
	private static final String RELATED_PARTY_API_ORGANIZATION_HREF = EXTENSIONNAME + ".relatedPartyApi.OrganizationHref";
	public static final String RELATED_PARTY_API_URL_INDIVIDUAL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(RELATED_PARTY_API_INDIVIDUAL_HREF);
	public static final String RELATED_PARTY_API_URL_ORGANIZATION =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(RELATED_PARTY_API_ORGANIZATION_HREF);
	public static final String SPI_RELATED_PARTY_TYPE = Config.getParameter(EXTENSIONNAME + ".spiRelatedParty.type");

	private static final String RELATED_PLACE_API_HREF = EXTENSIONNAME + ".relatedPlaceApi.href";
	public static final String RELATED_PLACE_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(RELATED_PLACE_API_HREF);
	public static final String SPI_RELATED_PLACE_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".spiRelatedPlace" + ".referredType");

	public static final String SPI_PRODUCT_PRICE_TYPE = Config.getParameter(EXTENSIONNAME + ".spiProductPrice.type");

	public static final String SPI_PRICE_TYPE = Config.getParameter(EXTENSIONNAME + ".spiPrice.type");

	public static final String SPI_PRICE_ALTERATION_TYPE = Config.getParameter(EXTENSIONNAME + ".spiPriceAlteration.type");
	public static final String SPI_PRICE_ALTERATION_BASE_TYPE = Config
			.getParameter(EXTENSIONNAME + ".spiPriceAlteration.baseType");

	private static final String PRODUCT_OFFERING_PRICE_API_HREF = EXTENSIONNAME + ".productOfferingPriceApi.href";
	public static final String PRODUCT_OFFERING_PRICE_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(SPI_API_WEBROOT) + Config.getParameter(SPI_API_VERSION) + Config
					.getParameter(PRODUCT_OFFERING_PRICE_API_HREF);
	public static final String SPI_PRODUCT_OFFERING_PRICE_REFERRED_TYPE = Config
			.getParameter(EXTENSIONNAME + ".spiProductOfferingPrice" + ".type");

	public static final String SPI_PRODUCT_RELATIONSHIP_TYPE = Config.getParameter(EXTENSIONNAME + ".spiProductRelationship.type");

	private SubscribedproducttmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}
}

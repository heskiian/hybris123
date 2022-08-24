/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.constants;

import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;


/**
 * Global class for all B2ctelcotmfwebservices constants. You can add global constants for your extension into this
 * class.
 */
@SuppressWarnings("deprecation")
public final class B2ctelcotmfwebservicesConstants extends GeneratedB2ctelcotmfwebservicesConstants //NOSONAR
{
	public static final String EXTENSIONNAME = "b2ctelcotmfwebservices";
	public static final String DOCUMENTATION_DESC_PROPERTY = EXTENSIONNAME + ".documentation.desc";
	public static final String DOCUMENTATION_TITLE_PROPERTY = EXTENSIONNAME + ".documentation.title";

	public static final String AUTHORIZATION_URL = "/authorizationserver/oauth/token";
	public static final String PASSWORD_AUTHORIZATION_NAME = "oauth2_password";
	public static final String CLIENT_CREDENTIAL_AUTHORIZATION_NAME = "oauth2_client_credentials";

	public static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	public static final String TMA_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	public static final String TMA_API_DOC_LOCATION = EXTENSIONNAME + ".api.response.schemaLocation";

	public static final String TMA_PRODUCT_OFFERING_API_VERSION = EXTENSIONNAME + ".tmaProductOfferingApi.version";
	public static final String TMA_PRODUCT_OFFERING_API_VERSION_V3 = EXTENSIONNAME + ".tmaProductOfferingApi.v3.version";
	public static final String TMA_PRODUCT_OFFERING_PRICE_API_VERSION_V3 = EXTENSIONNAME
			+ ".tmaProductOfferingPriceApi.v3.version";

	public static final String TMA_ATTACHMENT_API_VERSION_V3 = EXTENSIONNAME + ".tmaAttachmentApi.v3.version";

	public static final String TMA_PRODUCT_ORDER_API_VERSION = EXTENSIONNAME + ".productOrderApi.version";
	public static final String TMA_PARTY_API_VERSION = EXTENSIONNAME + ".tmaPartyApi.version";
	public static final String TMA_ADDRESS_API_VERSION = EXTENSIONNAME + ".tmaAddressApi.version";
	public static final String TMA_CHANNEL_API_VERSION = EXTENSIONNAME + ".tmaChannelApi.version";
	public static final String TMA_CATEGORY_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaCategory.defaultReferredType";
	public static final String TMA_POP_DEFAULT_REFERRED = EXTENSIONNAME + ".productOfferingPrice.defaultReferredType";
	public static final String TMA_PRODUCT_OFFERING_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaProductOffering.defaultReferredType";
	public static final String TMA_PORELATIONSHIP_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaPoRelationship.defaultReferredType";
	public static final String TMA_CHANNEL_DEFAULT_REFERRED = EXTENSIONNAME + ".tmaChannel.defaultReferredType";
	public static final String TMA_PAYMENT_METHOD_DEFAULT_REFERRED = EXTENSIONNAME + ".paymentMethod.defaultReferredType";
	public static final String TMA_PAYMENT_DEFAULT_REFERRED = EXTENSIONNAME + ".payment.defaultReferredType";
	public static final String TMA_PRODUCTSPECIFICATION_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".tmaProductSpecification.defaultReferredType";
	public static final String PRODUCT_OFFERING_TERM_DEFAULT_REFERRED = EXTENSIONNAME + ".productOfferingTerm"
			+ ".defaultReferredType";
	public static final String PRODUCT_OFFERING_RECOMMENDATION_DEFAULT_REFERRED = EXTENSIONNAME
			+ ".productOfferingRecommendation.defaultReferredType";
	public static final String PLACE_DEFAULT_REFFERED = EXTENSIONNAME + ".place.defaultReferredType";

	public static final String PRODUCT_OFFERING_API_HREF = EXTENSIONNAME + ".productOfferingApi.href";
	public static final String PRODUCT_OFFERING_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION) + Config.getParameter(PRODUCT_OFFERING_API_HREF);

	public static final String PRODUCT_OFFERING_API_URL_V3 = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION_V3)
			+ Config.getParameter(PRODUCT_OFFERING_API_HREF);

	public static final String PRODUCT_ORDER_API_HREF = EXTENSIONNAME + ".productOrderApi.href";
	public static final String PRODUCT_ORDER_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PRODUCT_ORDER_API_VERSION) + Config.getParameter(PRODUCT_ORDER_API_HREF);

	public static final String PRODUCT_OFFERING_PRICE_API_HREF = EXTENSIONNAME + ".productOfferingPriceApi.href";
	public static final String PRODUCT_OFFERING_PRICE_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION)
			+ Config.getParameter(PRODUCT_OFFERING_PRICE_API_HREF);

	public static final String PRODUCT_SPECIFICATION_API_HREF = EXTENSIONNAME + ".productSpecificationApi.href";
	public static final String PRODUCT_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION)
			+ Config.getParameter(PRODUCT_SPECIFICATION_API_HREF);

	public static final String PRODUCT_OFFERING_TERM_API_VERSION = EXTENSIONNAME + ".productOfferingTermApi.version";
	public static final String PRODUCT_OFFERING_TERM_API_HREF = EXTENSIONNAME + ".productOfferingTermApi.href";
	public static final String PRODUCT_OFFERING_TERM_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(PRODUCT_OFFERING_TERM_API_VERSION)
			+ Config.getParameter(PRODUCT_OFFERING_TERM_API_HREF);

	public static final String CATEGORY_API_HREF = EXTENSIONNAME + ".categoryApi.href";
	public static final String CATEGORY_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PRODUCT_OFFERING_API_VERSION) + Config.getParameter(CATEGORY_API_HREF);

	public static final String CATALOG_API_HREF = EXTENSIONNAME + ".catalogApi.href";
	public static final String TMA_CATALOG_API_VERSION = EXTENSIONNAME + ".tmaCatalogApi.version";
	public static final String CATALOG_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_CATALOG_API_VERSION) + Config.getParameter(CATALOG_API_HREF);

	public static final String RELATEDPARTYREF_API_HREF = EXTENSIONNAME + ".relatedPartyRefApi.href";
	public static final String RELATEDPARTYREF_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PARTY_API_VERSION) + Config.getParameter(RELATEDPARTYREF_API_HREF);

	public static final String ADDRESS_API_HREF = EXTENSIONNAME + ".addressApi.href";
	public static final String ADDRESS_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_ADDRESS_API_VERSION) + Config.getParameter(ADDRESS_API_HREF);

	public static final String CHANNEL_API_HREF = EXTENSIONNAME + ".channelApi.href";
	public static final String CHANNEL_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_CHANNEL_API_VERSION) + Config.getParameter(CHANNEL_API_HREF);

	public static final String ATTACHMENT_API_HREF = EXTENSIONNAME + ".attachmentApi.href";
	public static final String ATTACHMENT_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_CHANNEL_API_VERSION) + Config.getParameter(ATTACHMENT_API_HREF);

	public static final String ATTACHMENT_API_URL_V3 = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_ATTACHMENT_API_VERSION_V3) + Config.getParameter(ATTACHMENT_API_HREF);

	private static final String TMA_DELIVERY_MODE_API_HREF = EXTENSIONNAME + ".tmaDeliveryModeApi.href";
	private static final String TMA_DELIVERY_MODE_API_VERSION = EXTENSIONNAME + ".tmaDeliveryModeApi.version";
	public static final String DELIVERY_MODE_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_DELIVERY_MODE_API_VERSION) + Config.getParameter(TMA_DELIVERY_MODE_API_HREF);
	public static final String DELIVERY_MODE_API_REFERRED_TYPE = EXTENSIONNAME + ".deliveryModeApi.referredType";

	public static final String TMA_PARTY_ROLE_API_VERSION = EXTENSIONNAME + ".tmaPartyRoleApi.version";
	public static final String TMA_PARTY_ROLE_API_HREF = EXTENSIONNAME + ".tmaPartyRoleApi.href";
	public static final String TMA_PARTY_ROLE_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PARTY_ROLE_API_VERSION) + Config.getParameter(TMA_PARTY_ROLE_API_HREF);

	public static final String TMA_SUBSCRIPTION_BASE_API_VERSION = EXTENSIONNAME + ".subscriptionBaseApi.version";
	public static final String TMA_SUBSCRIPTION_BASE_API_HREF = EXTENSIONNAME + ".subscriptionBaseApi.href";
	public static final String TMA_SUBSCRIPTION_BASE_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_SUBSCRIPTION_BASE_API_VERSION)
			+ Config.getParameter(TMA_SUBSCRIPTION_BASE_API_HREF);

	private static final String BILLING_ACCOUNT_REF_API_HREF = EXTENSIONNAME + ".billingAccountRefApi.href";
	private static final String BILLING_ACCOUNT_REF_API_VERSION = EXTENSIONNAME + ".billingAccountRefApi.version";
	public static final String BILLING_ACCOUNT_REF_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(BILLING_ACCOUNT_REF_API_VERSION)
			+ Config.getParameter(BILLING_ACCOUNT_REF_API_HREF);

	private static final String PROMOTION_REF_API_HREF = EXTENSIONNAME + ".promotionRefApi.href";
	private static final String PROMOTION_REF_API_VERSION = EXTENSIONNAME + ".promotionRefApi.version";
	public static final String PROMOTION_REF_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(PROMOTION_REF_API_VERSION) + Config.getParameter(PROMOTION_REF_API_HREF);
	public static final String PROMOTION_REF_API_REFERRED_TYPE = EXTENSIONNAME + ".promotionRefApi.defaultReferredType";

	private static final String BILLING_SYSTEM_REF_API_HREF = EXTENSIONNAME + ".billingSystemRefApi.href";
	private static final String BILLING_SYSTEM_REF_API_VERSION = EXTENSIONNAME + ".billingSystemRefApi.version";
	public static final String BILLING_SYSTEM_REF_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(BILLING_SYSTEM_REF_API_VERSION)
			+ Config.getParameter(BILLING_SYSTEM_REF_API_HREF);

	public static final String TMA_PRODUCT_API_HREF = EXTENSIONNAME + ".tmaProductApi.href";
	public static final String TMA_PRODUCT_API_VERSION = EXTENSIONNAME + ".tmaProductApi.version";

	public static final String TMA_API_BASESITEID = EXTENSIONNAME + ".api.basesiteId";
	public static final String HYBRIS_API_WEBROOT = "ext.ycommercewebservices.extension.webmodule.webroot";
	public static final String TMA_API_DEFAULT_REFERRED = EXTENSIONNAME + ".api.defaultReferredType";
	public static final String TMA_API_DEFAULT_ROLE = EXTENSIONNAME + ".api.role";
	public static final String DEFAULT_HOST = "http://localhost:9002/";

	public static final String TMA_BUCKET_LIST_WSDTO_NAME_PROPERTY = EXTENSIONNAME + ".bucketList.response.name";
	public static final String TMA_BUCKET_LIST_WSDTO_ISSHARED_PROPERTY = EXTENSIONNAME + ".bucketList.response.isShared";
	public static final String TMA_CONSUMPTION_COUNTER_LEVEL = EXTENSIONNAME + ".consumptionCounter.response.level";
	public static final String TMA_CONSUMPTION_COUNTER_COUNTERTYPE = EXTENSIONNAME + ".consumptionCounter.response.counterType";
	public static final String TMA_BALANCE_WSDTO_REMAINING_VALUE_LABEL = EXTENSIONNAME + ".balance.remainingValueLabel";

	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_HREF = EXTENSIONNAME + ".usageConsumptionReport.response.href";
	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_BASE_TYPE = EXTENSIONNAME
			+ ".usageConsumptionReport.response.baseType";
	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_ID = EXTENSIONNAME + ".usageConsumptionReport.response.id";
	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_NAME = EXTENSIONNAME + ".usageConsumptionReport.response.name";
	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_DESCRIPTION = EXTENSIONNAME
			+ ".usageConsumptionReport.response.description";
	public static final String TMA_USAGE_CONSUMPTION_REPORT_WSDTO_VALUELABEL = EXTENSIONNAME
			+ ".consumptionCounter.response.valueLabel";

	public static final String TMA_SUBSCRIPTION_API_HREF = EXTENSIONNAME + ".tmaSubscriptionApi.href";
	public static final String TMA_SUBSCRIPTION_API_BASETYPE = EXTENSIONNAME + ".tmaSubscriptionApi.reponse.basetype";
	public static final String TMA_SUBSCRIPTION_API_ID = EXTENSIONNAME + ".tmaProductApi.reponse.id";
	public static final String TMA_SUBSCRIPTION_API_NAME = EXTENSIONNAME + ".tmaProductApi.reponse.name";
	public static final String TMA_SUBSCRIPTION_API_DESCRIPTION = EXTENSIONNAME + ".tmaProductApi.reponse.description";

	public static final String TMA_PRODUCTAPI_HREF = EXTENSIONNAME + ".tmaProductApi.href";
	public static final String TMA_PRODUCTAPI_VERSION = EXTENSIONNAME + ".tmaProductApi.version";
	public static final String TMA_PRODUCTAPI_CUSTOMERVISISBLE = EXTENSIONNAME + ".tmaProductApi.response.isCustomerVisible";
	public static final String TMA_PRODUCTAPI_BASETYPE = EXTENSIONNAME + ".tmaProduct.reponse.type";

	public static final String TMA_API_ORDERAPI_URL = EXTENSIONNAME + ".tmaProductApi.response.orderApi";

	public static final String HTTP_REQUEST_PARAM_BASESITE = "baseSiteId";
	public static final String SESSION_DEFAULT_BASESITE = "session.default.baseSiteId";
	public static final String SESSION_DEFAULT_BASESTORE = "session.default.baseStoreId";
	public static final String HTTP_REQUEST_PARAM_CURRENCY = "curr";
	public static final String HTTP_REQUEST_HEADER_LANGUAGE = "Accept-Language";

	public static final String TMA_PROCESSTYPE_MANAGEMENT = EXTENSIONNAME + ".tmaProcessTypeManagement";
	public static final String TMA_PROCESSTYPE_HREF = EXTENSIONNAME + ".tmaProcessType.href";
	public static final String TMA_PROCESSTYPE_VERSION = EXTENSIONNAME + ".tmaProcessType.version";
	public static final String TMA_QUALIFIED_PROCESSTYPE_VERSION = EXTENSIONNAME + ".tmaQualifiedProcessType.version";
	public static final String TMA_QUALIFIED_PROCESSTYPE_HREF = EXTENSIONNAME + ".tmaQualifiedProcessType.href";

	public static final String TMA_PROCESS_TYPES_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PROCESSTYPE_VERSION) + Config.getParameter(TMA_PROCESSTYPE_MANAGEMENT)
			+ Config.getParameter(TMA_PROCESSTYPE_HREF);
	public static final String PROCESS_TYPE_DEFAULT_REFERRED = EXTENSIONNAME + ".processType.defaultReferredType";

	public static final String TMA_QUALIFIED_PROCESS_TYPES_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_QUALIFIED_PROCESSTYPE_VERSION)
			+ Config.getParameter(TMA_PROCESSTYPE_MANAGEMENT) + Config.getParameter(TMA_QUALIFIED_PROCESSTYPE_HREF);

	public static final String TMA_API_SCHEMA = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_API_VERSION, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DOC_LOCATION, StringUtils.EMPTY);

	public static final String TMA_API_SCHEMA_V3 = Config.getString(B2ctelcotmfwebservicesConstants.API_BASE_URL,
			StringUtils.EMPTY) + Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_WEBROOT, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCT_OFFERING_API_VERSION_V3, StringUtils.EMPTY)
			+ Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DOC_LOCATION, StringUtils.EMPTY);

	private static final String TMA_PAYMENT_METHOD_API_HREF = EXTENSIONNAME + ".paymentMethodApi.href";
	private static final String TMA_PAYMENT_METHOD_REF_TYPE_API_VERSION = EXTENSIONNAME + ".paymentMethodApi.version";
	public static final String TMA_PAYMENT_METHOD_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PAYMENT_METHOD_REF_TYPE_API_VERSION)
			+ Config.getParameter(TMA_PAYMENT_METHOD_API_HREF);

	private static final String RECOMMENDATION_API_HREF = EXTENSIONNAME + ".recommendationApi.href";
	private static final String RECOMMENDATION_API_VERSION = EXTENSIONNAME + ".recommendationApi.version";
	public static final String RECOMMENDATION_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(RECOMMENDATION_API_VERSION) + Config.getParameter(RECOMMENDATION_API_HREF);

	private static final String TMA_RELATED_PARTY_REF_TYPE_API_HREF = EXTENSIONNAME + ".relatedPartyRefTypeApi.href";
	private static final String TMA_RELATED_PARTY_REF_TYPE_API_VERSION = EXTENSIONNAME + ".relatedPartyRefTypeApi.version";
	public static final String TMA_RELATED_PARTY_REF_TYPE_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_RELATED_PARTY_REF_TYPE_API_VERSION)
			+ Config.getParameter(TMA_RELATED_PARTY_REF_TYPE_API_HREF);

	private static final String TMA_SHOPPING_CART_API_HREF = EXTENSIONNAME + ".shoppingCartApi.href";
	private static final String TMA_SHOPPING_CART_API_VERSION = EXTENSIONNAME + ".shoppingCartApi.version";
	public static final String SHOPPING_CART_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_SHOPPING_CART_API_VERSION) + Config.getParameter(TMA_SHOPPING_CART_API_HREF);
	private static final String TMA_BASE_STORE_API_HREF = EXTENSIONNAME + ".baseStoreRefApi.href";
	private static final String TMA_BASE_STORE_API_VERSION = EXTENSIONNAME + ".baseStoreRefApi.version";
	public static final String BASE_STORE_REF_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_BASE_STORE_API_VERSION) + Config.getParameter(TMA_BASE_STORE_API_HREF);

	public static final String TMA_PLACE_REF_DEFAULT_REFERRED = EXTENSIONNAME + ".placeRef.defaultReferredType";
	private static final String TMA_PLACE_REF_API_HREF = EXTENSIONNAME + ".placeRefApi.href";
	private static final String TMA_PLACE_REF_API_VERSION = EXTENSIONNAME + ".placeRefApi.version";
	public static final String TMA_PLACE_REF_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PLACE_REF_API_VERSION) + Config.getParameter(TMA_PLACE_REF_API_HREF);

	private static final String TMA_PRODUCT_SPEC_CHARACTERISTIC_API_VERSION = EXTENSIONNAME
			+ ".productSpecCharacteristicApi.version";
	private static final String PRODUCT_SPEC_CHARACTERISTIC_API_HREF = EXTENSIONNAME + ".productSpecCharacteristicApi.href";
	public static final String PRODUCT_SPEC_CHARACTERISTIC_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_SPEC_CHARACTERISTIC_API_VERSION)
			+ Config.getParameter(PRODUCT_SPEC_CHARACTERISTIC_API_HREF);

	private static final String PRODUCT_API_VERSION = EXTENSIONNAME + ".productApi.version";
	private static final String PRODUCT_API_HREF = EXTENSIONNAME + ".productApi.href";
	public static final String PRODUCT_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(PRODUCT_API_VERSION) + Config.getParameter(PRODUCT_API_HREF);

	public static final String TMA_USAGE_SPECIFICATION_REF_DEFAULT_REFERRED = EXTENSIONNAME + ".usageSpecificationRef"
			+ ".defaultReferredType";
	private static final String TMA_USAGE_SPECIFICATION_REF_API_HREF = EXTENSIONNAME + ".usageSpecificationRefApi.href";
	private static final String TMA_USAGE_SPECIFICATION_REF_API_VERSION = EXTENSIONNAME + ".usageSpecificationRefApi.version";
	public static final String TMA_USAGE_SPECIFICATION_REF_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_USAGE_SPECIFICATION_REF_API_VERSION)
			+ Config.getParameter(TMA_USAGE_SPECIFICATION_REF_API_HREF);

	public static final String RECOMMENDATION_API_OFFER_TYPE = EXTENSIONNAME + ".recommendationApi.type";
	public static final String ELIGIBILTY_ROLES = "b2ctelcotmfwebservices.eligibility.admin.roles";
	public static final String RELATED_PARTY_ID = "relatedParty.Id";
	public static final String PRODUCT_OFFERING_RELATED_PARTY_ID = "productOfferingPrice.relatedParty.id";
	public static final String PRICE_CONTEXT_RELATED_PARTY_ID = "priceContext.relatedParty.id";
	public static final String PRODUCT_OFFERING_PRICE_API_URL_V3 = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_PRODUCT_OFFERING_PRICE_API_VERSION_V3)
			+ Config.getParameter(PRODUCT_OFFERING_PRICE_API_HREF);

	private static final String PRICING_LOGIC_ALGORITHM_API = EXTENSIONNAME + ".tmaPricingLogicAlgorithmApi.version";
	private static final String PRICING_LOGIC_ALGORITHM_API_HREF = EXTENSIONNAME + ".tmaPricingLogicAlgorithmApi.href";
	public static final String PRICING_LOGIC_ALGORITHM_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(PRICING_LOGIC_ALGORITHM_API)
			+ Config.getParameter(PRICING_LOGIC_ALGORITHM_API_HREF);

	private static final String SERVICE_SPECIFICATION_API = EXTENSIONNAME + ".serviceSpecificationApi.version";
	private static final String SERVICE_SPECIFICATION_API_HREF = EXTENSIONNAME + ".serviceSpecificationApi.href";
	public static final String SERVICE_SPECIFICATION_API_URL = Config.getParameter(API_BASE_URL)
			+ Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(SERVICE_SPECIFICATION_API)
			+ Config.getParameter(SERVICE_SPECIFICATION_API_HREF);
	public static final String TMA_SERVICE_SPECIFICATION_DEFAULT_REFERRED = EXTENSIONNAME + ".serviceSpecificationRef"
			+ ".defaultReferredType";

	public static final String TMA_PRODUCT_API_VERSION_V3 = EXTENSIONNAME + ".tmaProductApi.v3.version";
	public static final String PRODUCT_API_HREF_V3 = EXTENSIONNAME + ".tmaProductApi.v3.href";
	public static final String PRODUCT_API_URL_V3 = Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT)
			+ Config.getParameter(TMA_PRODUCT_API_VERSION_V3) + Config.getParameter(PRODUCT_API_HREF_V3);
	public static final String BUNDLESPRODUCTRELATIONSHIP = "bundles";
	public static final String TARGETSPRODUCTRELATIONSHIP = "targets";
	public static final String TMA_PRODUCT_OFFERING_OPERATIONAL_REFERRED = EXTENSIONNAME+ ".tmaProductOffering.operationalReferredType";
	public static final String ORDER_API_URL = Config.getParameter(API_BASE_URL) + Config.getParameter(HYBRIS_API_WEBROOT)
			+ Config.getParameter(TMA_API_BASESITEID) + Config.getParameter(TMA_API_ORDERAPI_URL);
	public static final String PRODUCT = "product";

	private B2ctelcotmfwebservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}

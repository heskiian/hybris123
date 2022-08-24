/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.constants.B2ctelcofacadesConstants;
import de.hybris.platform.b2ctelcofacades.data.TmaAccessTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaBillingAccountFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcoservices.constants.GeneratedB2ctelcoservicesConstants.Enumerations.SubscriptionStatus;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.enums.TmaUsageType;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaNormalizedTermOfServiceFrequencyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.impl.DefaultTmaUnitConversionStrategy;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * For testing the {@link DefaultTmaSubscribedProductFacade} class.
 * @since 6.6
 */
@UnitTest
public class DefaultTmaSubscribedProductFacadeUnitTest
{
	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";
	private static final String SUBSCRIBER_IDENTITY_TEST = "00012344321";
	private static final String BILLING_SYSTEM_ID = "IN";
	protected static final String CUSTOMER_UID = "customerLOL";
	private static final String BILLING_ACCOUNT_ID = "billingAccountId";
	private static final String BILLING_ID = "billingId";

	private static final TmaAccessType ACCESS_TYPE = TmaAccessType.OWNER;
	private static final String PRODUCT_CODE = "productCodeTest";
	private static final String SUBSCRIPTION_PRODUCT_CODE = "productCodeTest1";
	private static final String PARENT_PRODUCT_CODE = "parentProductCodeTest";

	private static final String TMA_SERVICE_AVG_USAGE_ID = "LOCAL_STD_DATA_USG";
	private static final String TMA_SERVICE_AVG_USAGE_VALUE = "996";
	private static final String TMA_SERVICE_AVG_USAGE_UNIT_OF_MEASURE = "MB";
	private static final String TMA_PSCV_UNIT_OF_MEASURE = "GB";

	private static final String SUBSCRIBER_IDENTITY_NEG = "NEG_Test";
	private static final String BILLING_SYSTEM_ID_NEG = "NEG_Test";
	private static final String TMA_PSCV_VALUE = "1";
	private static final String CHILDPRODUCT_PSCV_VALUE = "10";
	private static final String PSC_VALUE = "voice";
	public static final String PRODUCT_CATALOG = "b2ctelcoProductCatalog";
	public static final String PRODUCT_CATALOG_VERSION = "Online";

	private CustomerModel customerModel;
	private Set<TmaSubscribedProductModel> tmaSubscribedProductModels;
	private TmaSubscribedProductModel tmaSubscribedProductModel;
	private TmaSubscriptionBaseModel tmaSubscriptionBaseModel;
	private TmaAverageServiceUsageModel tmaAverageServiceUsageModel;
	private TmaSubscriptionAccessData tmaSubscriptionAccessData;
	private TmaSubscribedProductData tmaSubscribedProductData;
	private List<TmaSubscriptionAccessData> tmaSubscriptionAccessDatas;
	private List<TmaSubscribedProductData> tmaSubscribedProductDataList;
	private TmaSubscriptionAccessModel tmaSubscriptionAccessModel;
	private ProductData productData;
	private ProductModel productModel;
	private Set<ProductModel> productModels;
	private Set<TmaSubscriptionAccessModel> tmaSubscriptionAccessModels;
	private TmaAverageServiceUsageData tmaAverageServiceUsageData;
	private Set<TmaAverageServiceUsageModel> tmaAverageServiceUsageModels;
	private Set<TmaAverageServiceUsageData> tmaAverageServiceUsageDatas;
	private Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue;
	private List<TmaSubscriptionAccessModel> tmaSubscriptionAccessModelList;
	private OrderModel orderModel;
	private AbstractOrderEntryModel orderEntry;
	private List<AbstractOrderEntryModel> orderEntries;
	private TmaBillingAccountData tmaBillingAccountData;
	private TmaSubscriptionBaseData tmaSubscriptionBaseData;
	private TmaBundledProductOfferingModel bundleProduct;
	private Set<TmaProductOfferingModel> productOfferings;
	private List<CategoryModel> categoryList;
	private TmaProductOfferingModel productOfferingModel;
	private CategoryModel categoryModel;
	private SubscriptionBillingData subscriptionBillingData;
	private List<SubscriptionBillingData> subscriptionBillingDataList;
	private Date updatedEndDate;
	private TmaProductSpecCharacteristicValueModel pscvModel;
	private TmaProductSpecCharacteristicValueModel childProductPscv;
	private Set<TmaProductSpecCharacteristicValueModel> pscvModels;
	private Set<TmaProductSpecCharacteristicValueModel> childProductPscvs;
	private TmaProductSpecCharacteristicModel pscModel;
	private TmaProductOfferingModel subscriptionProduct;
	private UsageUnitModel usageUnitModel;
	private SubscriptionTermModel subscriptionTermModel;
	private final String defaultSubscriptionProductsCategories = "addons|plans";

	@Mock
	private TmaPoService tmaPoService;
	@Mock
	private List<ProductReferenceData> productReferenceList;
	@Mock
	private UserService userService;
	@Mock
	private ModelService modelService;
	@Mock
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	@Mock
	private ProductFacade productFacade;
	@Mock
	private Converter<TmaSubscribedProductModel, TmaSubscribedProductData> tmaSubscribedProductConverter;
	@Mock
	private TmaSubscribedProductService tmaSubscribedProductService;
	@Mock
	private Converter<TmaSubscribedProductData, TmaSubscribedProductModel> tmaSubscribedProductReverseConverter;
	@Mock
	private Converter<TmaAverageServiceUsageModel, TmaAverageServiceUsageData> tmaAverageServiceUsageConverter;
	@Mock
	private Converter<ProductModel, ProductData> productConverter;
	@Mock
	private CategoryService categoryService;
	@Mock
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;
	@Mock
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter;
	@Mock
	private Converter<AbstractOrderEntryModel, TmaSubscribedProductData> tmaSubscribedProductDataOrderEntryConverter;
	@Mock
	private Converter<OrderModel, TmaSubscribedProductData> tmaSubscribedProductDataOrderConverter;
	@Mock
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;
	@Mock
	private TmaBillingAccountFacade tmaBillingAccountFacade;
	@Mock
	private Converter<TmaSubscribedProductData, List<SubscriptionBillingData>> subscriptionBillingDataListConverter;
	@Mock
	private DefaultGenericDao<TmaNormalizedTermOfServiceFrequencyModel> termsOfServiceDao;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private TmaCustomerInventoryService customerInventoryService;
	private DefaultTmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;
	private TmaUnitConversionStrategy tmaUnitConversionStrategy;
	private DefaultTmaSubscribedProductFacade tmaSubscribedProductFacade;


	@Before
	public void setUp() throws SubscriptionFacadeException
	{
		MockitoAnnotations.initMocks(this);
		setPscData();
		setPscvData();
		setSubscriptionTerm();
		setTmaSubscriptionAccessData();
		setCustomerData();
		setAccessData();
		setTmaServiceAverageUsageData();
		setSubscriptionBaseData();
		setProductData();
		setProductModel();
		setTmaSubscribedProductData();
		setTmaBillingAccountData();
		setTmaSubscriptionBaseData();
		setSubscriptionBillingData();
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		tmaUnitConversionStrategy = new DefaultTmaUnitConversionStrategy();
		tmaSubscriptionAccessFacade = new DefaultTmaSubscriptionAccessFacade();
		tmaSubscriptionAccessFacade.setTmaSubscriptionAccessService(tmaSubscriptionAccessService);
		tmaSubscriptionAccessFacade.setTmaSubscriptionAccessConverter(tmaSubscriptionAccessConverter);
		subscriptionBaseServicesWithValue = new HashMap();
		tmaSubscribedProductModels = new HashSet();
		subscriptionBaseServicesWithValue.putIfAbsent(tmaSubscribedProductData, tmaAverageServiceUsageDatas);
		tmaSubscribedProductFacade = new DefaultTmaSubscribedProductFacade(customerInventoryService);
		tmaSubscribedProductFacade.setUserService(userService);
		tmaSubscribedProductFacade.setTmaSubscriptionBaseService(tmaSubscriptionBaseService);
		tmaSubscribedProductFacade.setProductFacade(productFacade);
		tmaSubscribedProductFacade.setProductConverter(productConverter);
		tmaSubscribedProductFacade.setCategoryService(categoryService);
		tmaSubscribedProductFacade.setTmaSubscribedProductConverter(tmaSubscribedProductConverter);
		tmaSubscribedProductFacade.setTmaSubscriptionAccessFacade(tmaSubscriptionAccessFacade);
		tmaSubscribedProductFacade.setTmaSubscribedProductReverseConverter(tmaSubscribedProductReverseConverter);
		tmaSubscribedProductFacade.setTmaAverageServiceUsageConverter(tmaAverageServiceUsageConverter);
		tmaSubscribedProductFacade.setTmaSubscribedProductService(tmaSubscribedProductService);
		tmaSubscribedProductFacade.setModelService(modelService);
		tmaSubscribedProductFacade.setTmaBillingAccountFacade(tmaBillingAccountFacade);
		tmaSubscribedProductFacade.setTmaSubscriptionBaseFacade(tmaSubscriptionBaseFacade);
		tmaSubscribedProductFacade.setSubscriptionBillingDataListConverter(subscriptionBillingDataListConverter);
		tmaSubscribedProductFacade.setTmaPoService(tmaPoService);
		tmaSubscribedProductFacade.setTmaUnitConversionStrategy(tmaUnitConversionStrategy);
		tmaSubscribedProductFacade.setCatalogVersionService(catalogVersionService);
		tmaSubscribedProductFacade.setDefaultSubscriptionProductsCategories(defaultSubscriptionProductsCategories);

		when(userService.getCurrentUser()).thenReturn(customerModel);
		when(tmaSubscriptionAccessFacade.findSubscriptionAccessesByPrincipal(customerModel.getUid()))
				.thenReturn(tmaSubscriptionAccessDatas);
		when(tmaSubscriptionBaseService.getSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID))
				.thenReturn(tmaSubscriptionBaseModel);
		when(tmaBillingAccountFacade.generateBillingAccount()).thenReturn(tmaBillingAccountData);
		when(tmaSubscriptionBaseFacade.generateSubscriptionBase(tmaBillingAccountData.getBillingAccountId()))
				.thenReturn(tmaSubscriptionBaseData);
		when(tmaSubscribedProductService.findSubscribedProductById(tmaSubscribedProductModel.getId()))
				.thenReturn(tmaSubscribedProductModel);
		when(tmaSubscribedProductService.findSubscribedProduct(BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY))
				.thenReturn(tmaSubscribedProductModel);
		when(categoryService.getCategoryPathForProduct(bundleProduct)).thenReturn(categoryList);
		when(productConverter.convert(bundleProduct)).thenReturn(productData);
		when(productConverter.convert(productOfferingModel)).thenReturn(productData);
		when(subscriptionBillingDataListConverter.convert(tmaSubscribedProductData)).thenReturn(subscriptionBillingDataList);
		when(productFacade.getProductReferencesForCode(PRODUCT_CODE, ProductReferenceTypeEnum.UPSELLING, new ArrayList<>(), 100))
				.thenReturn(productReferenceList);
		when(tmaPoService.getPoForCode(tmaSubscribedProductModel.getProductCode())).thenReturn(subscriptionProduct);
		when(categoryService.getCategoryPathForProduct(productModel)).thenReturn(categoryList);

		final TmaNormalizedTermOfServiceFrequencyModel yearlyFactor = createFactor(TermOfServiceFrequency.ANNUALLY, 12);
		final TmaNormalizedTermOfServiceFrequencyModel quarterlyFactor = createFactor(TermOfServiceFrequency.QUARTERLY, 3);
		final TmaNormalizedTermOfServiceFrequencyModel monthlyFactor = createFactor(TermOfServiceFrequency.MONTHLY, 1);
		mockTermsOfServiceRetrieval(TermOfServiceFrequency.ANNUALLY, yearlyFactor);
		mockTermsOfServiceRetrieval(TermOfServiceFrequency.QUARTERLY, quarterlyFactor);
		mockTermsOfServiceRetrieval(TermOfServiceFrequency.MONTHLY, monthlyFactor);
	}

	private void mockTermsOfServiceRetrieval(final TermOfServiceFrequency frequency,
			final TmaNormalizedTermOfServiceFrequencyModel factor)
	{
		final Matcher<Map<? extends String, ? extends TermOfServiceFrequency>> matcher = hasEntry(
				TmaNormalizedTermOfServiceFrequencyModel.TARGET, frequency);
		when(termsOfServiceDao.find((Map<String, ?>) argThat(matcher))).thenReturn(Collections.singletonList(factor));
	}

	private TmaNormalizedTermOfServiceFrequencyModel createFactor(final TermOfServiceFrequency convertedUnit, final Integer factor)
	{
		final TmaNormalizedTermOfServiceFrequencyModel termsOfServiceNormalizedYearly = new TmaNormalizedTermOfServiceFrequencyModel();
		termsOfServiceNormalizedYearly.setSource(TermOfServiceFrequency.MONTHLY);
		termsOfServiceNormalizedYearly.setTarget(convertedUnit);
		termsOfServiceNormalizedYearly.setNormalizationFactor(factor);
		return termsOfServiceNormalizedYearly;
	}

	@Test
	public void getSubscriptions() throws SubscriptionFacadeException
	{
		given(tmaSubscriptionAccessService.getSubscriptionAccessesByPrincipalUid(customerModel.getUid()))
				.willReturn(tmaSubscriptionAccessModelList);
		given(tmaSubscriptionAccessConverter.convert(tmaSubscriptionAccessModel)).willReturn(tmaSubscriptionAccessData);
		final Map<TmaSubscriptionAccessData, Set<TmaSubscribedProductData>> tmaSubscriptionDatas = tmaSubscribedProductFacade
				.getSubscriptions();
		tmaSubscriptionDatas.keySet()
				.forEach(tmaSubscriptionAccessData -> assertTrue("tmaSubscriptionAccessData's Principal ID is given customer ID",
						CUSTOMER_UID.equals(tmaSubscriptionAccessData.getPrincipalUid())));
	}

	@Test
	public void getSubscriptionBaseServicesWithAvgValuesTest() throws SubscriptionFacadeException
	{
		given(tmaSubscribedProductConverter.convert(tmaSubscribedProductModel)).willReturn(tmaSubscribedProductData);
		given(tmaAverageServiceUsageConverter.convert(tmaAverageServiceUsageModel)).willReturn(tmaAverageServiceUsageData);
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseServicesWithAvgValues(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		for (final TmaSubscribedProductData subscribedProductData : subscriptionBaseServicesWithValue.keySet())
		{
			assertTrue("TmaSubscribedProductData's Subscriber ID is given Subscription Base ID",
					SUBSCRIBER_IDENTITY.equals(subscribedProductData.getSubscriptionBaseId()));
			for (final TmaAverageServiceUsageData tmaAverageServiceUsageData : subscriptionBaseServicesWithValue
					.get(subscribedProductData))
			{
				assertTrue("tmaAverageServiceUsageData ID  is given Average service usages ID",
						TMA_SERVICE_AVG_USAGE_ID.equals(tmaAverageServiceUsageData.getId()));
			}
		}
	}

	@Test
	public void getSubscriptionBaseServicesWithAvgValuesTestWithInvalid() throws SubscriptionFacadeException
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseServicesWithAvgValues(SUBSCRIBER_IDENTITY_NEG, BILLING_SYSTEM_ID_NEG);
		assertTrue("subscriptionBaseServicesWithValue are empty for invalid value", subscriptionBaseServicesWithValue.isEmpty());
	}

	@Test
	public void getSubscriptionBaseServicesWithSubscribedProductIdTestWithInvalid()
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getAverageServiceUsageValueWithSubscribedProductId(tmaSubscribedProductModel);
		Assert.assertNotNull("subscriptionBaseServicesWithValue is not empty", subscriptionBaseServicesWithValue);
	}

	@Test
	public void whenSubscriberIdentityIsNull()
	{
		final ProductData product = tmaSubscribedProductFacade.getUpsellSubscribedProductForIdentity(null);
		Assert.assertNull(product);
	}

	@Test
	public void getSubscriptionBaseServicesByIdentityWithAvgValuesTestWithInvalid()
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseServicesByIdentityWithAvgValues(SUBSCRIBER_IDENTITY_NEG);
		assertTrue("subscriptionBaseServicesWithValue are empty for invalid value", subscriptionBaseServicesWithValue.isEmpty());
	}

	@Test
	public void getSubscriptionBaseProductWithAvgValuesTest() throws SubscriptionFacadeException
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseProductWithAvgValues(SUBSCRIBER_IDENTITY, tmaSubscribedProductModel.getId());
		Assert.assertEquals(SUBSCRIBER_IDENTITY, tmaSubscribedProductModel.getSubscriptionBase().getSubscriberIdentity());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getSubscriptionBaseProductWithAvgValuesWithExceptionTest() throws SubscriptionFacadeException
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseProductWithAvgValues(SUBSCRIBER_IDENTITY_NEG, tmaSubscribedProductModel.getId());
	}

	@Test
	public void getSubscriptionBaseProductWithAvgValuesIdNullTest() throws SubscriptionFacadeException
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseProductWithAvgValues(SUBSCRIBER_IDENTITY_TEST, null);
		Assert.assertNotNull(subscriptionBaseServicesWithValue);
	}

	@Test
	public void getSubscriptionBaseServicesByIdentity() throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = new HashSet<>(
				tmaSubscribedProductFacade.getSubscriptionBaseServicesByIdentity(SUBSCRIBER_IDENTITY));
		for (final TmaSubscribedProductModel tmaSubscribedProductModel : tmaSubscribedProducts)
		{
			assertTrue("Subscription Base Services Subscriber ID is given Subscription Base ID",
					SUBSCRIBER_IDENTITY.equals(tmaSubscribedProductModel.getSubscriptionBase().getSubscriberIdentity()));
		}
	}

	@Test
	public void getSubscriptionBaseProductIsNullWithAvgValuesTestWithInvalid() throws SubscriptionFacadeException
	{

		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValue = tmaSubscribedProductFacade
				.getSubscriptionBaseServicesByIdentityWithAvgValues(SUBSCRIBER_IDENTITY_NEG);
		assertTrue("subscriptionBaseServicesWithValue are empty for invalid value", subscriptionBaseServicesWithValue.isEmpty());
	}

	@Test
	public void getSubscriptionBaseServices() throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = new HashSet<>(
				tmaSubscribedProductFacade.getSubscriptionBaseServices(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID));
		for (final TmaSubscribedProductModel tmaSubscribedProductModel : tmaSubscribedProducts)
		{
			assertTrue("Subscription Base Services Subscriber ID is given Subscription Base ID",
					SUBSCRIBER_IDENTITY.equals(tmaSubscribedProductModel.getSubscriptionBase().getSubscriberIdentity()));
		}
	}

	@Test
	public void getSubscribedProductsTest()
	{
		tmaSubscribedProductModels = tmaSubscriptionBaseModel.getSubscribedProducts();
		when(tmaSubscribedProductConverter.convertAll(tmaSubscribedProductModels)).thenReturn(tmaSubscribedProductDataList);
		final List<TmaSubscribedProductData> subscribedProducts = tmaSubscribedProductFacade
				.getSubscribedProducts(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertNotNull(subscribedProducts);
		assertTrue(subscribedProducts.equals(tmaSubscribedProductDataList));
	}

	@Test
	public void getSubscribedProductsWhenNoSubscriptionBaseFound()
	{
		given(tmaSubscriptionBaseService.getSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID)).willReturn(null);
		final List<TmaSubscribedProductData> subscribedProducts = tmaSubscribedProductFacade
				.getSubscribedProducts(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertTrue(subscribedProducts.isEmpty());
	}

	@Test
	public void getSubscriptionBaseServicesWithInvalid() throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> subscribedProductModels = new HashSet<>(
				tmaSubscribedProductFacade.getSubscriptionBaseServices(SUBSCRIBER_IDENTITY_NEG, BILLING_SYSTEM_ID_NEG));
		assertTrue("TmaSubscribedProductModels shall be empty for invalid value", subscribedProductModels.isEmpty());
	}


	@Test
	public void getSubscriptionCompatibleAddonsTest() throws SubscriptionFacadeException
	{
		final Set<ProductData> productDataList = tmaSubscribedProductFacade.getSubscriptionCompatibleAddons(SUBSCRIBER_IDENTITY,
				BILLING_SYSTEM_ID);
		productDataList
				.forEach(productData -> assertTrue("productData is given productCode", PRODUCT_CODE.equals(productData.getCode())));
	}

	@Test
	public void getSubscriptionCompatibleWithSubscriberIdentityAddonsTest() throws SubscriptionFacadeException
	{
		final Set<ProductData> productDataList = tmaSubscribedProductFacade
				.getCompatibleAddonsForSubscriberIdentity(SUBSCRIBER_IDENTITY);
		productDataList
				.forEach(productData -> assertTrue("productData is given productCode", PRODUCT_CODE.equals(productData.getCode())));
	}

	@Test
	public void getSubscriptionAccessByPrincipalAndSubscriptionBaseTest()
	{
		given(tmaSubscriptionAccessService.getSubscriptionAccessByPrincipalAndSubscriptionBase(customerModel.getUid(),
				BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY)).willReturn(tmaSubscriptionAccessModel);
		final String accessType = tmaSubscribedProductFacade.getSubscriptionAccessByPrincipalAndSubscriptionBase(BILLING_SYSTEM_ID,
				SUBSCRIBER_IDENTITY);
		assertTrue("tmaSubscriptionAccessModel is given Customer accessType", ACCESS_TYPE.getCode().equals(accessType));
	}

	@Test
	public void testCreateBillingActivityData() throws SubscriptionFacadeException
	{
		given(tmaSubscribedProductConverter.convert(tmaSubscribedProductModel)).willReturn(tmaSubscribedProductData);
		final List<SubscriptionBillingData> subscriptionBillingData = tmaSubscribedProductFacade
				.createBillingActivityData(tmaSubscribedProductModel.getId());
		assertTrue(BILLING_ID.equalsIgnoreCase(subscriptionBillingData.get(0).getBillingId()));
	}

	@Test
	public void testGetUpsellingOptionsForSubscription()
	{
		final List<ProductData> productDataList = tmaSubscribedProductFacade.getUpsellingOptionsForSubscription(PRODUCT_CODE);
		assertTrue(productDataList.isEmpty());
	}

	@Test
	public void testIsServicePlanChildProducts() throws SubscriptionFacadeException
	{
		setSubscriptionBaseData();
		assertNotNull(productOfferings);
	}

	@Test
	public void testIsAddonProduct() throws SubscriptionFacadeException
	{
		assertTrue(tmaSubscribedProductFacade.isAddonProduct(bundleProduct));
	}

	@Test
	public void testProductDataIsNull()
	{
		setSubscriptionBaseData();
		tmaSubscribedProductModel.setBundledProductCode(null);
		final ProductData productData = tmaSubscribedProductFacade.getUpsellSubscribedProductData(tmaSubscribedProductModels, null);
		Assert.assertNull(productData);
	}

	private void setCustomerData()
	{
		final Set<PrincipalGroupModel> groups = new HashSet<>();
		final PrincipalGroupModel usergroup = new PrincipalGroupModel();
		usergroup.setUid("customergroup");
		modelService.save(usergroup);
		groups.add(usergroup);
		customerModel = new CustomerModel();
		customerModel.setOriginalUid(CUSTOMER_UID);
		customerModel.setUid(CUSTOMER_UID);
		customerModel.setName(CUSTOMER_UID);
		customerModel.setGroups(groups);
		customerModel.setPassword("1234");
		customerModel.setSubscriptionAccesses(tmaSubscriptionAccessModels);
		modelService.save(customerModel);
	}

	private void setAccessData()
	{
		//set AccessData
		tmaSubscriptionAccessDatas = new ArrayList();
		tmaSubscriptionAccessData = new TmaSubscriptionAccessData();
		tmaSubscriptionAccessData.setPrincipalUid(CUSTOMER_UID);
		tmaSubscriptionAccessData.setBillingSystemId(BILLING_SYSTEM_ID);
		tmaSubscriptionAccessData.setAccessType(TmaAccessTypeData.OWNER);
		tmaSubscriptionAccessData.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		tmaSubscriptionAccessDatas.add(tmaSubscriptionAccessData);
	}

	private void setSubscriptionBaseData()
	{

		productOfferingModel = new TmaProductOfferingModel();
		productOfferingModel.setCode(PRODUCT_CODE);
		productOfferingModel.setProductSpecCharacteristicValues(childProductPscvs);
		modelService.save(productOfferingModel);
		productOfferings = new HashSet<>();
		productOfferings.add(productOfferingModel);
		subscriptionProduct = new TmaProductOfferingModel();
		subscriptionProduct.setCode(SUBSCRIPTION_PRODUCT_CODE);
		subscriptionProduct.setProductSpecCharacteristicValues(pscvModels);
		modelService.save(subscriptionProduct);
		bundleProduct = new TmaBundledProductOfferingModel();
		bundleProduct.setChildren(productOfferings);
		bundleProduct.setCode(PARENT_PRODUCT_CODE);
		modelService.save(bundleProduct);
		categoryModel = new CategoryModel();
		categoryModel.setCode(B2ctelcofacadesConstants.ADDONS_CATEGORY_CODE);
		modelService.save(categoryModel);
		categoryList = new ArrayList<>();
		categoryList.add(categoryModel);

		tmaSubscribedProductModels = new HashSet<>();
		tmaSubscribedProductModel = new TmaSubscribedProductModel();
		tmaSubscriptionBaseModel = new TmaSubscriptionBaseModel();

		tmaSubscribedProductModel.setId("Id");
		tmaSubscribedProductModel.setName("Name");
		tmaSubscribedProductModel.setProductCode("ProductCode");
		tmaSubscribedProductModel.setServiceType(TmaServiceType.ADD_ON);
		tmaSubscribedProductModel.setBillingsystemId(BILLING_SYSTEM_ID);
		tmaSubscribedProductModel.setBillingSubscriptionId(SUBSCRIBER_IDENTITY);
		tmaSubscribedProductModel.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
		tmaSubscribedProductModel.setStartDate(new Date());
		tmaSubscribedProductModel.setEndDate(new Date());
		tmaSubscribedProductModel.setCancelledDate(new Date());
		tmaSubscribedProductModel.setRenewalType("Renewal");
		tmaSubscribedProductModel.setCancellable(true);
		tmaSubscribedProductModel.setBillingFrequency("BillingFrequency");
		tmaSubscribedProductModel.setContractDuration(12);
		tmaSubscribedProductModel.setContractFrequency("Yearly");
		tmaSubscribedProductModel.setAverageServiceUsages(tmaAverageServiceUsageModels);
		tmaSubscribedProductModel.setBundledProductCode(bundleProduct.getCode());
		modelService.save(tmaSubscribedProductModel);

		tmaSubscribedProductModels.add(tmaSubscribedProductModel);

		tmaSubscriptionBaseModel.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		tmaSubscriptionBaseModel.setBillingSystemId(BILLING_SYSTEM_ID);
		tmaSubscriptionBaseModel.setSubscriptionAccesses(new HashSet<>());
		tmaSubscriptionBaseModel.setSubscribedProducts(tmaSubscribedProductModels);
		tmaSubscribedProductModel.setSubscriptionBase(tmaSubscriptionBaseModel);
		modelService.save(tmaSubscriptionBaseModel);
	}

	private void setTmaSubscribedProductData()
	{
		//set SubscribedProductData
		tmaSubscribedProductDataList = new ArrayList();
		tmaSubscribedProductData = new TmaSubscribedProductData();
		tmaSubscribedProductData.setId("Id");
		tmaSubscribedProductData.setName("Name");
		tmaSubscribedProductData.setProductCode("ProductCode");
		tmaSubscribedProductData.setBillingsystemId(BILLING_SYSTEM_ID);
		tmaSubscribedProductData.setBillingSubscriptionId(SUBSCRIBER_IDENTITY);
		tmaSubscribedProductData.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
		tmaSubscribedProductData.setStartDate(new Date());
		tmaSubscribedProductData.setEndDate(new Date());
		tmaSubscribedProductData.setCancelledDate(new Date());
		tmaSubscribedProductData.setRenewalType("Renewal");
		tmaSubscribedProductData.setCancellable(true);
		tmaSubscribedProductData.setBillingFrequency("BillingFrequency");
		tmaSubscribedProductData.setContractDuration(12);
		tmaSubscribedProductData.setContractFrequency("ContractFrequency");
		tmaSubscribedProductData.setSubscriptionBaseId(SUBSCRIBER_IDENTITY);
		tmaSubscribedProductDataList.add(tmaSubscribedProductData);
	}

	private void setTmaServiceAverageUsageData()
	{
		//set ServiceAvgUsageData
		final Map<String, String> usageUnitAsuMap = new HashMap();
		usageUnitAsuMap.put("GB", "0.0009765625");
		usageUnitModel = new UsageUnitModel();
		usageUnitModel.setId(TMA_SERVICE_AVG_USAGE_UNIT_OF_MEASURE);
		usageUnitModel.setUnitConversionMap(usageUnitAsuMap);
		tmaAverageServiceUsageModels = new HashSet<>();
		tmaAverageServiceUsageModel = new TmaAverageServiceUsageModel();
		tmaAverageServiceUsageModel.setId(TMA_SERVICE_AVG_USAGE_ID);
		tmaAverageServiceUsageModel.setValue(TMA_SERVICE_AVG_USAGE_VALUE);
		tmaAverageServiceUsageModel.setUnitOfMeasure(usageUnitModel);
		tmaAverageServiceUsageModel.setUsageType(TmaUsageType.CURRENT);
		tmaAverageServiceUsageModel.setPscvId(pscvModel.getId());
		modelService.save(tmaAverageServiceUsageModel);
		tmaAverageServiceUsageModels.add(tmaAverageServiceUsageModel);
		tmaAverageServiceUsageDatas = new HashSet();
		tmaAverageServiceUsageData = new TmaAverageServiceUsageData();
		tmaAverageServiceUsageData.setId(TMA_SERVICE_AVG_USAGE_ID);
		tmaAverageServiceUsageData.setValue(TMA_SERVICE_AVG_USAGE_VALUE);
		tmaAverageServiceUsageData.setUnitOfMeasure(TMA_SERVICE_AVG_USAGE_UNIT_OF_MEASURE);
		tmaAverageServiceUsageDatas.add(tmaAverageServiceUsageData);
	}

	private void setTmaSubscriptionAccessData()
	{
		tmaSubscriptionAccessModels = new HashSet();
		tmaSubscriptionAccessModelList = new ArrayList();
		tmaSubscriptionAccessModel = new TmaSubscriptionAccessModel();
		tmaSubscriptionAccessModel.setAccessType(ACCESS_TYPE);
		modelService.save(tmaSubscriptionAccessModel);
		tmaSubscriptionAccessModels.add(tmaSubscriptionAccessModel);
		tmaSubscriptionAccessModelList.add(tmaSubscriptionAccessModel);
	}

	private void setProductData()
	{
		productData = new ProductData();
		productData.setCode(PRODUCT_CODE);
	}

	private void setProductModel()
	{
		productModel = new ProductModel();
		productModel.setCode(PRODUCT_CODE);
		productModel.setCatalogVersion(setUpCatalogVersionData());
		productModels = new HashSet<>();
		productModels.add(productModel);
	}

	private void setOrderModel(final boolean isSubscriptionTermNotNull, final boolean isSubscriptionIdNotNull)
	{
		orderModel = new OrderModel();
		orderModel.setUser(customerModel);
		customerModel.setUid(CUSTOMER_UID);

		final TmaCartSubscriptionInfoModel tmaCartSubscriptionInfoModel = new TmaCartSubscriptionInfoModel();
		tmaCartSubscriptionInfoModel.setSubscriptionTerm(subscriptionTermModel);
		tmaCartSubscriptionInfoModel.setSubscribedProductId(tmaSubscribedProductModel.getId());
		tmaCartSubscriptionInfoModel.setBillingSystemId(tmaSubscribedProductModel.getBillingsystemId());
		tmaCartSubscriptionInfoModel.setSubscriberIdentity(tmaSubscribedProductModel.getBillingSubscriptionId());

		orderEntry = new AbstractOrderEntryModel();
		orderEntry.setProduct(productModel);

		if (isSubscriptionTermNotNull)
		{
			orderEntry.setSubscriptionInfo(tmaCartSubscriptionInfoModel);
		}

		if (isSubscriptionIdNotNull)
		{
			orderEntry.setOriginalSubscriptionId(tmaSubscribedProductModel.getId());
		}
		orderEntries = new ArrayList<>();
		orderEntries.add(orderEntry);
		orderModel.setEntries(orderEntries);

	}

	private void setTmaBillingAccountData()
	{
		tmaBillingAccountData = new TmaBillingAccountData();
		tmaBillingAccountData.setBillingAccountId(BILLING_ACCOUNT_ID);
	}

	private void setTmaSubscriptionBaseData()
	{
		tmaSubscriptionBaseData = new TmaSubscriptionBaseData();
		tmaSubscriptionBaseData.setBillingSystemId(BILLING_SYSTEM_ID);
		tmaSubscriptionBaseData.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
	}

	private void setSubscriptionBillingData()
	{
		subscriptionBillingData = new SubscriptionBillingData();
		subscriptionBillingData.setBillingId(BILLING_ID);
		subscriptionBillingDataList = new ArrayList<>();
		subscriptionBillingDataList.add(subscriptionBillingData);
	}

	private void setPscData()
	{
		pscModel = new TmaProductSpecCharacteristicModel();
		pscModel.setId(PSC_VALUE);
		pscModel.setProductSpecCharacteristicValues(pscvModels);
	}

	private void setPscvData()
	{
		pscvModel = new TmaProductSpecCharacteristicValueModel();
		final LocaleProvider pscvLocaleProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl pscvItemModelContext = (ItemModelContextImpl) pscvModel.getItemModelContext();
		pscvItemModelContext.setLocaleProvider(pscvLocaleProvider);
		final Map<String, String> usageUnitPscvMap = new HashMap();
		usageUnitPscvMap.put("MB", "1024");
		usageUnitModel = new UsageUnitModel();
		usageUnitModel.setId(TMA_PSCV_UNIT_OF_MEASURE);
		usageUnitModel.setUnitConversionMap(usageUnitPscvMap);
		pscvModel.setValue(TMA_PSCV_VALUE);
		pscvModel.setUnitOfMeasure(usageUnitModel);
		pscvModel.setProductSpecCharacteristic(pscModel);
		pscvModels = new HashSet<>();
		pscvModels.add(pscvModel);
		childProductPscv = new TmaProductSpecCharacteristicValueModel();
		final LocaleProvider pscv1LocaleProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl pscv1ItemModelContext = (ItemModelContextImpl) childProductPscv.getItemModelContext();
		pscv1ItemModelContext.setLocaleProvider(pscv1LocaleProvider);
		childProductPscv.setValue(CHILDPRODUCT_PSCV_VALUE);
		childProductPscv.setUnitOfMeasure(usageUnitModel);
		childProductPscv.setProductSpecCharacteristic(pscModel);
		childProductPscvs = new HashSet<>();
		childProductPscvs.add(childProductPscv);
	}

	private void setSubscriptionTerm()
	{
		subscriptionTermModel = new SubscriptionTermModel();
		final StubLocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) subscriptionTermModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		subscriptionTermModel.setName("SubscriptionTerm");
		subscriptionTermModel.setCancellable(false);
		subscriptionTermModel.setTermOfServiceNumber(12);
		subscriptionTermModel.setTermOfServiceRenewal(TermOfServiceRenewal.AUTO_RENEWING);
	}

	private CatalogVersionModel setUpCatalogVersionData()
	{
		final CatalogModel catalogModel = mock(CatalogModel.class);
		given(catalogModel.getId()).willReturn(PRODUCT_CATALOG);
		given(catalogModel.getVersion()).willReturn(PRODUCT_CATALOG_VERSION);
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
		return catalogVersionModel;
	}

}

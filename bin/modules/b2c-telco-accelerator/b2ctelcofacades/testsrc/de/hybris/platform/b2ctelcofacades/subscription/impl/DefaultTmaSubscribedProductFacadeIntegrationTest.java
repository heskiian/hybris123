/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_BILLING_SUBSCRIPTION_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_BILLING_SYSTEM_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_CREATE_BILLING_SUBSCRIPTION_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_PARENT_PO_CODE;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertSubscribedProductIsUpdated;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductDataIsPopulatedWithDefaultValues;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductModelsPopulatedWithDefaultValues;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.generateRandomTmaSubscribedProductData;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.generateTmaSubscribedProductData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.subscription.converters.populator.TmaAverageServiceUsagePopulator;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscribedProductFacadeIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String PARENT_PO_CODE = "TEST_PARENT_PO_CODE";
	private static final String GRANDPARENT_PO_CODE = "TEST_PARENT_PO_CODE2";
	private static final String SERVICE_ID = "1000000001";
	private static final String GRANDPARENT_PO_CODE_DESCRIPTION = "TEST PARENT PO CODE2 DESCRIPTION";
	private static final String PRODUCT_CODE = "productCodeTest";
	public static final String PRODUCT_CATALOG = "b2ctelcoProductCatalog";
	public static final String PRODUCT_CATALOG_VERSION = "Online";
	public static final String SUBSCRIBER_IDENTITY_TEST = "02012344325";
	private static final String ASU_TEST = "ASU_TEST";
	private static final String ASU_PSCV = "ASU_UNLIMITED";

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;
	@Resource
	private TmaProductOfferFacade tmaProductOfferFacade;
	@Resource
	private TmaSubscribedProductService tmaSubscribedProductService;
	@Resource
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private TmaAverageServiceUsagePopulator tmaAverageServiceUsagePopulator;

	private TmaSubscribedProductData source;
	private TmaSubscribedProductData createdSubscribedProduct;
	private TmaSubscribedProductData updatedSubscribedProduct;
	private TmaSubscribedProductData subscribedProductForUpdate;
	private ProductData productdata;
	private Set<ProductData> productDataList;
	private TmaAverageServiceUsageData averageServiceUsageData;

	@Test
	public void testCreateSubscribedProduct() throws Exception
	{
		givenSource();
		whenSubscribedProductIsCreatedFromSource();
		assertSubscribedProductIsCreatedWithSourceValues();
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteSubscribedProduct()
	{
		whenSubscribedProductIsDeleted();
		findSubscribedProduct();
	}

	@Test
	public void testUpdateSubscribedProduct() throws ParseException
	{
		final TmaSimpleProductOfferingModel productOfferingModel = modelService.create(TmaSimpleProductOfferingModel.class);
		productOfferingModel.setCode(PRODUCT_CODE);
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		productOfferingModel.setCatalogVersion(catalogVersionService.getCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION));
		modelService.save(productOfferingModel);
		whenSubscribedProductIsUpdated();
		assertSubscribedProductIsUpdated(subscribedProductForUpdate, updatedSubscribedProduct);
	}

	@Test
	public void testFindSubscribedProduct()
	{
		final Object subscribedProductData = tmaSubscribedProductFacade.getSubscriptionsById(SERVICE_ID);
		assertTrue("Service Found By ID", subscribedProductData instanceof TmaSubscribedProductData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscribedProductWithNullId() throws ParseException
	{
		tmaSubscribedProductFacade.getSubscriptionsById(null);
	}

	@Test
	public void getServiceUsageUpSellProductsList() throws SubscriptionFacadeException, ParseException
	{
		givenSource();
		productDataList = tmaSubscribedProductFacade.getServiceUsageUpSellProducts(SUBSCRIBER_IDENTITY_TEST, BILLING_SYSTEM_ID);
		assertTrue("productDataList is not null", null != productDataList);
	}

	@Test
	public void getServiceUsageUpSellProductsListIsNull() throws SubscriptionFacadeException, ParseException
	{
		givenSource();
		productDataList = tmaSubscribedProductFacade.getServiceUsageUpSellProducts(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertTrue("productDataList is null", productDataList.size() == 0);
	}

	@Test
	public void getServiceUsageUpSellProductsWithSubscriberIdentity() throws SubscriptionFacadeException, ParseException
	{
		givenSource();
		productDataList = tmaSubscribedProductFacade.getServiceUsageUpSellProductsWithSubscriberIdentity(SUBSCRIBER_IDENTITY_TEST);
		Assert.assertNotNull(productDataList);
	}

	@Test
	public void getServiceUsageUpSellProductsWithSubscriberIdentityIsNull() throws SubscriptionFacadeException, ParseException
	{
		givenSource();
		productDataList = tmaSubscribedProductFacade.getServiceUsageUpSellProductsWithSubscriberIdentity(SUBSCRIBER_IDENTITY);
		assertTrue("productDataList is null", productDataList.isEmpty());
	}

	@Test
	public void getUpsellProductDataWhenSubsIDAndBillIDPresent() throws ParseException
	{
		givenSource();
		whenSubscribedProductIsCreatedFromSource();
		nonNullParentFound();
		productdata = tmaSubscribedProductFacade.upsellSubscriptionProductData(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertTrue("name", productdata.getCode().equals(GRANDPARENT_PO_CODE));
		assertTrue("description", productdata.getDescription().equalsIgnoreCase(GRANDPARENT_PO_CODE_DESCRIPTION));
	}

	@Test
	public void getUpsellProductDataWhenSubsIDPresent() throws ParseException
	{
		givenSource();
		whenSubscribedProductIsCreatedFromSource();
		nonNullParentFound();
		productdata = tmaSubscribedProductFacade.getUpsellSubscribedProductForIdentity(SUBSCRIBER_IDENTITY);
		assertTrue("name", productdata.getCode().equals(GRANDPARENT_PO_CODE));
		assertTrue("description", productdata.getDescription().equalsIgnoreCase(GRANDPARENT_PO_CODE_DESCRIPTION));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUpsellProductDataWhenBillingIDisNull()
	{
		productdata = tmaSubscribedProductFacade.upsellSubscriptionProductData(SUBSCRIBER_IDENTITY, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUpsellProductDataWhenSubscriberIDisNull()
	{
		productdata = tmaSubscribedProductFacade.upsellSubscriptionProductData(null, BILLING_SYSTEM_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUpsellProductDataWithSubscriberIDisNull()
	{
		productdata = tmaSubscribedProductFacade.getUpsellSubscribedProductForIdentity(null);
	}

	@Test
	public void testIsSubscriptionProduct() throws Exception
	{

		final TmaSimpleProductOfferingModel productOfferingModel = modelService.create(TmaSimpleProductOfferingModel.class);
		productOfferingModel.setCode(PRODUCT_CODE);
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		productOfferingModel.setCatalogVersion(catalogVersionService.getCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION));
		modelService.save(productOfferingModel);

		assertFalse(tmaSubscribedProductFacade.isSubscriptionProduct(productOfferingModel));
		final SubscriptionPricePlanModel priceRow = setUpPriceAndSubscriptionTerm();

		priceRow.setProduct(productOfferingModel);
		modelService.save(priceRow);

		final Collection<PriceRowModel> coll = new ArrayList<PriceRowModel>();
		coll.add(priceRow);
		productOfferingModel.setEurope1Prices(coll);

		modelService.save(productOfferingModel);

		assertTrue(tmaSubscribedProductFacade.isSubscriptionProduct(productOfferingModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSubscriptionTermAndPrice() throws Exception
	{
		final TmaSimpleProductOfferingModel productOfferingModel = modelService.create(TmaSimpleProductOfferingModel.class);
		productOfferingModel.setCode(PRODUCT_CODE);
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		productOfferingModel.setCatalogVersion(catalogVersionService.getCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION));
		modelService.save(productOfferingModel);
		final SubscriptionPricePlanModel priceRow = setUpPriceAndSubscriptionTerm();
		priceRow.setProduct(productOfferingModel);
		modelService.save(priceRow);
		final Collection<PriceRowModel> coll = new ArrayList<PriceRowModel>();
		coll.add(priceRow);
		productOfferingModel.setEurope1Prices(coll);
		modelService.save(productOfferingModel);
		ProductData subscriptionProductData = tmaProductOfferFacade.getPoForCode(productOfferingModel.getCode(),
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
		subscriptionProductData = tmaSubscribedProductFacade.getSubscriptionTermAndPrice(subscriptionProductData);
		assertTrue(priceRow.getPrice().equals(subscriptionProductData.getPrice().getValue().doubleValue()));
		assertTrue(priceRow.getSubscriptionTerms().stream().findFirst().get().getName()
				.equals(((SubscriptionPricePlanData) subscriptionProductData.getPrice()).getSubscriptionTerms().stream().findFirst()
						.get().getName()));
	}

	private void givenSource() throws ParseException
	{
		source = generateTmaSubscribedProductData(BILLING_SYSTEM_ID, TEST_CREATE_BILLING_SUBSCRIPTION_ID, TEST_PARENT_PO_CODE);
	}

	private void whenSubscribedProductIsCreatedFromSource()
	{
		createdSubscribedProduct = tmaSubscribedProductFacade.createSubscribedProduct(source);
	}

	private void whenSubscribedProductIsDeleted()
	{
		tmaSubscribedProductFacade.deleteSubscribedProduct(BILLING_SYSTEM_ID, TEST_BILLING_SUBSCRIPTION_ID);
	}

	private void whenSubscribedProductIsUpdated() throws ParseException
	{
		this.subscribedProductForUpdate = generateRandomTmaSubscribedProductData(BILLING_SYSTEM_ID, TEST_BILLING_SUBSCRIPTION_ID);
		this.updatedSubscribedProduct = tmaSubscribedProductFacade.updateSubscribedProduct(TEST_BILLING_SYSTEM_ID,
				TEST_BILLING_SUBSCRIPTION_ID, subscribedProductForUpdate);
	}

	private void assertSubscribedProductIsCreatedWithSourceValues() throws ParseException
	{
		assertTrue(BILLING_SYSTEM_ID.equals(createdSubscribedProduct.getBillingsystemId()));
		assertTrue(TEST_CREATE_BILLING_SUBSCRIPTION_ID.equals(createdSubscribedProduct.getBillingSubscriptionId()));
		assertCreatedSubscribedProductIsPopulatedWithSourceValues();
		final TmaSubscribedProductModel retrievedSubscribedProduct = tmaSubscribedProductService.findSubscribedProduct(
				createdSubscribedProduct.getBillingsystemId(), createdSubscribedProduct.getBillingSubscriptionId());
		assertTrue(BILLING_SYSTEM_ID.equals(retrievedSubscribedProduct.getBillingsystemId()));
		assertTrue(TEST_CREATE_BILLING_SUBSCRIPTION_ID.equals(retrievedSubscribedProduct.getBillingSubscriptionId()));
		final TmaSubscriptionBaseModel subscriptionBase = tmaSubscriptionBaseService
				.getSubscriptionBase(createdSubscribedProduct.getSubscriptionBaseId(), createdSubscribedProduct.getBillingsystemId());
		assertTmaSubscribedProductModelsPopulatedWithDefaultValues(retrievedSubscribedProduct, subscriptionBase);
	}

	private void assertCreatedSubscribedProductIsPopulatedWithSourceValues() throws ParseException
	{
		assertTmaSubscribedProductDataIsPopulatedWithDefaultValues(createdSubscribedProduct);
	}

	private TmaSubscribedProductModel findSubscribedProduct()
	{
		return tmaSubscribedProductService.findSubscribedProduct(BILLING_SYSTEM_ID, TEST_BILLING_SUBSCRIPTION_ID);
	}

	private void nonNullParentFound()
	{
		assertTrue("Parent Found", PARENT_PO_CODE.equals(createdSubscribedProduct.getParentPOCode()));
	}

	@Test(expected = NumberFormatException.class)
	public void forASUProductSpecCharValueInvalid()
	{
		final TmaAverageServiceUsageModel averageServiceUsage = getSource(ASU_PSCV);
		averageServiceUsageData = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(averageServiceUsage, averageServiceUsageData);
	}

	@Test(expected = NumberFormatException.class)
	public void forASUException()
	{
		final TmaAverageServiceUsageModel averageServiceUsage = getSource(ASU_TEST);
		averageServiceUsageData = new TmaAverageServiceUsageData();
		tmaAverageServiceUsagePopulator.populate(averageServiceUsage, averageServiceUsageData);
	}

	private TmaAverageServiceUsageModel getSource(final String value)
	{
		final TmaAverageServiceUsageModel source = modelService.create(TmaAverageServiceUsageModel.class);
		source.setId(value);
		return flexibleSearchService.getModelByExample(source);
	}

	private SubscriptionPricePlanModel setUpPriceAndSubscriptionTerm()
	{
		final SubscriptionTermModel subscriptionTermModel = modelService.create(SubscriptionTermModel.class);
		final StubLocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) subscriptionTermModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		subscriptionTermModel.setId("SubscriptionTerm");
		subscriptionTermModel.setName("SubscriptionTerm");
		subscriptionTermModel.setCancellable(false);
		subscriptionTermModel.setTermOfServiceNumber(12);
		subscriptionTermModel.setTermOfServiceRenewal(TermOfServiceRenewal.AUTO_RENEWING);
		modelService.save(subscriptionTermModel);

		final Set<SubscriptionTermModel> subscriptionTermSet = new HashSet();
		subscriptionTermSet.add(subscriptionTermModel);

		final UnitModel testUnit = modelService.create(UnitModel.class);
		testUnit.setCode("kg");
		testUnit.setUnitType("test");
		modelService.save(testUnit);

		final SubscriptionPricePlanModel priceRow = modelService.create(SubscriptionPricePlanModel.class);
		priceRow.setUnit(testUnit);
		priceRow.setCurrency(commonI18NService.getBaseCurrency());
		priceRow.setSubscriptionTerms(subscriptionTermSet);
		priceRow.setNet(Boolean.TRUE);
		priceRow.setPrice(Double.valueOf(2.3));
		return priceRow;
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import de.hybris.platform.b2ctelcofacades.constants.B2ctelcofacadesConstants;
import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaBillingAccountFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaUsageType;
import de.hybris.platform.b2ctelcoservices.model.TmaAverageServiceUsageModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.b2ctelcoservices.unitconversion.strategies.TmaUnitConversionStrategy;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscribedProductFacade}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscribedProductFacade implements TmaSubscribedProductFacade
{
	private static final String BPO_CONFIGURE_PATH = "/bpo/configure/";
	private static final Logger LOG = Logger.getLogger(DefaultTmaSubscribedProductFacade.class);

	private CategoryService categoryService;
	private ModelService modelService;
	private UserService userService;
	private ProductFacade productFacade;
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;
	private TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	private TmaBillingAccountFacade tmaBillingAccountFacade;
	private TmaSubscribedProductService tmaSubscribedProductService;
	private Converter<TmaSubscribedProductModel, TmaSubscribedProductData> tmaSubscribedProductConverter;
	private Converter<TmaSubscribedProductData, TmaSubscribedProductModel> tmaSubscribedProductReverseConverter;
	private Converter<TmaAverageServiceUsageModel, TmaAverageServiceUsageData> tmaAverageServiceUsageConverter;
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<TmaSubscribedProductData, List<SubscriptionBillingData>> subscriptionBillingDataListConverter;
	private Converter<SubscriptionTermModel, SubscriptionTermData> subscriptionTermConverter;
	private Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter;
	private TmaPoService tmaPoService;
	private TmaUnitConversionStrategy tmaUnitConversionStrategy;
	private CatalogVersionService catalogVersionService;
	private String defaultSubscriptionProductsCategories;
	private TmaCustomerInventoryService customerInventoryService;

	public DefaultTmaSubscribedProductFacade(final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	@Override
	public TmaSubscribedProductData createSubscribedProduct(final TmaSubscribedProductData subscribedProductSource)
	{
		final TmaSubscribedProductModel createdSubscribedProduct = createSubscribedProductFromSource(subscribedProductSource);
		return getTmaSubscribedProductConverter().convert(createdSubscribedProduct);
	}

	@Override
	public TmaSubscribedProductData updateSubscribedProduct(final String billingSystemId, final String billingSubscriptionId,
			final TmaSubscribedProductData subscribedProductData)
	{
		final TmaSubscribedProductModel subscribedProductToBeUpdated = getTmaSubscribedProductService()
				.findSubscribedProduct(billingSystemId, billingSubscriptionId);
		subscribedProductData.setBillingSubscriptionId(billingSubscriptionId);
		subscribedProductData.setBillingsystemId(billingSystemId);
		subscribedProductData.setId(subscribedProductToBeUpdated.getId());
		getTmaSubscribedProductReverseConverter().convert(subscribedProductData, subscribedProductToBeUpdated);
		getModelService().save(subscribedProductToBeUpdated);
		return getTmaSubscribedProductConverter().convert(subscribedProductToBeUpdated);
	}

	@Override
	@Nonnull
	public TmaSubscribedProductData replacePaymentMethod(final String subscriptionId, final String paymentMethodId)
	{

		final TmaSubscribedProductModel subscribedProductToBeUpdated = getTmaSubscribedProductService()
				.findSubscribedProductById(subscriptionId);
		subscribedProductToBeUpdated.setPaymentMethodId(paymentMethodId);
		getModelService().save(subscribedProductToBeUpdated);
		return getTmaSubscribedProductConverter().convert(subscribedProductToBeUpdated);
	}

	@Override
	public void deleteSubscribedProduct(final String billingSystemId, final String billingSubscriptionId)
	{
		getTmaSubscribedProductService().deleteSubscribedProduct(billingSystemId, billingSubscriptionId);
	}

	@Override
	@Nonnull
	public Map<TmaSubscriptionAccessData, Set<TmaSubscribedProductData>> getSubscriptions()
			throws SubscriptionFacadeException //NOSONAR
	{
		final Comparator<TmaSubscriptionAccessData> c = (p, o) -> p.getSubscriberIdentity().compareTo(o.getSubscriberIdentity());
		final Map<TmaSubscriptionAccessData, Set<TmaSubscribedProductData>> subscriptionDataMap = new TreeMap<>(c);
		final List<TmaSubscriptionAccessData> tmaSubscriptionAccessDatas = getSubscriptionAccessForUser();

		tmaSubscriptionAccessDatas
				.forEach(tmaSubscriptionAccessData -> subscriptionDataMap.put(tmaSubscriptionAccessData, getSubscribedProductDataSet(
						tmaSubscriptionAccessData.getSubscriberIdentity(), tmaSubscriptionAccessData.getBillingSystemId())));

		LOG.debug("subscriptionData found for user : " + subscriptionDataMap.size());
		return subscriptionDataMap;
	}

	@Override
	@Nonnull
	public TmaSubscribedProductData getSubscriptionsById(final String subscriptionId)
	{
		final TmaSubscribedProductModel tmaSubscribedProductModel = getTmaSubscribedProductService()
				.findSubscribedProductById(subscriptionId);
		return getTmaSubscribedProductConverter().convert(tmaSubscribedProductModel);
	}

	@Nonnull
	@Override
	public List<TmaSubscribedProductData> getSubscriptionsForPaymentMethod(final String paymentMethodId)
	{
		final List<TmaSubscribedProductData> tmaSubscribedProductDataList = new ArrayList<>();
		final List<TmaSubscribedProductModel> tmaSubscribedProductModelList = getTmaSubscribedProductService()
				.findSubscriptionsForPaymentMethod(paymentMethodId);
		tmaSubscribedProductModelList.forEach(tmaSubscribedProductModel -> tmaSubscribedProductDataList
				.add(getTmaSubscribedProductConverter().convert(tmaSubscribedProductModel)));
		return tmaSubscribedProductDataList;
	}

	@Nonnull
	@Override
	public List<ProductData> getUpsellingOptionsForSubscription(@Nonnull final String productCode)
	{
		final List<ProductOption> productOptions = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

		return getProductFacade().getProductReferencesForCode(productCode,
				Collections.singletonList(ProductReferenceTypeEnum.UPSELLING), productOptions, 100).stream()
				.map(ProductReferenceData::getTarget).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public Set<TmaSubscribedProductModel> getSubscriptionBaseServices(@Nonnull final String subscriberIdentity,
			@Nonnull final String billingSystemId) throws SubscriptionFacadeException
	{
		final TmaSubscriptionBaseModel tmaSubscriptionBaseModel = getTmaSubscriptionBaseService()
				.getSubscriptionBase(subscriberIdentity, billingSystemId);
		if (tmaSubscriptionBaseModel instanceof TmaSubscriptionBaseModel)
		{
			return tmaSubscriptionBaseModel.getSubscribedProducts();
		}
		return Collections.emptySet();
	}

	@Override
	public List<TmaSubscribedProductData> getSubscribedProducts(final String subscriberIdentity, final String billingSystemId)
	{
		List<TmaSubscribedProductData> resultSubscribedProducts = new ArrayList<>();
		final TmaSubscriptionBaseModel subscriptionBase = getTmaSubscriptionBaseService().getSubscriptionBase(subscriberIdentity,
				billingSystemId);
		if (null == subscriptionBase)
		{
			return resultSubscribedProducts;
		}
		final Set<TmaSubscribedProductModel> subscribedProductModelList = subscriptionBase.getSubscribedProducts();
		resultSubscribedProducts = getTmaSubscribedProductConverter().convertAll(subscribedProductModelList);
		return resultSubscribedProducts;
	}

	@Nonnull
	@Override
	public Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getSubscriptionBaseServicesWithAvgValues(
			@Nonnull final String subscriberIdentity, @Nonnull final String billingSystemId) throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = getSubscriptionBaseServices(subscriberIdentity,
				billingSystemId);
		return getAverageServiceUsageValueWithSubscribedProduct(tmaSubscribedProducts);
	}

	@Nonnull
	@Override
	public Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getSubscriptionBaseProductWithAvgValues(
			@Nonnull final String subscriberIdentity, final String subscribedProductId) throws SubscriptionFacadeException
	{
		if (StringUtils.isNotBlank(subscribedProductId))
		{

			final TmaSubscribedProductModel tmaSubscribedProductModel = getTmaSubscribedProductService()
					.findSubscribedProductById(subscribedProductId);
			if (!tmaSubscribedProductModel.getSubscriptionBase().getSubscriberIdentity().equals(subscriberIdentity))
			{
				throw new UnknownIdentifierException("subscribedProductId with code '" + subscribedProductId
						+ "' not found with subscriberIdentity " + subscriberIdentity);
			}
			return (getAverageServiceUsageValueWithSubscribedProductId(tmaSubscribedProductModel));
		}
		else
		{
			return (getSubscriptionBaseServicesByIdentityWithAvgValues(subscriberIdentity));
		}
	}

	@Override
	@Nonnull
	public Set<ProductData> getSubscriptionCompatibleAddons(@Nonnull final String subscriberIdentity,
			@Nonnull final String billingSystemId) throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = new HashSet<>(
				getSubscriptionBaseServices(subscriberIdentity, billingSystemId));
		return findCompatibleAddons(tmaSubscribedProducts);
	}

	@Override
	@Nonnull
	public Set<ProductData> getCompatibleAddonsForSubscriberIdentity(@Nonnull final String subscriberIdentity)
			throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = new HashSet<>(
				getSubscriptionBaseServicesByIdentity(subscriberIdentity));
		return findCompatibleAddons(tmaSubscribedProducts);
	}

	@Override
	@Nonnull
	public String getSubscriptionAccessByPrincipalAndSubscriptionBase(@Nonnull final String billingSystemId,
			@Nonnull final String subscriberIdentity)
	{
		final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
		final TmaSubscriptionAccessModel tmaSubscriptionAccessModel = getTmaSubscriptionAccessFacade()
				.getSubscriptionAccessByPrincipalAndSubscriptionBase(customerModel.getUid(), billingSystemId, subscriberIdentity);
		return tmaSubscriptionAccessModel.getAccessType().getCode();
	}


	@Override
	public ProductData upsellSubscriptionProductData(@Nonnull final String subscriberIdentity,
			@Nonnull final String billingSystemId)
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = getTmaSubscriptionBaseService()
				.getSubscriptionBase(subscriberIdentity, billingSystemId).getSubscribedProducts();
		return getUpsellSubscribedProduct(tmaSubscribedProducts);
	}

	@Override
	public ProductData getUpsellSubscribedProductForIdentity(@Nonnull final String subscriberIdentity)
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = new HashSet<>(
				getSubscriptionBaseServicesByIdentity(subscriberIdentity));
		return getUpsellSubscribedProduct(tmaSubscribedProducts);
	}

	@Override
	public ProductData getSubscriptionTermAndPrice(@Nonnull final ProductData productData)
	{
		final TmaProductOfferingModel tmaProductOfferingModel = tmaPoService.getPoForCode(productData.getCode());
		for (final PriceRowModel priceRowModel : tmaProductOfferingModel.getEurope1Prices())
		{
			if (priceRowModel instanceof SubscriptionPricePlanModel
					&& CollectionUtils.isNotEmpty(((SubscriptionPricePlanModel) priceRowModel).getSubscriptionTerms()))
			{
				productData.setPrice(getTmaSubscriptionPricePlanConverter().convert((SubscriptionPricePlanModel) priceRowModel));
				final Optional<SubscriptionTermModel> subscriptionTermModel = ((SubscriptionPricePlanModel) priceRowModel)
						.getSubscriptionTerms().stream().findFirst();
				if (subscriptionTermModel.isPresent())
				{
					productData.setSubscriptionTerm(getSubscriptionTermConverter().convert(subscriptionTermModel.get()));
				}
				return productData;
			}
		}
		return productData;
	}

	@Override
	@Nonnull
	public List<SubscriptionBillingData> createBillingActivityData(@Nonnull final String subscriptionId)
			throws SubscriptionFacadeException
	{
		final TmaSubscribedProductData subscribedProductData = getSubscriptionsById(subscriptionId);
		return getSubscriptionBillingDataListConverter().convert(subscribedProductData);
	}

	@Override
	@Nonnull
	public Date getSubscriptionServiceEndDate(final String frequencyCode, final Integer duration, final Date startDate)
	{
		return getTmaSubscribedProductService().getSubscriptionServiceEndDate(frequencyCode, duration, startDate);
	}

	@Override
	@Nonnull
	public Set<ProductData> getServiceUsageUpSellProducts(@Nonnull final String subscriberIdentity,
			@Nonnull final String billingSystemId) throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProductModels = new HashSet<>(
				getSubscriptionBaseServices(subscriberIdentity, billingSystemId));
		final Set<TmaProductOfferingModel> upSellProductList = new HashSet<>();
		final Set<String> subscribedProductCodeList = new HashSet<>();
		tmaSubscribedProductModels.forEach(tmaSubscribedProductModel ->
		{
			final TmaProductOfferingModel subscribedProduct = getTmaPoService()
					.getPoForCode(tmaSubscribedProductModel.getProductCode());
			if (!StringUtils.isBlank(tmaSubscribedProductModel.getBundledProductCode()))
			{
				final Set<TmaAverageServiceUsageModel> tmaAverageServiceUsageModels = getTmaAverageServiceCurrentUsage(
						tmaSubscribedProductModel);
				if (CollectionUtils.isNotEmpty(tmaAverageServiceUsageModels))
				{
					final TmaBundledProductOfferingModel parentProduct = getTmaPoService()
							.getBpoForCode(tmaSubscribedProductModel.getBundledProductCode());
					final Set<TmaProductOfferingModel> childProducts = getSubscriptionChildProducts(parentProduct);
					tmaAverageServiceUsageModels.forEach(tmaAverageServiceUsageModel -> upSellProductList
							.addAll(getNextBestUpSellProducts(tmaAverageServiceUsageModel, subscribedProduct, childProducts)));
				}
			}
			subscribedProductCodeList.add(subscribedProduct.getCode());
		});
		return convertToUpSellProductData(upSellProductList, subscribedProductCodeList);
	}

	@Override
	@Nonnull
	public Set<ProductData> getServiceUsageUpSellProductsWithSubscriberIdentity(@Nonnull final String subscriberIdentity)
			throws SubscriptionFacadeException
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProductModels = new HashSet<>(
				getSubscriptionBaseServicesByIdentity(subscriberIdentity));
		final Set<TmaProductOfferingModel> upSellProductList = new HashSet<>();
		final Set<String> subscribedProductCodeList = new HashSet<>();
		tmaSubscribedProductModels.forEach(tmaSubscribedProductModel ->
		{
			final TmaProductOfferingModel subscribedProduct = getTmaPoService()
					.getPoForCode(tmaSubscribedProductModel.getProductCode());
			if (StringUtils.isNotBlank(tmaSubscribedProductModel.getBundledProductCode()))
			{
				final Set<TmaAverageServiceUsageModel> tmaAverageServiceUsageModels = getTmaAverageServiceCurrentUsage(
						tmaSubscribedProductModel);
				if (CollectionUtils.isNotEmpty(tmaAverageServiceUsageModels))
				{
					final TmaBundledProductOfferingModel parentProduct = getTmaPoService()
							.getBpoForCode(tmaSubscribedProductModel.getBundledProductCode());
					final Set<TmaProductOfferingModel> childProducts = getSubscriptionChildProducts(parentProduct);
					tmaAverageServiceUsageModels
							.forEach(tmaAverageServiceUsageModel -> upSellProductList.addAll(getNextBestServicePlanProducts(
									tmaAverageServiceUsageModel, subscribedProduct, retrieveServicePlanChildProducts(childProducts))));
				}
			}
			subscribedProductCodeList.add(subscribedProduct.getCode());
		});
		return convertToUpSellProductData(upSellProductList, subscribedProductCodeList);
	}

	@Override
	@Nonnull
	public boolean isServicePlan(final ProductModel product)
	{
		final List<CategoryModel> categoryList = getCategoryService().getCategoryPathForProduct(product);
		return categoryList.stream()
				.anyMatch(category -> category.getCode().equalsIgnoreCase(B2ctelcofacadesConstants.PLANS_CATEGORY_CODE));
	}

	@Override
	@Nonnull
	public boolean isAddonProduct(final ProductModel product)
	{
		final List<CategoryModel> categoryList = getCategoryService().getCategoryPathForProduct(product);
		return categoryList.stream()
				.anyMatch(category -> category.getCode().equalsIgnoreCase(B2ctelcofacadesConstants.ADDONS_CATEGORY_CODE));
	}

	@Override
	@Nonnull
	public boolean isSubscriptionProduct(final ProductModel product)
	{
		return product.getEurope1Prices().stream().anyMatch(priceRowModel -> priceRowModel instanceof SubscriptionPricePlanModel
				&& !CollectionUtils.isEmpty(((SubscriptionPricePlanModel) priceRowModel).getSubscriptionTerms()));
	}

	protected Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getAverageServiceUsageValueWithSubscribedProduct(
			final Set<TmaSubscribedProductModel> subscribedProducts)
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> tmaSubscriptionBaseServiceUsageMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(subscribedProducts))
		{
			for (final TmaSubscribedProductModel tmaSubscribedProductModel : subscribedProducts)
			{
				final TmaSubscribedProductData subscribedProductData = getSubscribedProductData(tmaSubscribedProductModel);
				final Set<TmaAverageServiceUsageData> averageServiceUsageDatas = getTmaAverageServiceUsageData(
						tmaSubscribedProductModel);
				tmaSubscriptionBaseServiceUsageMap.put(subscribedProductData, averageServiceUsageDatas);
			}
		}
		return tmaSubscriptionBaseServiceUsageMap;
	}

	protected Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getAverageServiceUsageValueWithSubscribedProductId(
			@Nonnull final TmaSubscribedProductModel tmaSubscribedProductModel)
	{
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> tmaSubscriptionBaseServiceUsageMapWithProduct = new HashMap<>();
		final TmaSubscribedProductData tmaSubscribedProductData = getSubscribedProductData(tmaSubscribedProductModel);
		final Set<TmaAverageServiceUsageData> tmaAverageServiceUsageDatas = getTmaAverageServiceUsageData(
				tmaSubscribedProductModel);
		tmaSubscriptionBaseServiceUsageMapWithProduct.put(tmaSubscribedProductData, tmaAverageServiceUsageDatas);
		return tmaSubscriptionBaseServiceUsageMapWithProduct;
	}

	protected TmaSubscribedProductData getSubscribedProductData(final TmaSubscribedProductModel tmaSubscribedProductModel)
	{
		return (getTmaSubscribedProductConverter().convert(tmaSubscribedProductModel));
	}

	protected Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> getSubscriptionBaseServicesByIdentityWithAvgValues(
			@Nonnull final String subscriberIdentity)
	{
		final Set<TmaSubscribedProductModel> tmaSubscribedProducts = getSubscriptionBaseServicesByIdentity(subscriberIdentity);
		return getAverageServiceUsageValueWithSubscribedProduct(tmaSubscribedProducts);
	}

	protected Set<TmaSubscribedProductModel> getSubscriptionBaseServicesByIdentity(@Nonnull final String subscriberIdentity)
	{
		final TmaSubscriptionBaseModel tmaSubscriptionBaseModel = getTmaSubscriptionBaseService()
				.getSubscriptionBaseByIdentity(subscriberIdentity);
		if (tmaSubscriptionBaseModel instanceof TmaSubscriptionBaseModel)
		{
			return tmaSubscriptionBaseModel.getSubscribedProducts();
		}
		return Collections.emptySet();
	}

	protected Set<TmaAverageServiceUsageData> getTmaAverageServiceUsageData(
			final TmaSubscribedProductModel tmaSubscribedProductModel)
	{
		final Set<TmaAverageServiceUsageData> tmaAverageServiceUsageDatas = new HashSet<>();
		final Set<TmaAverageServiceUsageModel> tmaAverageServiceUsageModels = getTmaAverageServiceCurrentUsage(
				tmaSubscribedProductModel);
		if (CollectionUtils.isNotEmpty(tmaAverageServiceUsageModels))
		{
			for (final TmaAverageServiceUsageModel tmaAverageServiceUsageModel : tmaAverageServiceUsageModels)
			{
				tmaAverageServiceUsageDatas.add(getTmaAverageServiceUsageConverter().convert(tmaAverageServiceUsageModel));
			}
		}
		return tmaAverageServiceUsageDatas;
	}

	protected Set<TmaProductOfferingModel> retrieveServicePlanChildProducts(final Set<TmaProductOfferingModel> childProducts)
	{
		final Set<TmaProductOfferingModel> childProductDatas = new HashSet<>();
		childProducts.forEach(childProduct ->
		{
			final boolean isServicePlan = isServicePlan(childProduct);
			if (isServicePlan)
			{
				childProductDatas.add(childProduct);
			}
		});
		return childProductDatas;
	}

	protected ProductData getUpsellSubscribedProduct(final Set<TmaSubscribedProductModel> tmaSubscribedProducts)
	{
		final ProductData productData = null;
		if (CollectionUtils.isNotEmpty(tmaSubscribedProducts))
		{
			return getUpsellSubscribedProductData(tmaSubscribedProducts, productData);
		}
		return productData;
	}

	protected ProductData getUpsellSubscribedProductData(final Set<TmaSubscribedProductModel> tmaSubscribedProducts,
			ProductData productData)
	{
		for (final TmaSubscribedProductModel tmaSubscribedProductModel : tmaSubscribedProducts)
		{
			if (!StringUtils.isBlank(tmaSubscribedProductModel.getBundledProductCode()))
			{
				final TmaBundledProductOfferingModel parentProduct = getTmaPoService()
						.getBpoForCode(tmaSubscribedProductModel.getBundledProductCode());
				final TmaProductOfferingModel firstGrandParentBpo = getCompatibleBpoUpsell(parentProduct);
				if (firstGrandParentBpo != null)
				{
					productData = getProductData(firstGrandParentBpo);
					productData.setUrl(BPO_CONFIGURE_PATH + productData.getCode());
					return productData;
				}
			}
		}
		return productData;
	}

	protected Set<ProductData> findCompatibleAddons(final Set<TmaSubscribedProductModel> tmaSubscribedProducts)
	{
		final Set<String> tmaServicesProductCodeList = new HashSet<>();
		if (CollectionUtils.isEmpty(tmaSubscribedProducts))
		{
			return Collections.emptySet();
		}
		final Set<TmaProductOfferingModel> childProductsList = new HashSet<>();
		tmaSubscribedProducts.forEach(tmaSubscribedProductModel ->
		{
			if (!StringUtils.isBlank(tmaSubscribedProductModel.getBundledProductCode()))
			{
				final TmaBundledProductOfferingModel parentProduct = getTmaPoService()
						.getBpoForCode(tmaSubscribedProductModel.getBundledProductCode());
				childProductsList.addAll(getSubscriptionChildProducts(parentProduct));
			}
			tmaServicesProductCodeList.add(tmaSubscribedProductModel.getProductCode());
		});
		return getAddonList(childProductsList, tmaServicesProductCodeList);
	}

	private Set<TmaProductOfferingModel> getSubscriptionChildProducts(final TmaProductOfferingModel product)
	{
		if (product instanceof TmaBundledProductOfferingModel)
		{
			return ((TmaBundledProductOfferingModel) product).getChildren();
		}
		return Collections.emptySet();
	}

	private Set<ProductData> getAddonList(final Set<TmaProductOfferingModel> childProductsList,
			final Set<String> tmaServicesProductCodeList)
	{
		final Set<ProductData> compatibleAddonDataList = new HashSet<>();
		childProductsList.forEach(childProduct ->
		{
			final boolean isAddon = isAddonProduct(childProduct);
			if (!tmaServicesProductCodeList.contains(childProduct.getCode()) && isAddon)
			{
				final ProductData compatibleAddonData = getProductConverter().convert(childProduct);
				compatibleAddonDataList.add(compatibleAddonData);
			}
		});
		return compatibleAddonDataList;
	}

	private TmaBundledProductOfferingModel getCompatibleBpoUpsell(final TmaProductOfferingModel parentProductOffering)
	{
		if ((null != parentProductOffering) && (null != parentProductOffering.getParents()))
		{
			return parentProductOffering.getParents().stream().filter(grandParentBpo -> grandParentBpo.getChildren().size() > 1)
					.findFirst().orElse(null);
		}
		return null;
	}

	private ProductData getProductData(final TmaProductOfferingModel firstGrandParentBpo)
	{
		final List<ProductOption> extraOptions = Arrays.asList(ProductOption.BASIC, ProductOption.DESCRIPTION);
		return getProductFacade().getProductForCodeAndOptions(firstGrandParentBpo.getCode(), extraOptions);
	}

	private TmaSubscribedProductModel createSubscribedProductFromSource(final TmaSubscribedProductData subscribedProductSource)
	{
		final TmaSubscribedProductModel subscribedProduct = getModelService().create(TmaSubscribedProductModel.class);
		getTmaSubscribedProductReverseConverter().convert(subscribedProductSource, subscribedProduct);
		return getTmaSubscribedProductService().createSubscribedProduct(subscribedProduct);
	}

	private Set<TmaSubscribedProductData> getSubscribedProductDataSet(final String subscriberIdentity,
			final String billingSystemId)
	{
		final Set<TmaSubscribedProductModel> subscribedProductModels = getTmaSubscriptionBaseService()
				.getSubscriptionBase(subscriberIdentity, billingSystemId).getSubscribedProducts();
		return convertTmaSubscribedProductModelsToTmaSubscribedProductDatas(subscribedProductModels);
	}

	private Set<TmaSubscribedProductData> convertTmaSubscribedProductModelsToTmaSubscribedProductDatas(
			final Set<TmaSubscribedProductModel> subscribedProductModels)
	{
		final Set<TmaSubscribedProductData> subscribedProductDatas = new HashSet<>();
		subscribedProductModels.forEach(tmaSubscribedProductModel -> subscribedProductDatas
				.add(getTmaSubscribedProductConverter().convert(tmaSubscribedProductModel)));
		return subscribedProductDatas;
	}

	private List<TmaSubscriptionAccessData> getSubscriptionAccessForUser() throws SubscriptionFacadeException
	{
		final CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
		return tmaSubscriptionAccessFacade.findSubscriptionAccessesByPrincipal(customerModel.getUid());
	}

	private Set<TmaAverageServiceUsageModel> getTmaAverageServiceCurrentUsage(
			final TmaSubscribedProductModel tmaSubscribedProductModel)
	{
		final Set<TmaAverageServiceUsageModel> tmaAverageServiceUsageModels = tmaSubscribedProductModel.getAverageServiceUsages();
		final Set<TmaAverageServiceUsageModel> tmaAverageServiceCurrentUsage = new HashSet<>();
		if (CollectionUtils.isNotEmpty(tmaAverageServiceUsageModels))
		{
			for (final TmaAverageServiceUsageModel tmaAverageServiceUsageModel : tmaAverageServiceUsageModels)
			{
				if (null != tmaAverageServiceUsageModel.getUsageType()
						&& (TmaUsageType.CURRENT.getCode()).equalsIgnoreCase(tmaAverageServiceUsageModel.getUsageType().getCode()))
				{
					tmaAverageServiceCurrentUsage.add(tmaAverageServiceUsageModel);
				}
			}
		}
		return tmaAverageServiceCurrentUsage;
	}

	private Set<TmaProductOfferingModel> getNextBestUpSellProducts(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel,
			final TmaProductOfferingModel subscribedProduct, final Set<TmaProductOfferingModel> childProducts)
	{
		Set<TmaProductOfferingModel> upSellProductData = new HashSet<>();
		try
		{
			final String poPscvValue = tmaAverageServiceUsageModel.getProductSpecCharValue().getValue();
			if (!B2ctelcofacadesConstants.UNLIMITED.equalsIgnoreCase(poPscvValue))
			{
				final BigDecimal pscvValue = BigDecimal.valueOf(Double.parseDouble(poPscvValue));
				final BigDecimal avgSerUsageValue = getTmaUnitConversionStrategy().getUnitConversion(
						tmaAverageServiceUsageModel.getUnitOfMeasure(),
						tmaAverageServiceUsageModel.getProductSpecCharValue().getUnitOfMeasure(),
						BigDecimal.valueOf(Double.parseDouble(tmaAverageServiceUsageModel.getValue())));
				final BigDecimal percentageUsage = avgSerUsageValue.divide(pscvValue, 2, RoundingMode.HALF_UP)
						.multiply(BigDecimal.valueOf(100));
				final BigDecimal threshold = BigDecimal
						.valueOf(Double.parseDouble(Config.getParameter(B2ctelcofacadesConstants.PERCENTAGE_USAGE_THRESHOLD)));
				if (percentageUsage.compareTo(threshold) > 0)
				{
					upSellProductData = getServicePlan(tmaAverageServiceUsageModel, subscribedProduct, childProducts, poPscvValue);
				}
			}
		}
		catch (final NumberFormatException e)
		{
			LOG.error(e.getMessage());
		}
		return upSellProductData;
	}

	protected Set<TmaProductOfferingModel> getNextBestServicePlanProducts(
			final TmaAverageServiceUsageModel tmaAverageServiceUsageModel, final TmaProductOfferingModel subscribedProduct,
			final Set<TmaProductOfferingModel> childProducts)
	{
		final String poPscvValue = tmaAverageServiceUsageModel.getProductSpecCharValue().getValue();
		if (!StringUtils.isNumeric(poPscvValue))
		{
			throw new NumberFormatException(
					TmaAverageServiceUsageModel._TYPECODE + " with numeric " + poPscvValue + " productSpecCharValue not found");
		}
		return getServicePlan(tmaAverageServiceUsageModel, subscribedProduct, childProducts, poPscvValue);
	}

	protected Set<TmaProductOfferingModel> getServicePlan(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel,
			final TmaProductOfferingModel subscribedProduct, final Set<TmaProductOfferingModel> childProducts,
			final String poPscvValue)
	{
		final BigDecimal pscvValue = BigDecimal.valueOf(Double.parseDouble(poPscvValue));
		final TmaProductSpecCharacteristicModel serviceUsagePsc = tmaAverageServiceUsageModel.getProductSpecCharValue()
				.getProductSpecCharacteristic();
		final UsageUnitModel pscvUnit = tmaAverageServiceUsageModel.getProductSpecCharValue().getUnitOfMeasure();
		return childProducts.stream()
				.filter(childProduct -> !subscribedProduct.getCode().equalsIgnoreCase(childProduct.getCode()) && isValidUpSellProduct(
						childProduct.getProductSpecCharValueUses().stream()
						.flatMap(pscvu -> pscvu.getProductSpecCharacteristicValues().stream()).collect(Collectors.toSet()), serviceUsagePsc, pscvValue, pscvUnit))
				.collect(Collectors.toSet());
	}

	private boolean isValidUpSellProduct(final Set<TmaProductSpecCharacteristicValueModel> childPSCVs,
			final TmaProductSpecCharacteristicModel serviceUsagePsc, final BigDecimal poPscvValue, final UsageUnitModel poPscvUnit)
	{
		return childPSCVs.stream()
				.anyMatch(childPSCV -> (childPSCV.getProductSpecCharacteristic().getId().equalsIgnoreCase(serviceUsagePsc.getId())
						&& isValidUpSellPSCV(childPSCV, poPscvValue, poPscvUnit)));
	}

	private Set<ProductData> convertToUpSellProductData(final Set<TmaProductOfferingModel> upSellProductList,
			final Set<String> subscribedProductCodeList)
	{
		final Set<ProductData> upSellProductDataList = new HashSet<>();
		upSellProductList.stream().filter(upSellProduct -> !subscribedProductCodeList.contains(upSellProduct.getCode()))
				.forEach(upSellProduct -> upSellProductDataList.add(getProductConverter().convert(upSellProduct)));
		return upSellProductDataList;
	}

	private boolean isValidUpSellPSCV(final TmaProductSpecCharacteristicValueModel childPSCV, final BigDecimal poPscvValue,
			final UsageUnitModel poPscvUnit)
	{
		try
		{
			if (B2ctelcofacadesConstants.UNLIMITED.equalsIgnoreCase(childPSCV.getValue()))
			{
				return true;
			}
			else
			{
				final BigDecimal childPscvValue = getTmaUnitConversionStrategy().getUnitConversion(childPSCV.getUnitOfMeasure(),
						poPscvUnit, BigDecimal.valueOf(Double.parseDouble(childPSCV.getValue())));
				if (childPscvValue.compareTo(poPscvValue) > 0)
				{
					return true;
				}
			}
		}
		catch (final NumberFormatException e)
		{
			LOG.error(e.getMessage());
		}
		return false;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected Converter<TmaSubscribedProductModel, TmaSubscribedProductData> getTmaSubscribedProductConverter()
	{
		return tmaSubscribedProductConverter;
	}

	@Required
	public void setTmaSubscribedProductConverter(
			final Converter<TmaSubscribedProductModel, TmaSubscribedProductData> tmaSubscribedProductConverter)
	{
		this.tmaSubscribedProductConverter = tmaSubscribedProductConverter;
	}

	protected Converter<TmaSubscribedProductData, TmaSubscribedProductModel> getTmaSubscribedProductReverseConverter()
	{
		return tmaSubscribedProductReverseConverter;
	}

	@Required
	public void setTmaSubscribedProductReverseConverter(
			final Converter<TmaSubscribedProductData, TmaSubscribedProductModel> tmaSubscribedProductReverseConverter)
	{
		this.tmaSubscribedProductReverseConverter = tmaSubscribedProductReverseConverter;
	}

	protected TmaSubscribedProductService getTmaSubscribedProductService()
	{
		return tmaSubscribedProductService;
	}

	@Required
	public void setTmaSubscribedProductService(final TmaSubscribedProductService tmaSubscribedProductService)
	{
		this.tmaSubscribedProductService = tmaSubscribedProductService;
	}

	protected TmaSubscriptionAccessFacade getTmaSubscriptionAccessFacade()
	{
		return tmaSubscriptionAccessFacade;
	}

	@Required
	public void setTmaSubscriptionAccessFacade(final TmaSubscriptionAccessFacade tmaSubscriptionAccessFacade)
	{
		this.tmaSubscriptionAccessFacade = tmaSubscriptionAccessFacade;
	}

	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
	}

	@Required
	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@Required
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	protected Converter<TmaAverageServiceUsageModel, TmaAverageServiceUsageData> getTmaAverageServiceUsageConverter()
	{
		return tmaAverageServiceUsageConverter;
	}

	@Required
	public void setTmaAverageServiceUsageConverter(
			final Converter<TmaAverageServiceUsageModel, TmaAverageServiceUsageData> tmaAverageServiceUsageConverter)
	{
		this.tmaAverageServiceUsageConverter = tmaAverageServiceUsageConverter;
	}


	protected TmaSubscriptionBaseFacade getTmaSubscriptionBaseFacade()
	{
		return tmaSubscriptionBaseFacade;
	}

	@Required
	public void setTmaSubscriptionBaseFacade(final TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade)
	{
		this.tmaSubscriptionBaseFacade = tmaSubscriptionBaseFacade;
	}

	protected TmaBillingAccountFacade getTmaBillingAccountFacade()
	{
		return tmaBillingAccountFacade;
	}

	@Required
	public void setTmaBillingAccountFacade(final TmaBillingAccountFacade tmaBillingAccountFacade)
	{
		this.tmaBillingAccountFacade = tmaBillingAccountFacade;
	}

	protected Converter<TmaSubscribedProductData, List<SubscriptionBillingData>> getSubscriptionBillingDataListConverter()
	{
		return subscriptionBillingDataListConverter;
	}

	@Required
	public void setSubscriptionBillingDataListConverter(
			final Converter<TmaSubscribedProductData, List<SubscriptionBillingData>> subscriptionBillingDataListConverter)
	{
		this.subscriptionBillingDataListConverter = subscriptionBillingDataListConverter;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected Converter<SubscriptionTermModel, SubscriptionTermData> getSubscriptionTermConverter()
	{
		return subscriptionTermConverter;
	}

	@Required
	public void setSubscriptionTermConverter(
			final Converter<SubscriptionTermModel, SubscriptionTermData> subscriptionTermConverter)
	{
		this.subscriptionTermConverter = subscriptionTermConverter;
	}

	protected Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> getTmaSubscriptionPricePlanConverter()
	{
		return tmaSubscriptionPricePlanConverter;
	}

	@Required
	public void setTmaSubscriptionPricePlanConverter(
			final Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> tmaSubscriptionPricePlanConverter)
	{
		this.tmaSubscriptionPricePlanConverter = tmaSubscriptionPricePlanConverter;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected TmaUnitConversionStrategy getTmaUnitConversionStrategy()
	{
		return tmaUnitConversionStrategy;
	}

	@Required
	public void setTmaUnitConversionStrategy(final TmaUnitConversionStrategy tmaUnitConversionStrategy)
	{
		this.tmaUnitConversionStrategy = tmaUnitConversionStrategy;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected String getDefaultSubscriptionProductsCategories()
	{
		return defaultSubscriptionProductsCategories;
	}

	@Required
	public void setDefaultSubscriptionProductsCategories(final String defaultSubscriptionProductsCategories)
	{
		this.defaultSubscriptionProductsCategories = defaultSubscriptionProductsCategories;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

}

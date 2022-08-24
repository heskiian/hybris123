/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.impl;

import de.hybris.platform.b2ctelcofacades.constants.B2ctelcofacadesConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.MockCreateCpiService;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementItemModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAgreementTermModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductLineModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.assertj.core.util.Lists;
import org.fest.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import static de.hybris.platform.b2ctelcoservices.enums.TmaProcessType.ACQUISITION;
import static de.hybris.platform.b2ctelcoservices.enums.TmaProcessType.RETENTION;
import static de.hybris.platform.b2ctelcoservices.enums.TmaProcessType.TARIFF_CHANGE;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation for {@link MockCreateCpiService}.
 *
 * @since 1810
 */
public class DefaultMockCreateCpiService implements MockCreateCpiService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultMockCreateCpiService.class);

	private TmaSubscribedProductService subscribedProductService;
	private CatalogVersionService catalogVersionService;
	private CategoryService categoryService;
	private TmaCustomerInventoryService customerInventoryService;
	private ModelService modelService;
	private EntryGroupService entryGroupService;

	private String defaultBillingSystemId;
	private String defaultSubscriptionStatus;


	@Override
	public void mockCreateSubscriptionsFromOrder(OrderModel orderModel)
	{
		if (orderModel.getEntries().isEmpty())
		{
			return;
		}
		final CatalogVersionModel catalogVersion = orderModel.getEntries().get(0).getProduct().getCatalogVersion();
		getCatalogVersionService().setSessionCatalogVersion(catalogVersion.getCatalog().getId(),
				catalogVersion.getCatalog().getVersion());

		processOrderGroupEntries(orderModel);
		processOrderStandaloneEntries(orderModel);
	}

	protected void processOrderStandaloneEntries(OrderModel orderModel)
	{
		List<AbstractOrderEntryModel> standaloneEntries = orderModel.getEntries().stream().filter(entry -> CollectionUtils.isEmpty
				(entry.getEntryGroupNumbers())).collect(Collectors.toList());
		for (final AbstractOrderEntryModel orderEntry : standaloneEntries)
		{
			processEntriesByProcessType(Lists.newArrayList(orderEntry), orderEntry.getProcessType());
		}
	}

	protected void processOrderGroupEntries(OrderModel orderModel)
	{
		for (EntryGroup entryGroup : orderModel.getEntryGroups())
		{
			List<AbstractOrderEntryModel> groupEntries = orderModel.getEntries().stream().filter(entry -> entry
					.getEntryGroupNumbers().contains(entryGroup.getGroupNumber())).collect(Collectors.toList());
			//all entries of an entry group have the same process type
			processEntriesByProcessType(groupEntries, groupEntries.get(0).getProcessType());
		}
	}

	protected void processEntriesByProcessType(List<AbstractOrderEntryModel> groupEntries, TmaProcessType processType)
	{
		if (processType.equals(ACQUISITION))
		{
			processAcquisitionEntries(groupEntries);
			return;
		}
		if (processType.equals(RETENTION) || processType.equals(TARIFF_CHANGE))
		{
			LOG.error("Mock operations are not supported for " + processType.getCode());
		}
	}

	/**
	 * Process a list of entries having the same entry group.
	 * Only Acquisition entries are processed.
	 *
	 * @param entries
	 * 		entries having the same entry group
	 */
	protected void processEntryGroupEntries(final List<AbstractOrderEntryModel> entries)
	{
		//all entries of an entry group have the same process type
		final TmaProcessType processType = entries.get(0).getProcessType();
		processEntriesByProcessType(entries, processType);
	}

	/**
	 * Process the list of ACQUISITION order entries. The entries are all from the same entry group or standalone products.
	 * From the entries list are excluded the ones that don't a product specification and the ones without order subscription
	 * details, especially the subscription term. For each entry group, or each standalone product, a new
	 * {@link TmaBillingAgreementModel} is created, then for each different {@link TmaProductSpecTypeModel} a new
	 * {@link TmaSubscriptionBaseModel} is created and attached to the billing agreement. For each entry, a new
	 * {@link TmaSubscribedProductModel} and {@link TmaBillingAgreementItemModel} are created to be finally assigned to the
	 * billing agreement.
	 *
	 * @param entries
	 * 		order entries list
	 */
	protected void processAcquisitionEntries(final List<AbstractOrderEntryModel> entries)
	{
		//group all entries by product specification type and for each product specification type create a new
		// subscription base
		Map<TmaProductSpecTypeModel, List<AbstractOrderEntryModel>> orderEntriesGroupedByProdSpecType = groupEntriesByProductSpecificationType(
				entries);

		if (Maps.isEmpty(orderEntriesGroupedByProdSpecType))
		{
			return;
		}
		//get first entry (all should have the same subscription details)
		final AbstractOrderEntryModel entry = orderEntriesGroupedByProdSpecType.values().iterator().next().get(0);
		//create a new billing agreement for all entries
		TmaBillingAgreementModel billingAgreementModel = createNewTmaBillingAgreementModel(entry);

		for (final TmaProductSpecTypeModel productSpecType : orderEntriesGroupedByProdSpecType.keySet())
		{
			//create subscription base for each different product specification type
			TmaSubscriptionBaseModel subscriptionBaseModel = getCustomerInventoryService().createNewSubscriptionHierarchy
					(entry.getOrder().getUser().getUid(), TmaAccessType.OWNER);

			for (final AbstractOrderEntryModel orderEntry : orderEntriesGroupedByProdSpecType.get(productSpecType))
			{
				generateNewSubscribedProductForSubscriptionBase(orderEntry, subscriptionBaseModel,
						billingAgreementModel);
			}
		}
	}

	private void generateNewSubscribedProductForSubscriptionBase(final AbstractOrderEntryModel orderEntry,
			final TmaSubscriptionBaseModel subscriptionBaseModel,
			final TmaBillingAgreementModel entryGroupBillingArrangement)
	{
		//create new subscribed product
		final TmaSubscribedProductModel subscribedProduct = createNewSubscribedProduct(orderEntry);
		subscribedProduct.setSubscriptionBase(subscriptionBaseModel);
		getModelService().save(subscriptionBaseModel);

		//create new billing agreement item foe each new subscribed product
		TmaBillingAgreementItemModel billingAgreementItemModel = createNewBillingAgreementItem(orderEntry);
		billingAgreementItemModel.setSubscribedProducts(Lists.newArrayList(subscribedProduct));
		getModelService().save(billingAgreementItemModel);

		updateBillingAgreementWithBillingAgreementItem(entryGroupBillingArrangement, billingAgreementItemModel);

		getModelService().save(subscribedProduct);
	}

	private void updateBillingAgreementWithBillingAgreementItem(final TmaBillingAgreementModel billingArrangement,
			final TmaBillingAgreementItemModel billingAgreementItemModel)
	{
		List<TmaBillingAgreementItemModel> items = new ArrayList<>();
		if (!CollectionUtils.isEmpty(billingArrangement.getAgreementItems()))
		{
			items.addAll(billingArrangement.getAgreementItems());
		}
		items.add(billingAgreementItemModel);
		billingArrangement.setAgreementItems(items);
		getModelService().save(billingAgreementItemModel);
		getModelService().save(billingArrangement);
	}

	private TmaBillingAgreementItemModel createNewBillingAgreementItem(final AbstractOrderEntryModel orderEntry)
	{
		TmaBillingAgreementItemModel billingAgreementItemModel = getModelService()
				.create(TmaBillingAgreementItemModel.class);
		billingAgreementItemModel.setId(String.join("_", orderEntry.getProduct().getCode(), orderEntry.getEntryNumber()
				.toString(), orderEntry.getOrder().getCode()));
		//TODO billing agreement item price to be populated  with all details from order entry (one time charge, recurring charge)
		return billingAgreementItemModel;
	}

	private TmaBillingAgreementModel createNewTmaBillingAgreementModel(final AbstractOrderEntryModel orderEntry)
	{
		TmaBillingAgreementTermModel billingAgreementTermModel = getTmaBillingAgreementTermModel(orderEntry);
		TmaBillingAgreementModel billingAgreementModel = getModelService().create(TmaBillingAgreementModel.class);
		billingAgreementModel.setBillingSystemId(getDefaultBillingSystemId());
		billingAgreementModel.setTerm(billingAgreementTermModel);

		String label = orderEntry.getProduct().getCode();
		if (orderEntry.getBpo() != null)
		{
			label = orderEntry.getBpo().getCode().concat("_").concat(orderEntry.getEntryGroupNumbers().iterator().next().toString());
		}

		billingAgreementModel
				.setId(String.join("_", orderEntry.getOrder().getCode(), label, billingAgreementTermModel.getLength().toString(),
						billingAgreementTermModel.getFrequency().getCode()));

		modelService.save(billingAgreementTermModel);
		return billingAgreementModel;
	}


	private Map<TmaProductSpecTypeModel, List<AbstractOrderEntryModel>> groupEntriesByProductSpecificationType(
			final List<AbstractOrderEntryModel> entryList)
	{
		Map<TmaProductSpecTypeModel, List<AbstractOrderEntryModel>> orderEntriesGroupedByProdSpecType = new HashMap<>();
		for (final AbstractOrderEntryModel orderEntry : entryList)
		{
			final TmaProductOfferingModel productOffering = (TmaProductOfferingModel) orderEntry.getProduct();
			final Set<TmaProductSpecTypeModel> productSpecTypes = productOffering.getProductSpecification() != null ?
					productOffering.getProductSpecification().getProductSpecificationTypes() : null;
			final Optional<TmaProductSpecTypeModel> productSpecType = productSpecTypes != null ?
					productSpecTypes.stream().filter(productSpecTypeModel -> productSpecTypeModel instanceof TmaProductLineModel)
							.findFirst() : Optional.empty();
			if (productSpecType.isEmpty())
			{
				continue;
			}
			if (ObjectUtils.isEmpty(orderEntry.getSubscriptionInfo())
					|| ObjectUtils.isEmpty(orderEntry.getSubscriptionInfo().getSubscriptionTerm()))
			{
				LOG.warn("Cannot create subscribed product for " + orderEntry.getProduct().getCode() + ". Subscription details are "
						+ "missing.");
				continue;
			}
			if (orderEntriesGroupedByProdSpecType.containsKey(productSpecType))
			{
				List<AbstractOrderEntryModel> exitingEntries = orderEntriesGroupedByProdSpecType.get(productSpecType);
				exitingEntries.add(orderEntry);
				orderEntriesGroupedByProdSpecType.put(productSpecType.get(), exitingEntries);
			}
			else
			{
				orderEntriesGroupedByProdSpecType.put(productSpecType.get(), Lists.newArrayList(orderEntry));
			}
		}
		return orderEntriesGroupedByProdSpecType;
	}

	private TmaBillingAgreementTermModel getTmaBillingAgreementTermModel(final AbstractOrderEntryModel orderEntry)
	{
		final SubscriptionTermModel subsTerm = orderEntry.getSubscriptionInfo().getSubscriptionTerm();
		final Integer contractDuration = getContractDuration(subsTerm);
		final TermOfServiceFrequency contractFrequency = subsTerm.getTermOfServiceFrequency();

		TmaBillingAgreementTermModel billingAgreementTerm = getModelService().create(TmaBillingAgreementTermModel.class);
		Date startDate = new Date();
		billingAgreementTerm.setBillingSystemId(getDefaultBillingSystemId());
		billingAgreementTerm.setStart(startDate);
		billingAgreementTerm.setEnd(getSubscriptionEndDate(subsTerm, startDate));
		billingAgreementTerm.setFrequency(contractFrequency);
		billingAgreementTerm.setLength(contractDuration);
		billingAgreementTerm
				.setId(String.join("_", orderEntry.getOrder().getCode(), orderEntry.getEntryNumber().toString(), subsTerm.getId(),
						startDate.toString(), billingAgreementTerm.getEnd().toString(), contractDuration.toString(),
						contractFrequency.getCode()));
		return billingAgreementTerm;
	}


	protected TmaSubscribedProductModel createNewSubscribedProduct(final AbstractOrderEntryModel orderEntry)
	{
		final AbstractOrderModel order = orderEntry.getOrder();
		TmaSubscribedProductModel subscribedProduct = getSubscribedProductService().createNewSubscribedProduct(new Date(),
				getDefaultBillingSystemId(), getDefaultSubscriptionStatus());
		subscribedProduct.setOrderNumber(order.getCode());
		subscribedProduct.setCustomerId(order.getUser().getUid());

		if (!ObjectUtils.isEmpty(order.getPaymentInfo()) && order.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			final CreditCardPaymentInfoModel creditCardPaymentInfo = (CreditCardPaymentInfoModel) order.getPaymentInfo();
			subscribedProduct.setPaymentMethodId(creditCardPaymentInfo.getSubscriptionId());
		}
		updateSubscribedProductDetails(orderEntry, subscribedProduct);
		return subscribedProduct;
	}

	/**
	 * Updates Subscription product with details from order entry.
	 */
	private void updateSubscribedProductDetails(final AbstractOrderEntryModel orderEntry, final TmaSubscribedProductModel
			subscribedProduct)
	{
		subscribedProduct.setOrderEntryNumber(orderEntry.getEntryNumber());
		final ProductModel product = orderEntry.getProduct();
		if (product == null)
		{
			return;
		}
		if (!ObjectUtils.isEmpty(orderEntry.getBpo()))
		{
			subscribedProduct.setBundledProductCode(orderEntry.getBpo().getCode());
		}
		subscribedProduct.setName(product.getName());
		subscribedProduct.setProductCode(product.getCode());
		subscribedProduct.setServiceType(getSubscribedProductServiceType(product));
		if (ObjectUtils.isEmpty(orderEntry.getSubscriptionInfo())
				|| ObjectUtils.isEmpty(orderEntry.getSubscriptionInfo().getSubscriptionTerm()))
		{
			return;
		}
		final SubscriptionTermModel subsTerm = orderEntry.getSubscriptionInfo().getSubscriptionTerm();
		final Integer contractDuration = getContractDuration(subsTerm);
		final TermOfServiceFrequency contractFrequency = subsTerm.getTermOfServiceFrequency();
		subscribedProduct.setCancellable(subsTerm.getCancellable());
		subscribedProduct.setContractDuration(contractDuration);
		subscribedProduct.setEndDate(getSubscriptionEndDate(subsTerm, new Date()));
		subscribedProduct.setContractFrequency(contractFrequency == null ? null : contractFrequency.getCode());
		if (!ObjectUtils.isEmpty(subsTerm.getTermOfServiceRenewal()))
		{
			subscribedProduct.setRenewalType("true");
		}
		if (!ObjectUtils.isEmpty(subsTerm.getBillingPlan())
				&& !ObjectUtils.isEmpty(subsTerm.getBillingPlan().getBillingFrequency()))
		{
			subscribedProduct.setBillingFrequency(subsTerm.getBillingPlan().getBillingFrequency().getCode());
		}

	}

	private Date getSubscriptionEndDate(@Nonnull final SubscriptionTermModel subscriptionTerm, @Nonnull final Date startDate)
	{
		final TermOfServiceFrequency frequency = subscriptionTerm.getTermOfServiceFrequency();
		final Integer contractDuration = getContractDuration(subscriptionTerm);
		final String frequencyCode = frequency == null ? null : frequency.getCode();
		return getSubscribedProductService().getSubscriptionServiceEndDate(frequencyCode, contractDuration, startDate);
	}

	private TmaServiceType getSubscribedProductServiceType(final ProductModel product)
	{
		final CatalogVersionModel catalogVersion = product.getCatalogVersion();
		getCatalogVersionService().setSessionCatalogVersion(catalogVersion.getCatalog().getId(),
				catalogVersion.getCatalog().getVersion());
		final boolean isAddon = isAddonProduct(product);
		if (isAddon)
		{
			return TmaServiceType.ADD_ON;
		}
		return TmaServiceType.TARIFF_PLAN;
	}

	private Integer getContractDuration(final SubscriptionTermModel subscriptionTerm)
	{
		Integer contractDuration = subscriptionTerm.getTermOfServiceNumber();
		// if no contract duration is provided as terms of service fallback to 1
		return contractDuration == null ? 1 : contractDuration;
	}

	private boolean isAddonProduct(final ProductModel product)
	{
		final List<CategoryModel> categoryList = getCategoryService().getCategoryPathForProduct(product);
		return categoryList.stream()
				.anyMatch(category -> category.getCode().equalsIgnoreCase(B2ctelcofacadesConstants.ADDONS_CATEGORY_CODE));
	}

	public TmaSubscribedProductService getSubscribedProductService()
	{
		return subscribedProductService;
	}

	public void setSubscribedProductService(
			TmaSubscribedProductService subscribedProductService)
	{
		this.subscribedProductService = subscribedProductService;
	}

	public String getDefaultBillingSystemId()
	{
		return defaultBillingSystemId;
	}

	public void setDefaultBillingSystemId(String defaultBillingSystemId)
	{
		this.defaultBillingSystemId = defaultBillingSystemId;
	}

	public String getDefaultSubscriptionStatus()
	{
		return defaultSubscriptionStatus;
	}

	public void setDefaultSubscriptionStatus(String defaultSubscriptionStatus)
	{
		this.defaultSubscriptionStatus = defaultSubscriptionStatus;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	public void setCustomerInventoryService(
			TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	public void setEntryGroupService(EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}
}

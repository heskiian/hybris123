/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaProcessTypeResourceStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceContextService;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;


/**
 * Overrides {@code DefaultCommerceAddToCartStrategy} to calculate the cart before triggering add to cart and also after
 * merging the entry.
 *
 * @since 6.7
 */
public class TmaCommerceAddToCartStrategy extends DefaultCommerceAddToCartStrategy implements TmaCartStrategy
{
	private EnumerationService enumerationService;
	private TmaPoService tmaPoService;
	private TmaCommercePriceService commercePriceService;
	private TmaSubscriptionTermService subscriptionTermService;
	private TmaCustomerInventoryService customerInventoryService;
	private TmaCommerceCartResourceService tmaCommerceCartResourceService;
	private TmaPriceContextService tmaPriceContextService;
	private TmaAbstractOrderEntryService abstractOrderEntryService;
	private List<CommerceAddToCartMethodHook> masterAddToCartEntryHooks;

	public TmaCommerceAddToCartStrategy(final TmaCommerceCartResourceService tmaCommerceCartResourceService,
			final TmaPriceContextService tmaPriceContextService, final TmaAbstractOrderEntryService abstractOrderEntryService,
			final List<CommerceAddToCartMethodHook> masterAddToCartEntryHooks)
	{
		this.tmaCommerceCartResourceService = tmaCommerceCartResourceService;
		this.tmaPriceContextService = tmaPriceContextService;
		this.abstractOrderEntryService = abstractOrderEntryService;
		this.masterAddToCartEntryHooks = masterAddToCartEntryHooks;
	}

	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModificationList = new ArrayList<>();

		for (CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{
			if (CollectionUtils.isNotEmpty(commerceCartParameter.getChildren()))
			{
				commerceCartModificationList.addAll(addToCartBpoStructure(commerceCartParameter));
				continue;
			}

			final CommerceCartModification commerceCartModification = this.addToCart(commerceCartParameter);
			commerceCartModificationList.add(commerceCartModification);
		}

		return commerceCartModificationList;
	}

	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		beforeAddToCartMasterEntry(parameter);
		final CommerceCartModification modification = doAddToCart(parameter);
		if (modification.getQuantityAdded() > 0 && modification.getEntry() != null)
		{
			getTmaCommerceCartResourceService().updateResources(parameter, modification);
		}
		afterAddToCart(parameter, modification);
		mergeEntry(modification, parameter);
		if (modification.getEntry().getMasterEntry() != null)
		{
			getModelService().refresh(modification.getEntry().getMasterEntry());
		}
		afterAddToCartMasterEntry(parameter, modification);
		getCommerceCartCalculationStrategy().recalculateCart(parameter);
		return modification;
	}

	@Override
	protected CommerceCartModification doAddToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		this.beforeAddToCart(parameter);
		validateAddToCart(parameter);
		return performAddToCart(parameter, true);
	}

	/**
	 * Performs add to cart for the input {@link CommerceCartParameter} and creates the corresponding {@link CommerceCartModification}
	 * for the newly created {@link CartEntryModel}.
	 *
	 * @param parameter
	 * 		contains request details
	 * @param saveCartEntry
	 * 		flag indicating if the new created entry to be saved or not
	 * @return corresponding {@link CommerceCartModification} for the new added entry
	 * @throws CommerceCartModificationException
	 * 		in case an error occurs
	 */
	protected CommerceCartModification performAddToCart(final CommerceCartParameter parameter,
			final boolean saveCartEntry) throws CommerceCartModificationException
	{
		final CartModel cartModel = parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
		checkApplicableSubscriptionTerm(parameter);

		if (!isProductForCode(parameter))
		{
			return createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE, createEmptyCartEntry(parameter), 0);
		}

		PriceRowModel bestApplicablePrice = null;
		if (mustHavePrice(parameter))
		{
			bestApplicablePrice =
					getCommercePriceService().getBestApplicablePrice(getTmaPriceContextService().createPriceContext(parameter));

			if (bestApplicablePrice == null)
			{
				//if there is no price for given params, the product cannot be added to cart
				return createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE, createEmptyCartEntry(parameter), 0);
			}
		}

		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
				deliveryPointOfService);
		final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
		final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
		final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

		if (actualAllowedQuantityChange <= 0)
		{
			// Not allowed to add any quantity, or maybe even asked to reduce the quantity
			// Do nothing!
			final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);
			return createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);
		}
		// We are allowed to add items to the cart
		final CartEntryModel entryModel = addCartEntry(parameter, actualAllowedQuantityChange);

		TmaCartSubscriptionInfoModel subscriptionInfo = parameter.getSubscriptionInfo();
		if (subscriptionInfo == null)
		{
			subscriptionInfo = createSubscriptionInfo(bestApplicablePrice);
		}
		entryModel.setSubscriptionInfo(subscriptionInfo);

		updateCartEntryDetails(parameter, entryModel);

		if (saveCartEntry)
		{
			saveCartEntry(entryModel);
		}
		final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity, quantityToAdd,
				cartLevelAfterQuantityChange);

		return createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
	}

	protected void beforeAddToCartMasterEntry(final CommerceCartParameter commerceCartParameter)
			throws CommerceCartModificationException
	{
		if (getMasterAddToCartHooks() != null && (commerceCartParameter.isEnableHooks() && getConfigurationService()
				.getConfiguration().getBoolean(CommerceServicesConstants.ADDTOCARTHOOK_ENABLED)))
		{
			for (final CommerceAddToCartMethodHook addToCartMethodHook : getMasterAddToCartHooks())
			{
				addToCartMethodHook.beforeAddToCart(commerceCartParameter);
			}
		}
	}

	protected void afterAddToCartMasterEntry(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		if (getMasterAddToCartHooks() != null && (commerceCartParameter.isEnableHooks() && getConfigurationService()
				.getConfiguration().getBoolean(CommerceServicesConstants.ADDTOCARTHOOK_ENABLED)))
		{
			for (final CommerceAddToCartMethodHook addToCartMethodHook : getMasterAddToCartHooks())
			{
				addToCartMethodHook.afterAddToCart(commerceCartParameter, commerceCartModification);
			}
		}
	}

	/**
	 * Performs validations for each parameter from bundled product offering structure to be added to cart.
	 *
	 * @param commerceCartParameter
	 * 		corresponding bundled product offering param
	 * @throws CommerceCartModificationException
	 * 		in case a validation error occurs
	 */
	protected void beforeAddToCartBpo(final CommerceCartParameter commerceCartParameter) throws CommerceCartModificationException
	{
		this.beforeAddToCart(commerceCartParameter);
		validateAddToCart(commerceCartParameter);
		if (CollectionUtils.isEmpty(commerceCartParameter.getChildren()))
		{
			return;
		}
		for (CommerceCartParameter childParam : commerceCartParameter.getChildren())
		{
			beforeAddToCartBpo(childParam);
		}
	}

	protected void buildBpoCommerceCartModificationsMap(final CommerceCartParameter commerceCartParameter,
			final Map<CommerceCartParameter, CommerceCartModification> paramModificationMap) throws CommerceCartModificationException
	{
		final CommerceCartModification modification = performAddToCart(commerceCartParameter, false);
		paramModificationMap.put(commerceCartParameter, modification);

		if (!modification.getStatusCode().equals(CommerceCartModificationStatus.SUCCESS))
		{
			return;
		}

		if (CollectionUtils.isNotEmpty(commerceCartParameter.getChildren()))
		{
			for (CommerceCartParameter childParam : commerceCartParameter.getChildren())
			{
				childParam.setParentEntry(modification.getEntry());
				buildBpoCommerceCartModificationsMap(childParam, paramModificationMap);
			}
		}
	}

	/**
	 * Adds into cart a hierarchical structure of {@link CommerceCartParameter} representing a
	 * {@link TmaBundledProductOfferingModel} by ensuring that all components part of given BPO can be added successfully into
	 * cart.
	 *
	 * @param commerceCartParameter
	 * 		commerceCartParameter representing the {@link TmaBundledProductOfferingModel} to be added into cart.
	 * @return commerceCartModifications representing the results for each BPO component
	 * @throws CommerceCartModificationException
	 * 		in case any validation for the given BPO fails
	 */
	protected List<CommerceCartModification> addToCartBpoStructure(final CommerceCartParameter commerceCartParameter)
			throws CommerceCartModificationException
	{
		final Map<CommerceCartParameter, CommerceCartModification> paramModificationMap = new LinkedHashMap<>();

		beforeAddToCartMasterEntry(commerceCartParameter);
		beforeAddToCartBpo(commerceCartParameter);

		buildBpoCommerceCartModificationsMap(commerceCartParameter, paramModificationMap);

		final Optional<CommerceCartModification> unsuccessfulCartModification = paramModificationMap.values().stream()
				.filter((storeModification -> !storeModification.getStatusCode().equals(CommerceCartModificationStatus.SUCCESS)))
				.findFirst();

		if (unsuccessfulCartModification.isPresent())
		{
			throw new CommerceCartModificationException(
					"Product Offering with code " + commerceCartParameter.getProduct().getCode() + " cannot be added to cart, due "
							+ "to " + unsuccessfulCartModification.get().getEntry().getProduct().getCode() + " "
							+ unsuccessfulCartModification.get().getStatusCode());
		}
		paramModificationMap.values().forEach(storedModification -> {
			saveCartEntry(storedModification.getEntry());
		});

		for (final Map.Entry<CommerceCartParameter, CommerceCartModification> entry : paramModificationMap.entrySet())
		{
			final CommerceCartParameter cartParameter = entry.getKey();
			final CommerceCartModification cartModification = entry.getValue();
			if (cartModification.getQuantityAdded() > 0 && cartModification.getEntry() != null)
			{
				getTmaCommerceCartResourceService().updateResources(cartParameter, cartModification);
			}
			afterAddToCart(commerceCartParameter, cartModification);
			mergeEntry(cartModification, cartParameter);
		}

		afterAddToCartMasterEntry(commerceCartParameter, paramModificationMap.get(commerceCartParameter));
		getCommerceCartCalculationStrategy().recalculateCart(commerceCartParameter);

		return new ArrayList<>(paramModificationMap.values());
	}

	private void saveCartEntry(AbstractOrderEntryModel entryModel)
	{
		getModelService().save(entryModel);
		getModelService().refresh(entryModel);
		if (entryModel.getMasterEntry() != null)
		{
			getModelService().refresh(entryModel.getMasterEntry());
		}
	}

	protected void checkApplicableSubscriptionTerm(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		if (parameter.getProduct() instanceof TmaOperationalProductOfferingModel)
		{
			return;
		}

		if (parameter.getParentEntry() == null)
		{
			return;
		}

		final List<AbstractOrderEntryModel> entries = getEntriesInRelationWith(parameter.getParentEntry().getEntryNumber(),
				parameter.getCart());
		if (CollectionUtils.isEmpty(entries))
		{
			return;
		}

		final TmaCartSubscriptionInfoModel cartSubscriptionInfoModel = entries.stream()
				.filter((AbstractOrderEntryModel cartEntry) -> cartEntry.getSubscriptionInfo() != null
						&& cartEntry.getSubscriptionInfo().getSubscriptionTerm() != null)
				.findFirst().map(AbstractOrderEntryModel::getSubscriptionInfo).orElse(null);

		if (cartSubscriptionInfoModel == null || cartSubscriptionInfoModel.getSubscriptionTerm() == null)
		{
			return;
		}
		if (parameter.getSubscriptionInfo() == null || parameter.getSubscriptionInfo().getSubscriptionTerm() == null)
		{
			final TmaCartSubscriptionInfoModel subscriptionInfoToBeSet = getModelService()
					.create(TmaCartSubscriptionInfoModel.class);
			subscriptionInfoToBeSet.setSubscriptionTerm(cartSubscriptionInfoModel.getSubscriptionTerm());
			getModelService().save(subscriptionInfoToBeSet);
			parameter.setSubscriptionInfo(subscriptionInfoToBeSet);
			return;
		}
		if (!parameter.getSubscriptionInfo().getSubscriptionTerm().equals(cartSubscriptionInfoModel.getSubscriptionTerm()))
		{
			throw new CommerceCartModificationException(
					"Invalid subscription term: " + parameter.getSubscriptionInfo().getSubscriptionTerm().getId() + ",for entry with "
							+ "product code " + parameter.getProduct().getCode());
		}
	}

	private List<AbstractOrderEntryModel> getEntriesInRelationWith(final long entryNumber, final AbstractOrderModel abstractOrder)
	{
		final Optional<AbstractOrderEntryModel> abstractOrderEntryModel = abstractOrder.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getEntryNumber() == entryNumber).findFirst();

		if (abstractOrderEntryModel.isEmpty() || (abstractOrderEntryModel.get().getMasterEntry() == null && CollectionUtils
				.isEmpty(abstractOrderEntryModel.get().getChildEntries())))
		{
			return Collections.emptyList();
		}

		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(abstractOrderEntryModel.get());

		return getAbstractOrderEntryService().getAllChildEntries(rootEntry);
	}

	/**
	 * Updates new created {@link CartEntryModel} with details from {@link CommerceCartParameter} request object.
	 *
	 * @param parameter
	 * 		contains details for updating the cart entry
	 * @param entryModel
	 * 		cart entry to be updated
	 */
	protected void updateCartEntryDetails(final CommerceCartParameter parameter, final CartEntryModel entryModel)
	{
		if (parameter.isAutomaticallyAdded())
		{
			entryModel.setAutomaticallyAdded(true);
		}

		if (StringUtils.isNotEmpty(parameter.getBpoCode()))
		{
			final TmaBundledProductOfferingModel parentBpo = (TmaBundledProductOfferingModel) getTmaPoService()
					.getPoForCode(parameter.getBpoCode());
			if (parentBpo != null)
			{
				entryModel.setBpo(parentBpo);
			}
		}

		if (parameter.getParentEntry() != null)
		{
			AbstractOrderEntryModel parentEntry = parameter.getParentEntry();
			List<AbstractOrderEntryModel> childEntries = CollectionUtils.isEmpty(parentEntry.getChildEntries()) ?
					Lists.newArrayList() : Lists.newArrayList(parentEntry.getChildEntries());
			childEntries.add(entryModel);
			parentEntry.setChildEntries(childEntries);
			entryModel.setMasterEntry(parameter.getParentEntry());
		}
	}


	/**
	 * Creates a new {@link TmaCartSubscriptionInfoModel} based on the information retrieved from the minimum identified
	 * price. In case no subscription term is found on the price, a default value is configured.
	 *
	 * @param bestApplicablePrice
	 * 		The best applicable price
	 * @return subscriptionInfo populated with the subscription term
	 */
	protected TmaCartSubscriptionInfoModel createSubscriptionInfo(final PriceRowModel bestApplicablePrice)
	{
		final TmaCartSubscriptionInfoModel subscriptionInfo = getModelService().create(TmaCartSubscriptionInfoModel.class);
		if (bestApplicablePrice != null && CollectionUtils.isNotEmpty(bestApplicablePrice.getSubscriptionTerms()))
		{
			subscriptionInfo
					.setSubscriptionTerm(bestApplicablePrice.getSubscriptionTerms().iterator().next());
		}
		else
		{
			subscriptionInfo.setSubscriptionTerm(getSubscriptionTermService().getDefaultSubscriptionTerm());
		}

		getModelService().save(subscriptionInfo);
		return subscriptionInfo;
	}

	@Override
	protected void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		if (getCommerceAddToCartMethodHooks() != null
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
				CommerceServicesConstants.ADDTOCARTHOOK_ENABLED)))
		{
			for (final CommerceAddToCartMethodHook commerceAddToCartMethodHook : getCommerceAddToCartMethodHooks())
			{
				commerceAddToCartMethodHook.beforeAddToCart(parameters);
			}
		}
	}

	/**
	 * Checks current user eligibility
	 *
	 * @deprecated since 2105. Use {@link TmaProcessTypeResourceStrategy#validateResource(CommerceCartParameter)} instead.
	 */
	@Deprecated(since = "2105")
	protected void validateApplicableEligiblity(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final Set<TmaProcessType> eligibleProcessesForUser = getCustomerInventoryService().retrieveProcesses();
		try
		{
			if (StringUtils.isNotEmpty(parameter.getProcessType()))
			{
				final TmaProcessType processType = getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE,
						parameter.getProcessType());
				if (!eligibleProcessesForUser.contains(processType))
				{
					throw new CommerceCartModificationException(
							"User currently not eligible for " + parameter.getProcessType() + " flow. ");
				}
			}
		}
		catch (final UnknownIdentifierException e)
		{
			throw new IllegalArgumentException(String.format("Invalid processType: %s", parameter.getProcessType()));
		}

	}

	/**
	 * Returns the best applicable price based on the information provided in the parameter.
	 *
	 * @param parameter
	 * 		contains attributes used for cart updates
	 * @return The best applicable price
	 * @deprecated since 2105. Use instead {@link TmaCommercePriceService#getBestApplicablePrice(TmaPriceContext)}
	 */
	@Deprecated(since = "2105")
	protected PriceRowModel getBestApplicablePrice(CommerceCartParameter parameter)
	{
		if (parameter.getParentEntry() == null)
		{
			return getCommercePriceService().getBestApplicablePrice(getTmaPriceContextService().createPriceContext(parameter));
		}

		final List<AbstractOrderEntryModel> parentEntries = getAbstractOrderEntryService()
				.getBpoParentEntries(parameter.getParentEntry());
		final List<PriceRowModel> bestApplicablePrices = new ArrayList<>();

		parentEntries.forEach((AbstractOrderEntryModel entry) -> {
			parameter.setBpoCode(entry.getProduct().getCode());
			PriceRowModel price = getCommercePriceService()
					.getBestApplicablePrice(getTmaPriceContextService().createPriceContext(parameter));

			if (price != null)
			{
				bestApplicablePrices.add(price);
			}
		});

		if (CollectionUtils.isEmpty(bestApplicablePrices))
		{
			return null;
		}

		return bestApplicablePrices.stream().sorted(Comparator.comparingInt(PriceRowModel::getPriority).reversed())
				.collect(Collectors.toList()).iterator().next();
	}

	private boolean mustHavePrice(final CommerceCartParameter parameter)
	{
		if (parameter.getParentEntry() != null && parameter.getParentEntry()
				.getProduct() instanceof TmaFixedBundledProductOfferingModel)
		{
			return false;
		}

		return !StringUtils.equalsIgnoreCase(TmaBundledProductOfferingModel._TYPECODE, parameter.getProduct().getItemtype());
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(final TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}

	@Required
	public void setSubscriptionTermService(final TmaSubscriptionTermService subscriptionTermService)
	{
		this.subscriptionTermService = subscriptionTermService;
	}

	protected TmaCommerceCartResourceService getTmaCommerceCartResourceService()
	{
		return tmaCommerceCartResourceService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	@Required
	public void setCustomerInventoryService(final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	protected TmaPriceContextService getTmaPriceContextService()
	{
		return tmaPriceContextService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected List<CommerceAddToCartMethodHook> getMasterAddToCartHooks()
	{
		return masterAddToCartEntryHooks;
	}
}

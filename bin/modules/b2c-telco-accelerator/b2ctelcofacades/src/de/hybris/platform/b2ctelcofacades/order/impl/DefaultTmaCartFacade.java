/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.impl;

import de.hybris.platform.b2ctelcofacades.data.CartActionInput;
import de.hybris.platform.b2ctelcofacades.data.TmaCartStrategyType;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.order.TmaCartFacade;
import de.hybris.platform.b2ctelcofacades.strategy.impl.TmaCartStrategyMapping;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartTextGenerationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link TmaCartFacade}.
 *
 * @since 6.7
 */
public class DefaultTmaCartFacade extends DefaultCartFacade implements TmaCartFacade
{
	public static final long DEFAULT_QUANTITY = 1;

	private TmaPoService tmaPoService;
	private TmaSubscriptionTermService subscriptionTermService;
	private TmaCustomerFacade customerFacade;
	private TmaCartStrategyMapping cartStrategyMapping;
	private Converter<CartActionInput, CommerceCartParameter> tmaCommerceCartParameterConverter;
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;
	private final Map<TmaCartStrategyType, TmaCartStrategy> cartStrategyMap;
	private final CommerceSaveCartTextGenerationStrategy saveCartTextGenerationStrategy;
	private final ConfigurationService configurationService;
	private final EnumerationService enumerationService;

	public DefaultTmaCartFacade(final Map<TmaCartStrategyType, TmaCartStrategy> cartStrategyMap,
			final CommerceSaveCartTextGenerationStrategy saveCartTextGenerationStrategy,
			final ConfigurationService configurationService, final EnumerationService enumerationService)
	{
		this.cartStrategyMap = cartStrategyMap;
		this.saveCartTextGenerationStrategy = saveCartTextGenerationStrategy;
		this.configurationService = configurationService;
		this.enumerationService = enumerationService;
	}

	@Override
	public CartModificationData addProductOfferingToCart(final String spoCode, final long quantity, final String processType,
			final String rootBpoCode, final int cartGroupNo, final String subscriptionTermId, final String subscriberId,
			final String subscriberBillingId) throws CommerceCartModificationException
	{
		final CartActionInput cartActionInput = new CartActionInput();
		cartActionInput.setCartId(getCartService().getSessionCart().getCode());
		cartActionInput.setUserGuid(getCustomerFacade().getCurrentCustomerUid());
		cartActionInput.setProductCode(spoCode);
		cartActionInput.setQuantity(quantity);
		cartActionInput.setProcessType(processType);
		cartActionInput.setRootBpoCode(rootBpoCode);
		cartActionInput.setCartGroupNo(cartGroupNo);
		cartActionInput.setSubscriptionTermId(subscriptionTermId);
		cartActionInput.setSubscriberId(subscriberId);
		cartActionInput.setSubscriberBillingId(subscriberBillingId);

		return processCartAction(Collections.singletonList(cartActionInput)).get(0);
	}

	@Override
	public List<CartModificationData> processCartAction(final List<CartActionInput> cartActionInputList)
			throws CommerceCartModificationException
	{
		final List<CartModificationData> cartModificationDataList = new ArrayList<>();
		for (final CartActionInput cartActionInput : cartActionInputList)
		{
			final List<CommerceCartParameter> commerceCartParameters = new ArrayList<>();

			final TmaCartStrategy strategy = findCartStrategy(cartActionInput);
			validateCartActionInput(cartActionInput);
			commerceCartParameters.add(getTmaCommerceCartParameterConverter().convert(cartActionInput));

			cartModificationDataList
					.addAll(getCartModificationConverter().convertAll(strategy.processCartAction(commerceCartParameters)));
		}

		return cartModificationDataList;
	}

	@Override
	public List<CartModificationData> addBpoSelectedOfferings(final String rootBpoCode, final List<String> simpleProductOfferings,
			final String processType, final int cartGroupNo, final String subscriptionTerm, final String subscriberIdentity,
			final String subscriberBillingId) throws CommerceCartModificationException
	{
		final CartActionInput bpoCartActionInput = new CartActionInput();
		final List<CartActionInput> childCartActions = new ArrayList<>();

		simpleProductOfferings.forEach(spo -> {
			final CartActionInput cartActionInput = new CartActionInput();
			cartActionInput.setCartId(getCartService().getSessionCart().getCode());
			cartActionInput.setUserGuid(getCustomerFacade().getCurrentCustomerUid());
			cartActionInput.setProductCode(spo);
			cartActionInput.setRootBpoCode(rootBpoCode);
			cartActionInput.setProcessType(processType);
			cartActionInput.setCartGroupNo(cartGroupNo);
			cartActionInput.setSubscriptionTermId(subscriptionTerm);
			cartActionInput.setSubscriberId(subscriberIdentity);
			cartActionInput.setSubscriberBillingId(subscriberBillingId);
			cartActionInput.setQuantity(DEFAULT_QUANTITY);

			childCartActions.add(cartActionInput);
		});
		bpoCartActionInput.setUserGuid(getCustomerFacade().getCurrentCustomerUid());
		bpoCartActionInput.setQuantity(DEFAULT_QUANTITY);
		bpoCartActionInput.setChildren(childCartActions);
		bpoCartActionInput.setProductCode(rootBpoCode);
		return processCartAction(Collections.singletonList(bpoCartActionInput));
	}

	@Override
	public Optional<CartData> getCartForCodeAndCustomer(final String code, final String customerId)
	{
		final UserModel user = getUserService().getUserForUID(customerId);
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(code, user);
		if (cartModel == null)
		{
			return Optional.empty();
		}
		return Optional.of(getCartConverter().convert(cartModel));
	}

	@Override
	public Optional<CartData> getCartForCodeAndCustomer(final String code, final String customerId, final boolean recalculate)
			throws CalculationException
	{
		final UserModel user = getUserService().getUserForUID(customerId);
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(code, user);

		if (cartModel == null)
		{
			return Optional.empty();
		}

		if (!recalculate)
		{
			return Optional.of(getCartConverter().convert(cartModel));
		}

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setUser(user);
		commerceCartParameter.setCart(cartModel);
		commerceCartParameter.setEnableHooks(true);
		getCommerceCartService().recalculateCart(commerceCartParameter);

		return Optional.of(getCartConverter().convert(cartModel));
	}

	@Override
	public Optional<CartData> getCartForGuid(final String guid)
	{
		final CartModel cartModel = getCommerceCartService().getCartForGuidAndSite(guid, getBaseSiteService().getCurrentBaseSite());

		return cartModel == null ? Optional.empty() : Optional.of(getCartConverter().convert(cartModel));
	}

	@Override
	public List<CartData> getCartsForCustomer(final String customerId)
	{
		final UserModel user = getUserService().getUserForUID(customerId);
		return Converters.convertAll(
				getCommerceCartService().getCartsForSiteAndUser(getBaseSiteService().getCurrentBaseSite(), user), getCartConverter());
	}

	@Override
	public List<CartData> getCartsForCustomer(final String customerId, final boolean recalculate) throws CalculationException
	{
		final UserModel user = getUserService().getUserForUID(customerId);
		final List<CartModel> cartModelList = getCommerceCartService()
				.getCartsForSiteAndUser(getBaseSiteService().getCurrentBaseSite(), user);

		if (CollectionUtils.isEmpty(cartModelList))
		{
			return Collections.emptyList();
		}

		if (!recalculate)
		{
			return Converters.convertAll(cartModelList, getCartConverter());
		}

		for (final CartModel cartModel : cartModelList)
		{
			final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
			commerceCartParameter.setUser(user);
			commerceCartParameter.setCart(cartModel);
			commerceCartParameter.setEnableHooks(true);
			getCommerceCartService().recalculateCart(commerceCartParameter);
		}

		return Converters.convertAll(cartModelList, getCartConverter());
	}

	@Override
	public CartData getSessionCart()
	{
		final CartData cartData;
		final CartModel cart = getCartService().getSessionCart();
		cartData = getCartConverter().convert(cart);
		return cartData;
	}

	@Override
	public CartData createCartForUserAndCurrentBaseSite(final String userId)
	{
		final UserModel anonymousUser = getUserService().getAnonymousUser();

		if (userId.equals(anonymousUser.getUid()))
		{
			return getCartConverter()
					.convert(getCartService().createCartForUserAndBaseSite(anonymousUser, getBaseSiteService().getCurrentBaseSite()));
		}

		final UserModel userModel = getUserService().getUserForUID(userId);
		final List<CartModel> cartModelList = getCommerceCartService()
				.getCartsForSiteAndUser(getBaseSiteService().getCurrentBaseSite(), userModel);

		if (CollectionUtils.isNotEmpty(cartModelList))
		{
			return getCartConverter().convert(cartModelList.get(0));
		}

		return getCartConverter()
				.convert(getCartService().createCartForUserAndBaseSite(userModel, getBaseSiteService().getCurrentBaseSite()));
	}

	@Override
	public CartData getMiniCart()
	{
		final CartData cartData;
		if (hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			cartData = getMiniCartConverter().convert(cart);
		}
		else
		{
			cartData = createEmptyCart();
		}
		return cartData;
	}

	/**
	 * Checks if given card belongs to anonymous user.
	 *
	 * @param cartGuid
	 * 		GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to anonymous user.
	 */
	@Override
	public boolean isAnonymousUserCart(final String cartGuid)
	{
		final CartModel cart = getCommerceCartService().getCartForGuidAndSiteAndUser(cartGuid,
				getBaseSiteService().getCurrentBaseSite(), getUserService().getAnonymousUser());
		return cart != null;
	}

	/**
	 * Checks if given card belongs to current user.
	 *
	 * @param cartGuid
	 * 		GUID of the cart.
	 * @return <tt>true</tt> if the cart belongs to current user.
	 */
	@Override
	public boolean isCurrentUserCart(final String cartGuid)
	{
		final CartModel cart = getCommerceCartService().getCartForGuidAndSiteAndUser(cartGuid,
				getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());
		return cart != null;
	}

	/**
	 * Validates if cart to be cloned is valid and updates the cart action input with description and name.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for clone cart operation
	 * @throws CommerceSaveCartException
	 * 		in case the parameters provided in case cart is null or unsaved
	 */
	@Override
	public void validateAndUpdateCartActionInputForClone(final CartActionInput cartActionInput) throws CommerceSaveCartException
	{
		final CartModel cart;
		final UserModel user = getUserService().getUserForUID(cartActionInput.getUserGuid());
		if (StringUtils.isNotBlank(cartActionInput.getCloneCartID()))
		{
			cart = getCommerceCartService().getCartForCodeAndUser(cartActionInput.getCloneCartID(), user);
		}
		else if (isAnonymousUserCart(cartActionInput.getToCartGUID()))
		{
			cart = getCommerceCartService().getCartForGuidAndSite(cartActionInput.getToCartGUID(),
					getBaseSiteService().getCurrentBaseSite());
			cartActionInput.setCloneCartID(cart.getCode());
		}
		else
		{
			throw new CommerceSaveCartException("Cart to be cloned could not be empty");
		}

		if (cart == null || cart.getSaveTime() == null)
		{
			throw new CommerceSaveCartException("Cannot find a saved cart for code [" + cartActionInput.getCloneCartID() + "]");
		}

		cartActionInput
				.setCloneCartName(StringUtils.isNotBlank(cartActionInput.getCloneCartName()) ? cartActionInput.getCloneCartName()
						: generateSaveCartName(cart));
		cartActionInput.setCloneCartDescription(generateSaveCartDescription(cart, cartActionInput.getCloneCartDescription()));
	}

	/**
	 * Checks if the requested status for cart is valid or not
	 *
	 * @param status
	 * 		the requested status to be updated for cart
	 * @return True if requested status is valid status, otherwise false
	 */
	@Override
	public boolean isValidCartStatus(final String status)
	{
		if (StringUtils.isNotBlank(status))
		{
			final List<OrderStatus> orderStatuses = new ArrayList(
					getEnumerationService().getEnumerationValues(OrderStatus._TYPECODE));
			return orderStatuses.contains(OrderStatus.valueOf(status));
		}

		return true;
	}

	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity)
			throws CommerceCartModificationException
	{
		final AddToCartParams dto = new AddToCartParams();
		dto.setQuantity(quantity);
		final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(dto);
		parameter.setEnableHooks(true);
		parameter.setEntryNumber(entryNumber);
		parameter.setNewQuantity(quantity);

		final CommerceCartModification modification = cartStrategyMap.get(TmaCartStrategyType.MODIFY)
				.processCartAction(Collections.singletonList(parameter)).iterator().next();

		return getCartModificationConverter().convert(modification);
	}

	/**
	 * @deprecated since 1907
	 */
	@Deprecated(since = "1907", forRemoval = true)
	protected Optional<TmaSubscriptionBaseData> getValidCustomerSubscription(final String subscriberId,
			final String subscriberBillingId, final String processType)
	{
		return getCustomerFacade().getEligibleSubscriptionData(subscriberId, subscriberBillingId,
				TmaProcessType.valueOf(processType));
	}

	/**
	 * Validates the data provided for cart update.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	protected void validateCartActionInput(final CartActionInput cartActionInput) throws CommerceCartModificationException
	{
		if (cartActionInput.getParentEntryNumber() != null)
		{
			validateCartActionInputForUpdateBpo(cartActionInput);
			return;
		}

		validateCartActionInputForUpdatePo(cartActionInput);
	}

	/**
	 * Checks if the product offering is {@link TmaSimpleProductOfferingModel} either {@link TmaFixedBundledProductOfferingModel}
	 * and if the product offering and subscription term are valid.
	 *
	 * @param poCode
	 * 		the identifier of the product offering
	 * @param subscriptionTermId
	 * 		the identifier of the subscription term
	 * @throws CommerceCartModificationException
	 * 		in case the identifier or the subscription term provided is incorrect
	 */
	protected void validateProductOfferingAndSubscriptionTerm(final String poCode, final String subscriptionTermId,
			final String processType) throws CommerceCartModificationException
	{
		if (StringUtils.isEmpty(poCode))
		{
			return;
		}

		final TmaProductOfferingModel productOffering = getTmaPoService().getPoForCode(poCode);

		if (!(productOffering instanceof TmaSimpleProductOfferingModel
				|| productOffering instanceof TmaFixedBundledProductOfferingModel)
				|| productOffering instanceof TmaOperationalProductOfferingModel)
		{
			throw new CommerceCartModificationException("Wrong type for product offering with code:" + poCode + ".It should be "
					+ TmaSimpleProductOfferingModel._TYPECODE + " or " + TmaFixedBundledProductOfferingModel._TYPECODE);
		}

		if (!getTmaSubscribedProductFacade().isServicePlan(productOffering))
		{
			return;
		}

		final Collection<TmaBundledProductOfferingModel> allParents = getTmaPoService().getAllParents(productOffering);

		final boolean hasSubscriptionTerm = hasSubscriptionTerm(productOffering, null,
				TmaProcessType.valueOf(processType), subscriptionTermId);

		final boolean hasSubscriptionTermParent = allParents.stream()
				.anyMatch((final TmaBundledProductOfferingModel parent) -> hasSubscriptionTerm(productOffering, parent,
						TmaProcessType.valueOf(processType), subscriptionTermId));

		if (!hasSubscriptionTerm && !hasSubscriptionTermParent)
		{
			throw new CommerceCartModificationException(
					"The product offering '" + poCode + "' does not have price for subscription term '" + subscriptionTermId
							+ "' and process type '" + processType + ".");
		}
	}

	/**
	 * Checks if the provided subscription term is in the prices of the productOffering, parentBpo and processType
	 * provided.
	 *
	 * @param product
	 * 		the product
	 * @param parentBpo
	 * 		the parent of the product
	 * @param processType
	 * 		the process type
	 * @param subscriptionTermId
	 * 		the identifier of the subscription term
	 * @return True if provided subscription term is in the prices of the product and its parent, otherwise false.
	 */
	protected boolean hasSubscriptionTerm(final TmaProductOfferingModel product, final TmaBundledProductOfferingModel parentBpo,
			final TmaProcessType processType, final String subscriptionTermId)
	{
		final Set<SubscriptionTermModel> subscriptionTermModelSet = getSubscriptionTermService()
				.getApplicableSubscriptionTerms(product, parentBpo, processType);

		return StringUtils.isEmpty(subscriptionTermId)
				|| (CollectionUtils.isNotEmpty(subscriptionTermModelSet) && subscriptionTermModelSet.stream().anyMatch(
				(final SubscriptionTermModel subscriptionTerm) -> subscriptionTerm.getId().equals(subscriptionTermId)));
	}

	/**
	 * Returns the corresponding cartStrategy for a given cartActionInput.
	 *
	 * @param cartActionInput
	 * 		the cartActionInput that determines the action we want to apply.
	 * @return the {@link TmaCartStrategy} for the given input.
	 */
	protected TmaCartStrategy findCartStrategy(final CartActionInput cartActionInput)
	{
		final Long quantity = cartActionInput.getQuantity();

		if (StringUtils.isNotEmpty(cartActionInput.getFromCartGUID()))
		{
			return cartStrategyMap.get(TmaCartStrategyType.MERGE);
		}

		if (StringUtils.isNotEmpty(cartActionInput.getCloneCartID()))
		{
			return cartStrategyMap.get(TmaCartStrategyType.CLONE);
		}

		if (cartActionInput.isRestoreCart())
		{
			return cartStrategyMap.get(TmaCartStrategyType.RESTORE);
		}
		if (cartActionInput.getEntryId() == null
				&& (cartActionInput.getProductCode() != null || cartActionInput.getSubscribedProductCode() != null))
		{
			return cartStrategyMap.get(TmaCartStrategyType.ADD);
		}

		if (quantity != null && quantity == 0)
		{
			return cartStrategyMap.get(TmaCartStrategyType.DELETE);
		}

		return cartStrategyMap.get(TmaCartStrategyType.MODIFY);
	}

	/**
	 * When restoring a saved cart, one copy of the restored saved cart can be kept. The name of the copied/(cloned) cart
	 * is the original saved cart name + copy#. The property b2ctelcooccaddon.clone.savecart.name.regex set the regex for
	 * the name suffix of #
	 *
	 * @param cartModel
	 * 		the cart which will be cloned
	 * @return name the name which will be given to cloned cart
	 */
	protected String generateSaveCartName(final CartModel cartModel)
	{
		final String baseCartName = StringUtils.trim(cartModel.getName());

		if (StringUtils.isEmpty(baseCartName))
		{
			return getSaveCartTextGenerationStrategy().generateSaveCartName(cartModel);
		}

		final String copyCountRegex = getConfigurationService().getConfiguration()
				.getString("b2ctelcooccaddon.clone.savecart.name.regex" + "." + getBaseSiteService().getCurrentBaseSite().getUid());

		if (StringUtils.isEmpty(copyCountRegex))
		{
			final StringBuilder clonePrefix = new StringBuilder(Localization.getLocalizedString("b2ctelcooccaddon.cart.copyof"))
					.append(" ");
			return clonePrefix.append(baseCartName).toString();
		}

		return getSaveCartTextGenerationStrategy().generateCloneSaveCartName(cartModel, copyCountRegex);

	}

	/**
	 * Generate description for cart to be cloned if not provided by user.
	 *
	 * @param cartModel
	 * 		the cart which will be cloned
	 * @param description
	 * 		the description provided by user which will be given to cloned cart
	 * @return description the description which will given to cloned cart
	 */
	protected String generateSaveCartDescription(final CartModel cartModel, final String description)
	{

		if (StringUtils.isEmpty(description) && StringUtils.isEmpty(cartModel.getDescription()))
		{
			return getSaveCartTextGenerationStrategy().generateSaveCartDescription(cartModel);
		}

		if (StringUtils.isNotEmpty(description))
		{
			return description;
		}

		return cartModel.getDescription();
	}

	/**
	 * Validates the main cart action input when adding POs to an existing BPO.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	private void validateCartActionInputForUpdateBpo(final CartActionInput cartActionInput)
			throws CommerceCartModificationException
	{
		if (isCartActionInputForOperationsOnCart(cartActionInput))
		{
			return;
		}

		final UserModel user = getUserService().getUserForUID(cartActionInput.getUserGuid());
		final AbstractOrderModel abstractOrder = getCommerceCartService().getCartForCodeAndUser(cartActionInput.getCartId(), user);

		if (abstractOrder == null)
		{
			throw new CommerceCartModificationException(
					String.format("Cart with code '%s' and user '%s' not found.", cartActionInput.getCartId(),
							cartActionInput.getUserGuid()));
		}

		final Optional<AbstractOrderEntryModel> orderEntry = abstractOrder.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getEntryNumber().equals(cartActionInput.getParentEntryNumber()))
				.findFirst();

		if (orderEntry.isEmpty())
		{
			throw new CommerceCartModificationException(
					String.format("Entry #'%s' not found in cart '%s'.", cartActionInput.getParentEntryNumber(),
							cartActionInput.getCartId()));
		}

		if (!(orderEntry.get().getProduct() instanceof TmaBundledProductOfferingModel))
		{
			throw new CommerceCartModificationException(
					String.format("Can't add product offering '%s' to entry #%s. Entry should contain product offering of type %s.",
							cartActionInput.getProductCode(), orderEntry.get().getEntryNumber(),
							TmaBundledProductOfferingModel._TYPECODE));
		}

		if (StringUtils.isNotEmpty(cartActionInput.getProcessType()) && !(orderEntry.get().getProcessType().getCode()
				.equals(cartActionInput.getProcessType())))
		{
			throw new CommerceCartModificationException(
					String.format("Can't add product offering '%s' with process type '%s'.",
							cartActionInput.getProductCode(), cartActionInput.getProcessType()));
		}

		if (!isBpoDirectParent(cartActionInput.getProductCode(), (TmaBundledProductOfferingModel) orderEntry.get().getProduct()))
		{
			throw new CommerceCartModificationException(
					String.format("Bundled product offering '%s' is not a direct parent of '%s'.",
							orderEntry.get().getProduct().getCode(), cartActionInput.getProductCode()));
		}

		if (CollectionUtils.isEmpty(cartActionInput.getChildren()))
		{
			return;
		}

		validateBundledCartActionInput(cartActionInput);

		for (CartActionInput childCartActionInput : cartActionInput.getChildren())
		{
			validateCartActionInputForUpdateBpoChild(childCartActionInput, orderEntry.get().getProcessType().getCode());
		}
	}

	/**
	 * Validates the child cart action input when adding POs to an existing BPO.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	private void validateCartActionInputForUpdateBpoChild(final CartActionInput cartActionInput, final String processType)
			throws CommerceCartModificationException
	{
		if (CollectionUtils.isNotEmpty(cartActionInput.getChildren()))
		{
			for (CartActionInput childCartActionInput : cartActionInput.getChildren())
			{
				validateCartActionInputForUpdateBpoChild(childCartActionInput, processType);
			}
			return;
		}

		if (StringUtils.isEmpty(cartActionInput.getProductCode()))
		{
			throw new CommerceCartModificationException("Product offering cannot be empty.");
		}

		if (StringUtils.isNotEmpty(cartActionInput.getProcessType()))
		{
			throw new CommerceCartModificationException("Process type must be empty.");
		}

		validateProductOfferingAndSubscriptionTerm(cartActionInput.getProductCode(), cartActionInput.getSubscriptionTermId(),
				processType);
	}

	/**
	 * Checks if the data provided for cart update is valid.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	private void validateCartActionInputForUpdatePo(final CartActionInput cartActionInput) throws CommerceCartModificationException
	{
		if (CollectionUtils.isEmpty(cartActionInput.getChildren()))
		{
			validateSimpleCartActionInput(cartActionInput);
			return;
		}

		validateBundledCartActionInput(cartActionInput);
		for (CartActionInput childInput : cartActionInput.getChildren())
		{
			validateCartActionInputForUpdatePo(childInput);
		}
	}

	/**
	 * Checks if the data provided for cart action input without child is valid.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	private void validateSimpleCartActionInput(CartActionInput cartActionInput) throws CommerceCartModificationException
	{
		if (isCartActionInputForOperationsOnCart(cartActionInput))
		{
			return;
		}

		if (cartActionInput.getEntryId() == null && StringUtils.isEmpty(cartActionInput.getProcessType()))
		{
			throw new CommerceCartModificationException("Process type cannot be empty");
		}

		validateProductOfferingAndSubscriptionTerm(cartActionInput.getProductCode(), cartActionInput.getSubscriptionTermId(),
				cartActionInput.getProcessType());
	}

	/**
	 * Checks if the data provided for cart action input with child is valid.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		in case the parameters provided in cartActionInputList are not correct
	 */
	private void validateBundledCartActionInput(CartActionInput cartActionInput) throws CommerceCartModificationException
	{
		final TmaProductOfferingModel productOffering = getTmaPoService().getPoForCode(cartActionInput.getProductCode());

		if (!(productOffering instanceof TmaBundledProductOfferingModel))
		{
			throw new CommerceCartModificationException("Wrong type for product offering with code:"
					+ cartActionInput.getProductCode() + ".It should be " + TmaBundledProductOfferingModel._TYPECODE);
		}

		if (CollectionUtils.isEmpty(cartActionInput.getChildren()))
		{
			return;
		}

		for (CartActionInput childActionInput : cartActionInput.getChildren())
		{
			if (childActionInput.getProductCode() != null && !isBpoDirectParent(childActionInput.getProductCode(),
					(TmaBundledProductOfferingModel) productOffering))
			{
				throw new CommerceCartModificationException(
						String.format("Bundled product offering '%s' is not a direct parent of '%s'.", cartActionInput.getProductCode(),
								childActionInput.getProductCode()));
			}
		}
	}

	private boolean isCartActionInputForOperationsOnCart(final CartActionInput cartActionInput)
	{
		return StringUtils.isEmpty(cartActionInput.getEntryId()) && StringUtils.isEmpty(cartActionInput.getProductCode())
				&& cartActionInput.getAction() == null;
	}


	private boolean isBpoDirectParent(final String poCode, final TmaBundledProductOfferingModel bpo)
	{
		if (CollectionUtils.isEmpty(bpo.getChildren()))
		{
			return false;
		}

		final List<String> childPos = bpo.getChildren().stream().map(TmaProductOfferingModel::getCode).collect(Collectors.toList());

		return childPos.contains(poCode);
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}

	protected TmaCustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	protected TmaCartStrategyMapping getCartStrategyMapping()
	{
		return cartStrategyMapping;
	}

	protected Converter<CartActionInput, CommerceCartParameter> getTmaCommerceCartParameterConverter()
	{
		return tmaCommerceCartParameterConverter;
	}

	@Override
	public TmaCartService getCartService()
	{
		return (TmaCartService) super.getCartService();
	}

	protected TmaSubscribedProductFacade getTmaSubscribedProductFacade()
	{
		return tmaSubscribedProductFacade;
	}

	protected CommerceSaveCartTextGenerationStrategy getSaveCartTextGenerationStrategy()
	{
		return saveCartTextGenerationStrategy;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	@Required
	public void setSubscriptionTermService(final TmaSubscriptionTermService subscriptionTermService)
	{
		this.subscriptionTermService = subscriptionTermService;
	}

	@Required
	public void setCustomerFacade(final TmaCustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	@Required
	public void setCartStrategyMapping(final TmaCartStrategyMapping cartStrategyMapping)
	{
		this.cartStrategyMapping = cartStrategyMapping;
	}

	@Required
	public void setTmaCommerceCartParameterConverter(
			final Converter<CartActionInput, CommerceCartParameter> tmaCommerceCartParameterConverter)
	{
		this.tmaCommerceCartParameterConverter = tmaCommerceCartParameterConverter;
	}

	@Required
	public void setTmaSubscribedProductFacade(final TmaSubscribedProductFacade tmaSubscribedProductFacade)
	{
		this.tmaSubscribedProductFacade = tmaSubscribedProductFacade;
	}
}

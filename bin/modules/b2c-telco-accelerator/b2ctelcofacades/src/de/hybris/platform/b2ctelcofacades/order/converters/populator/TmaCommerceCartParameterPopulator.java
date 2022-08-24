/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.CartActionInput;
import de.hybris.platform.b2ctelcofacades.data.TmaPlaceData;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator for {@link CommerceCartParameter}
 *
 * @since 1907
 */
public class TmaCommerceCartParameterPopulator implements Populator<CartActionInput, CommerceCartParameter>
{
	private static final String WRONG_SUBSCRIBED_PRODUCT = "invalid subscribed product code";
	private static final String WRONG_ENTRY_NUMBER = "Invalid entry number.";
	private static final String IN_MEMORY_CART = "InMemoryCart";

	private CartService cartService;
	private ProductService productService;
	private PointOfServiceService pointOfServiceService;
	private CommerceCartService commerceCartService;
	private UserService userService;
	private TmaCustomerInventoryService customerInventoryService;
	private BaseSiteService baseSiteService;
	private final DeliveryModeService deliveryModeService;
	private final Map<TmaPlaceRoleType, Converter> placeConverters;
	private final ModelService modelService;
	private final Converter<CartActionInput, CommerceCartParameter> commerceCartParameterConverter;
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaCommerceCartParameterPopulator(final Map<TmaPlaceRoleType, Converter> placeConverters,
			final DeliveryModeService deliveryModeService, final ModelService modelService,
			final Converter<CartActionInput, CommerceCartParameter> commerceCartParameterConverter,
			final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.placeConverters = placeConverters;
		this.deliveryModeService = deliveryModeService;
		this.modelService = modelService;
		this.commerceCartParameterConverter = commerceCartParameterConverter;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void populate(final CartActionInput cartActionInput, final CommerceCartParameter commerceCartParameter)
			throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", cartActionInput);
		validateParameterNotNullStandardMessage("target", commerceCartParameter);

		final String cartModelTypeCode = Config.getString(JaloSession.CART_TYPE, "Cart");
		final CartModel cartModel = cartModelTypeCode.equalsIgnoreCase(IN_MEMORY_CART) ?
				getModelService().get(JaloSession.getCurrentSession().getCart()) :
				getCart(cartActionInput);
		commerceCartParameter.setCart(cartModel);
		getCartService().setSessionCart(cartModel);

		commerceCartParameter.setUser(getUser(cartActionInput));

		if (cartActionInput.getParentEntryNumber() != null)
		{
			final AbstractOrderEntryModel parentEntry = getEntryFor(cartModel, cartActionInput.getParentEntryNumber());
			commerceCartParameter.setParentEntry(parentEntry);
			populateCartAction(cartActionInput, parentEntry);
		}

		if (CollectionUtils.isNotEmpty(cartActionInput.getPlaces()))
		{
			commerceCartParameter.setPlaces(getPlaces(cartActionInput));
		}

		commerceCartParameter.setPaymentMethodId(cartActionInput.getPaymentMethodId());
		commerceCartParameter.setCouponIds(cartActionInput.getCouponIds());
		commerceCartParameter.setFromCartGUID(cartActionInput.getFromCartGUID());
		commerceCartParameter.setDeliveryMode(getDeliveryMode(cartActionInput.getDelieryModeId()));
		commerceCartParameter.setEnableHooks(true);
		commerceCartParameter.setCreateNewEntry(false);
		commerceCartParameter.setContractStartDate(cartActionInput.getContractStartDate());

		if (cartActionInput.getQuantity() != null)
		{
			commerceCartParameter.setQuantity(cartActionInput.getQuantity());
		}

		commerceCartParameter.setNewQuantity(cartActionInput.getQuantity());
		commerceCartParameter.setBpoCode(cartActionInput.getRootBpoCode());
		commerceCartParameter.setProcessType(cartActionInput.getProcessType());
		commerceCartParameter.setProductSpecCharacteristicConfigs(cartActionInput.getConfigurableProductSpecCharacteristics());
		commerceCartParameter.setAppointmentId(cartActionInput.getAppointmentId());
		commerceCartParameter.setPlaces(getPlaces(cartActionInput));
		commerceCartParameter.setServiceProvider(cartActionInput.getServiceProvider());
		if (cartActionInput.getEntryNumber() != null)
		{
			commerceCartParameter.setEntryNumber(cartActionInput.getEntryNumber());
		}

		commerceCartParameter.setEntryGroupNumbers(getEntryGroupNumbers(cartActionInput));
		commerceCartParameter.setPointOfService(getPointOfService(cartActionInput.getStoreId()));

		commerceCartParameter
				.setUnit(commerceCartParameter.getProduct() == null ? null : commerceCartParameter.getProduct().getUnit());

		commerceCartParameter.setCloneCartID(cartActionInput.getCloneCartID());
		commerceCartParameter.setCloneCartName(cartActionInput.getCloneCartName());
		commerceCartParameter.setCloneCartDescription(cartActionInput.getCloneCartDescription());

		final String firstSubscriptionTerm = cartActionInput.getSubscriptionTermId();
		commerceCartParameter
				.setSubscriptionInfo(getCustomerInventoryService().createNewCartSubscriptionInfo(cartActionInput.getSubscriberId(),
						cartActionInput.getSubscriberBillingId(), firstSubscriptionTerm, cartActionInput.getProcessType()));

		commerceCartParameter.setSubscribedProduct(getSubscribedProduct(cartActionInput.getSubscribedProductCode(),
				commerceCartParameter.getUser()));

		commerceCartParameter.setAction(cartActionInput.getAction());
		commerceCartParameter.setProduct(getProduct(cartActionInput.getProductCode()));
		if (cartActionInput.getStatus() != null)
		{
			commerceCartParameter.setStatus(OrderStatus.valueOf(cartActionInput.getStatus().toString()));
		}
		populateChildrenParameters(cartActionInput, commerceCartParameter);
	}

	private void populateChildrenParameters(final CartActionInput cartActionInput,
			final CommerceCartParameter commerceCartParameter)
	{
		if (CollectionUtils.isEmpty(cartActionInput.getChildren()))
		{
			return;
		}
		commerceCartParameter.setCreateNewEntry(true);
		List<CommerceCartParameter> childParamList = getCommerceCartParameterConverter()
				.convertAll(cartActionInput.getChildren());
		childParamList.forEach(childParam -> {
			childParam.setCreateNewEntry(commerceCartParameter.isCreateNewEntry());
			if (StringUtils.isEmpty(childParam.getProcessType()))
			{
				childParam.setProcessType(commerceCartParameter.getProcessType());
			}
		});
		commerceCartParameter.setChildren(childParamList);
	}

	private void populateCartAction(final CartActionInput cartActionInput, final AbstractOrderEntryModel parentEntry)
	{
		if (StringUtils.isEmpty(cartActionInput.getProcessType()))
		{
			cartActionInput.setProcessType(parentEntry.getProcessType().getCode());
		}

		if (StringUtils.isEmpty(cartActionInput.getRootBpoCode()))
		{
			final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(parentEntry);
			cartActionInput.setRootBpoCode(rootEntry.getProduct().getCode());
		}

		if (CollectionUtils.isNotEmpty(cartActionInput.getChildren()))
		{
			cartActionInput.getChildren().stream()
					.forEach(childCartAction -> populateCartAction(childCartAction, parentEntry));
		}
	}

	protected DeliveryModeModel getDeliveryMode(final String deliveryModeId)
	{
		return StringUtils.isEmpty(deliveryModeId) ? null : getDeliveryModeService().getDeliveryModeForCode(deliveryModeId);
	}

	protected Set<Integer> getEntryGroupNumbers(final CartActionInput cartActionInput)
	{
		return cartActionInput.getCartGroupNo() == null || cartActionInput.getCartGroupNo() == -1 ? Collections.emptySet()
				: Collections.singleton(cartActionInput.getCartGroupNo());
	}

	protected ProductModel getProduct(final String productCode)
	{
		return StringUtils.isEmpty(productCode) ? null : getProductService().getProductForCode(productCode);
	}

	protected PointOfServiceModel getPointOfService(final String storeId)
	{
		return StringUtils.isEmpty(storeId) ? null : getPointOfServiceService().getPointOfServiceForName(storeId);
	}

	/**
	 * Returns a cart for the parameters provided.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart entry updates
	 * @return {@link} CartModel
	 */
	protected CartModel getCart(final CartActionInput cartActionInput)
	{
		if (StringUtils.isNotEmpty(cartActionInput.getCartId()))
		{
			return getCommerceCartService()
					.getCartForCodeAndUser(cartActionInput.getCartId(), getUserService().getUserForUID(cartActionInput.getUserGuid()));
		}

		if (StringUtils.isNotEmpty(cartActionInput.getCloneCartID()))
		{
			return getCommerceCartService()
					.getCartForCodeAndUser(cartActionInput.getCloneCartID(),
							getUserService().getUserForUID(cartActionInput.getUserGuid()));
		}

		return getCommerceCartService()
				.getCartForGuidAndSiteAndUser(cartActionInput.getToCartGUID(), getBaseSiteService().getCurrentBaseSite(),
						getUserService().getUserForUID(cartActionInput.getUserGuid()));


	}

	/**
	 * Returns the places found on the product from the {@link CartActionInput} provided.
	 *
	 * @param cartActionInput
	 * 		the input.
	 * @return the places found on the cartAction given as input.
	 */
	protected List<TmaPlace> getPlaces(final CartActionInput cartActionInput)
	{
		final List<TmaPlace> places = new ArrayList<>();
		final List<TmaPlaceData> inputPlaces = cartActionInput.getPlaces();
		if (CollectionUtils.isEmpty(inputPlaces))
		{
			return places;
		}
		inputPlaces.forEach(placeData ->
		{
			final Converter placeConverter = getPlaceConverters().get(placeData.getRole());
			if (placeConverter != null)
			{
				places.add((TmaPlace) placeConverter.convert(placeData));
			}
		});
		return places;
	}

	/**
	 * Returns a user for the parameters provided.
	 *
	 * @param cartActionInput
	 * 		contains attributes used for cart updates
	 * @return {@link} CartModel
	 */
	protected UserModel getUser(final CartActionInput cartActionInput)
	{
		if (StringUtils.isEmpty(cartActionInput.getUserGuid()))
		{
			return null;
		}

		return getUserService().getUserForUID(cartActionInput.getUserGuid());
	}

	/**
	 * In the case of keep or remove CPI actions we obtain the productOffering from the subscribedProduct not from the
	 * request.
	 *
	 * @param subscribedProductInput
	 * 		the subscribed product
	 * @param user
	 * 		the customer
	 * @return the productOffering corresponding to the subscribedProduct if found; null otherwise.
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected ProductModel getProductOfferingFromSubscribedProduct(final TmaSubscribedProductModel subscribedProductInput,
			final UserModel user)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("subscribedProduct", subscribedProductInput);
		ServicesUtil.validateParameterNotNullStandardMessage("user", user);

		final TmaSubscribedProductModel subscribedProduct = getCustomerInventoryService()
				.getSubscribedProductById(subscribedProductInput.getId(), (CustomerModel) user).get();
		final String productCode = subscribedProduct.getProductCode();
		return getProduct(productCode);
	}

	/**
	 * This method gets a subscribed product with the given code from the customer's product inventory.
	 *
	 * @param subscribedProductCode
	 * 		the id of the subscribedProduct
	 * @param customer
	 * 		the customer
	 * @return The subscribed product if found;otherwise it will throw an exception.
	 */
	protected TmaSubscribedProductModel getSubscribedProduct(final String subscribedProductCode, final UserModel customer)
	{
		if (StringUtils.isEmpty(subscribedProductCode) || customer == null)
		{
			return null;
		}
		final Optional<TmaSubscribedProductModel> subscribedProduct = getCustomerInventoryService()
				.getSubscribedProductById(subscribedProductCode, (CustomerModel) customer);
		if (!subscribedProduct.isPresent())
		{
			throw new IllegalArgumentException(WRONG_SUBSCRIBED_PRODUCT);
		}
		return subscribedProduct.get();
	}

	/**
	 * Returns the entry for the provided entry number.
	 *
	 * @param abstractOrder
	 * 		the abstract order
	 * @param entryNumber
	 * 		the entry number
	 * @return The entry if found;otherwise it will throw an exception.
	 */
	protected AbstractOrderEntryModel getEntryFor(final AbstractOrderModel abstractOrder, final int entryNumber)
	{
		final Optional<AbstractOrderEntryModel> entryForEntryNumber = abstractOrder.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getEntryNumber().equals(entryNumber)).findFirst();

		if (entryForEntryNumber.isEmpty())
		{
			throw new IllegalArgumentException(WRONG_ENTRY_NUMBER);
		}

		return entryForEntryNumber.get();
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	protected Map<TmaPlaceRoleType, Converter> getPlaceConverters()
	{
		return placeConverters;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Required
	public void setPointOfServiceService(final PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setCustomerInventoryService(
			final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	protected DeliveryModeService getDeliveryModeService()
	{
		return deliveryModeService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected Converter<CartActionInput, CommerceCartParameter> getCommerceCartParameterConverter()
	{
		return commerceCartParameterConverter;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}

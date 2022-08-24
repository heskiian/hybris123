/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.bundle.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.b2ctelcofacades.bundle.TmaCheckoutFacade;
import de.hybris.platform.b2ctelcoservices.order.exception.OrderProcessingException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.PriceValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * TMA implementation of checkout facade.
 */
public class DefaultTmaCheckoutFacade extends DefaultAcceleratorCheckoutFacade implements TmaCheckoutFacade
{
	private static final String PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY = "subscription-only";

	/**
	 * Commerce cart service
	 */
	private CommerceCartService commerceCartService;

	/**
	 * Converts from {@link CartModel} to {@link CartData}
	 */
	private Converter<CartModel, CartData> cartConverter;

	/**
	 * Converts from {@link UserModel} to {@link PrincipalData}
	 */
	private Converter<UserModel, PrincipalData> principalConverter;

	/**
	 * Converter from {@link OrderData} to {@link CartData}
	 */
	private Converter<OrderData, CartData> orderDataToCartDataConverter;

	/**
	 * Converts from {@link CartData} to {@link CartModel}
	 */
	private Converter<CartData, CartModel> cartReverseConverter;

	@Override
	public boolean setCheapestDeliveryModeForCheckout()
	{
		final List<? extends DeliveryModeData> availableDeliveryModes = getSupportedDeliveryModes();
		if (availableDeliveryModes.isEmpty())
		{
			return false;
		}
		final boolean isSubscriptionOnlyCart = cartContainsSubscriptionProductsOnly(getCheckoutCart());

		for (final DeliveryModeData deliveryMethod : availableDeliveryModes)
		{
			if (isSubscriptionOnlyCart)
			{
				if (StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
				{
					return setDeliveryMode(deliveryMethod.getCode());
				}
			}
			else
			{
				if (!StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
				{
					return setDeliveryMode(deliveryMethod.getCode());
				}
			}
		}

		return false;
	}

	@Override
	public List<DeliveryModeData> getSupportedDeliveryModesForCartAndUser(String cartCode, String userCode)
	{
		final UserModel userModel = getUserService().getUserForUID(userCode);
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartCode, userModel);
		final List<DeliveryModeData> result = new ArrayList<>();

		if (cartModel == null)
		{
			throw new IllegalArgumentException("There is no cart found for user '" + userCode + "' with code '" + cartCode + "'.");
		}

		final List<DeliveryModeModel> deliveryModeModelList = getDeliveryService().getSupportedDeliveryModeListForOrder(cartModel);

		if (deliveryModeModelList == null)
		{
			return result;
		}

		for (final DeliveryModeModel deliveryModeModel : deliveryModeModelList)
		{
			if (deliveryModeModel instanceof ZoneDeliveryModeModel)
			{
				final ZoneDeliveryModeData zoneDeliveryModeData =
						getZoneDeliveryModeConverter().convert((ZoneDeliveryModeModel) deliveryModeModel);
				zoneDeliveryModeData.setCart(getCartConverter().convert(cartModel));
				zoneDeliveryModeData.setUser(getPrincipalConverter().convert(userModel));
				final PriceValue deliveryCost = getDeliveryService()
						.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel,
								cartModel);
				if (deliveryCost != null)
				{
					zoneDeliveryModeData.setDeliveryCost(getPriceDataFactory().create(PriceDataType.BUY,
							BigDecimal.valueOf(deliveryCost.getValue()), deliveryCost.getCurrencyIso()));
				}
				result.add(zoneDeliveryModeData);
			}
			else
			{
				DeliveryModeData deliveryModeData = getDeliveryModeConverter().convert(deliveryModeModel);
				deliveryModeData.setCart(getCartConverter().convert(cartModel));
				deliveryModeData.setUser(getPrincipalConverter().convert(userModel));
				result.add(deliveryModeData);
			}
		}

		return result;
	}

	@Override
	public OrderData placeOrderFromCart(String cartId, String userId, boolean removeCart) throws OrderProcessingException
	{
		if (!authorizePayment(null))
		{
			throw new OrderProcessingException("Payment authorization was not successful",
					OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR, "Cart with code " + cartId);
		}

		final UserModel user = getUserService().getUserForUID(userId);
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);
		if (cartModel == null)
		{
			throw new OrderProcessingException("Cart does not exist",
					OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR,
					"There is no cart found for user " + userId + " with code " + cartId);
		}

		try
		{
			CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
			commerceCartParameter.setCart(cartModel);
			commerceCartParameter.setEnableHooks(true);
			getCommerceCartService().recalculateCart(commerceCartParameter);

			beforePlaceOrder(cartModel);
			final OrderModel orderModel = placeOrder(cartModel);
			afterPlaceOrder(cartModel, orderModel, removeCart);
			if (orderModel == null)
			{
				throw new OrderProcessingException("Cannot place order from cart",
						OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR,
						"There was an issue in creating the order");
			}

			OrderData createdOrderData = getOrderConverter().convert(orderModel);
			if (createdOrderData == null)
			{
				throw new OrderProcessingException("Cannot place order from cart",
						OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR,
						"There was an issue in creating the order");
			}

			return createdOrderData;
		}
		catch (CalculationException e)
		{
			throw new OrderProcessingException("Cart cannot be properly recalculated",
					OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR,
					"There is a calculation issue for cart with id " + cartId, e);
		}
		catch (InvalidCartException e)
		{
			throw new OrderProcessingException(e.getMessage(),
					OrderProcessingException.OrderProcessingErrorCode.ORDER_CREATION_ERROR,
					"Cart with id " + cartId + " is invalid ", e);
		}
	}

	@Override
	protected void beforePlaceOrder(final CartModel cartModel)
	{
		if (isCartEmpty(cartModel))
		{
			throw new IllegalArgumentException("The order cannot be placed for an empty cart.");
		}
		super.beforePlaceOrder(cartModel);
	}

	protected boolean isCartEmpty(final CartModel cartModel)
	{
		return CollectionUtils.isEmpty(cartModel.getEntries()) && CollectionUtils.isEmpty(cartModel.getEntryGroups());
	}

	@Override
	public OrderData placeOrderFromDto(OrderData orderData, String userId) throws OrderProcessingException
	{
		CartModel cartModel = getCartReverseConverter().convert(getOrderDataToCartDataConverter().convert(orderData));
		getModelService().save(cartModel);
		getModelService().refresh(cartModel);

		return placeOrderFromCart(cartModel.getCode(), userId, true);
	}

	protected boolean cartContainsSubscriptionProductsOnly(final CartData cartData)
	{
		for (final OrderEntryData entry : cartData.getEntries())
		{
			if (entry.getProduct() != null && entry.getProduct().getSubscriptionTerm() == null
					&& entry.getDeliveryPointOfService() == null)
			{
				return false;
			}
		}
		return true;
	}

	private void afterPlaceOrder(CartModel cartModel, OrderModel orderModel, boolean removeCart)
	{
		if (removeCart)
		{
			getModelService().remove(cartModel);
		}

		getModelService().refresh(orderModel);
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	protected Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	@Required
	public void setCartConverter(
			Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

	protected Converter<UserModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(
			Converter<UserModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}

	public Converter<OrderData, CartData> getOrderDataToCartDataConverter()
	{
		return orderDataToCartDataConverter;
	}

	@Required
	public void setOrderDataToCartDataConverter(
			Converter<OrderData, CartData> orderDataToCartDataConverter)
	{
		this.orderDataToCartDataConverter = orderDataToCartDataConverter;
	}

	public Converter<CartData, CartModel> getCartReverseConverter()
	{
		return cartReverseConverter;
	}

	@Required
	public void setCartReverseConverter(
			Converter<CartData, CartModel> cartReverseConverter)
	{
		this.cartReverseConverter = cartReverseConverter;
	}
}

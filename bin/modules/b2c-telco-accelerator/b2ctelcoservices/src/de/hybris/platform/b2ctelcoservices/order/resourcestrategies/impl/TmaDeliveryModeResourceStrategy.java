/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceDeliveryModeStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import org.apache.commons.lang.StringUtils;


/**
 * Resource strategy implementation. Validates and updates delivery mode.
 *
 * @since 1911
 */
public class TmaDeliveryModeResourceStrategy implements TmaAbstractOrderResourceStrategy
{

	private DeliveryService deliveryService;
	private CommerceDeliveryModeStrategy commerceDeliveryModeStrategy;

	public TmaDeliveryModeResourceStrategy(DeliveryService deliveryService,
			CommerceDeliveryModeStrategy commerceDeliveryModeStrategy)
	{
		this.deliveryService = deliveryService;
		this.commerceDeliveryModeStrategy = commerceDeliveryModeStrategy;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter commerceCartParameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(commerceCartParameter);

		if (commerceCartParameter.getDeliveryMode() == null)
		{
			return result;
		}

		if (commerceCartParameter.getCart().getDeliveryAddress() == null || StringUtils
				.isEmpty(commerceCartParameter.getCart().getDeliveryAddress().getId()))
		{
			result.setValid(false);
			result.setMessage("Cart '" + commerceCartParameter.getCart().getCode() + "' does not have a delivery address set.");
			return result;
		}

		if (commerceCartParameter.getCart().getDeliveryAddress().getCountry() == null)
		{
			result.setValid(false);
			result.setMessage("Address '" + commerceCartParameter.getCart().getDeliveryAddress() + "' does not have the country set.");
			return result;
		}

		if (getDeliveryService().getSupportedDeliveryModeListForOrder(commerceCartParameter.getCart()).stream().filter(
				(DeliveryModeModel deliveryModeModel) -> deliveryModeModel.getCode()
						.equals(commerceCartParameter.getDeliveryMode().getCode())).findAny().isEmpty())
		{
			result.setValid(false);
			result.setMessage("Invalid delivery mode '" + commerceCartParameter.getDeliveryMode().getCode() + "' for cart '"
					+ commerceCartParameter.getCart().getCode() + "'.");
			return result;
		}

		return result;
	}

	@Override
	public void updateResource(CommerceCartParameter commerceCartParameter,
			CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		if (commerceCartParameter.getDeliveryMode() == null)
		{
			return;
		}

		final CommerceCheckoutParameter commerceCheckoutParameter = new CommerceCheckoutParameter();
		commerceCheckoutParameter.setCart(commerceCartParameter.getCart());
		commerceCheckoutParameter.setDeliveryMode(commerceCartParameter.getDeliveryMode());

		if (!getCommerceDeliveryModeStrategy().setDeliveryMode(commerceCheckoutParameter))
		{
			throw new CommerceCartModificationException(
					"Delivery mode '" + commerceCartParameter.getDeliveryMode().getCode() + "' cound not be added to cart '"
							+ commerceCartParameter.getCart().getCode() + "'.");
		}

		commerceCartModification.setDeliveryModeChanged(true);
		commerceCartModification.setDeliveryMode(commerceCartParameter.getDeliveryMode());
	}

	protected DeliveryService getDeliveryService()
	{
		return deliveryService;
	}

	protected CommerceDeliveryModeStrategy getCommerceDeliveryModeStrategy()
	{
		return commerceDeliveryModeStrategy;
	}
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;


/**
 * Resource strategy implementation. Validates and updates cart status.
 *
 * @since 2007
 */
public class TmaCartStatusResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private final ModelService modelService;
	private final EnumerationService enumerationService;

	public TmaCartStatusResourceStrategy(final ModelService modelService, final EnumerationService enumerationService)
	{
		this.modelService = modelService;
		this.enumerationService = enumerationService;
	}

	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		final TmaCartValidationResult cartValidationResult = new TmaCartValidationResult();
		cartValidationResult.setCommerceCartParameter(parameter);
		cartValidationResult.setValid(true);

		final List<OrderStatus> cartStatuses = new ArrayList(enumerationService.getEnumerationValues(OrderStatus._TYPECODE));
		if (parameter.getStatus() != null && !cartStatuses.contains(parameter.getStatus()))
		{
			cartValidationResult.setValid(false);
			cartValidationResult.setMessage("No valid status found for cart.");
		}

		return cartValidationResult;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		final OrderStatus newCartStatus = commerceCartParameter.getStatus();
		if (ObjectUtils.isEmpty(newCartStatus))
		{
			return;
		}

		final CartModel cart = commerceCartParameter.getCart();
		final OrderStatus existingCartStatus = ObjectUtils.isEmpty(cart.getStatus()) ? null : cart.getStatus();

		if (!ObjectUtils.isEmpty(existingCartStatus) && existingCartStatus.equals(newCartStatus))
		{
			return;
		}

		cart.setStatus(newCartStatus);
		getModelService().save(cart);

		commerceCartModification.setStatus(newCartStatus);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

}

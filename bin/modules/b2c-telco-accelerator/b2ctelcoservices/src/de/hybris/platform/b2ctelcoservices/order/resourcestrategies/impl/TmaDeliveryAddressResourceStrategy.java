/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.data.TmaAddressPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.order.validators.TmaPlaceValidator;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resource strategy implementation. Validates and updates delivery address.
 *
 * @since 1911
 */
public class TmaDeliveryAddressResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private TmaPlaceValidator placeValidator;
	private ModelService modelService;

	public TmaDeliveryAddressResourceStrategy(TmaPlaceValidator placeValidator, ModelService modelService)
	{
		this.placeValidator = placeValidator;
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter parameter)
	{
		return getPlaceValidator().validate(TmaPlaceRoleType.DELIVERY_ADDRESS, parameter);
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		if (CollectionUtils.isEmpty(commerceCartParameter.getPlaces()))
		{
			return;
		}

		final CartModel cartModel = commerceCartParameter.getCart();

		final List<TmaPlace> inputPlaces = commerceCartParameter.getPlaces();
		final List<TmaPlace> deliveryPlaces =
				inputPlaces.stream().filter(tmaPlace -> TmaPlaceRoleType.DELIVERY_ADDRESS.equals(tmaPlace.getRole()))
						.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(deliveryPlaces))
		{
			return;
		}

		final AddressModel deliveryAddress = ((TmaAddressPlace) deliveryPlaces.get(0)).getAddress();

		if (!deliveryAddress.getShippingAddress())
		{
			throw new IllegalArgumentException("The address '" + deliveryAddress.getId() + "' is not a delivery address.");
		}

		cartModel.setDeliveryAddress(deliveryAddress);
		getModelService().save(cartModel);

		commerceCartModification.setDeliveryAddress(deliveryAddress);
	}

	protected TmaPlaceValidator getPlaceValidator()
	{
		return placeValidator;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}

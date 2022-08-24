/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link AddressData}.
 *
 * @since 2007
 */
public class TmaDeliveryAddressValidator implements Validator
{
	private static final String FIELD_REQUIRED = "field.required";
	private static final String DELIVERY_ADDRESS_INVALID = "delivery.address.invalid";
	private static final String ADDRESS_ID = "id";

	@Resource(name = "deliveryService")
	private DeliveryService deliveryService;
	@Resource(name = "cartService")
	private CartService cartService;

	@Override
	public boolean supports(final Class clazz)
	{
		return AddressData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final AddressData addressData = (AddressData) target;
		Assert.notNull(errors, "Errors object must not be null");

		if (addressData == null || addressData.getId() == null || addressData.getId().trim().isEmpty())
		{
			errors.rejectValue(ADDRESS_ID, FIELD_REQUIRED);
			return;
		}

		if (!cartService.hasSessionCart())
		{
			errors.rejectValue(ADDRESS_ID, DELIVERY_ADDRESS_INVALID);
			return;
		}

		final CartModel sessionCartModel = cartService.getSessionCart();
		if (sessionCartModel == null)
		{
			errors.rejectValue(ADDRESS_ID, DELIVERY_ADDRESS_INVALID);
			return;
		}

		final List<AddressModel> addresses = deliveryService.getSupportedDeliveryAddressesForOrder(sessionCartModel, false);
		if (addresses == null)
		{
			errors.rejectValue(ADDRESS_ID, DELIVERY_ADDRESS_INVALID);
			return;
		}


		if (addresses.stream().noneMatch((AddressModel addressModel) -> addressData.getId().equals(addressModel.getId())))
		{
			errors.rejectValue(ADDRESS_ID, DELIVERY_ADDRESS_INVALID);
		}
	}
}

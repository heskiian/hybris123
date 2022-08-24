/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.validators;

import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Performs validations on different places.
 *
 * @since 1911
 */
public class TmaPlaceValidator
{
	/**
	 * @deprecated since 2011. No need for this service anymore, we don't verify anymore if the address is belonging to the cart's
	 * owner.
	 */
	@Deprecated(since = "2011")
	private TmaAddressService addressService;

	public TmaPlaceValidator(TmaAddressService addressService)
	{
		this.addressService = addressService;
	}

	public TmaCartValidationResult validate(TmaPlaceRoleType roleType, CommerceCartParameter parameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);

		final List<TmaPlace> inputPlaces = parameter.getPlaces();
		if (CollectionUtils.isEmpty(inputPlaces))
		{
			return result;
		}

		final UserModel user = parameter.getUser();
		if (user == null)
		{
			result.setValid(false);
			result.setMessage("Invalid user.");
			return result;
		}

		final List<TmaPlace> places =
				inputPlaces.stream().filter(tmaPlace -> roleType.equals(tmaPlace.getRole()))
						.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(places))
		{
			return result;
		}
		if (places.size() > 1)
		{
			result.setValid(false);
			result.setMessage("Found multiple addresses with role: " + roleType);
		}

		return result;
	}

	/**
	 * @deprecated since 2011
	 */
	@Deprecated(since = "2011")
	protected TmaAddressService getAddressService()
	{
		return addressService;
	}
}

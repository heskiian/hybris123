/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PlaceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.SubscribedProductWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link PlaceWsDTO}.
 *
 * @since 1911
 */
public class TmaCartEntryPlaceValidator implements Validator
{
	private static final String ROLE_REQUIRED = "role.required";
	private static final String INVALID_ROLE = "role.invalid";
	private static final String ROLE = "role";
	private static final String INVALID_ROLE_DETAILS = "role must be one of the allowed enum values";
	private static final String PRODUCT_PLACE = "subscribedProduct.place";

	@Override
	public boolean supports(Class<?> aClass)
	{
		return OrderEntryWsDTO.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final OrderEntryWsDTO cartEntry = (OrderEntryWsDTO) object;
		validatePlaces(cartEntry, errors);
	}

	/**
	 * Checks that all the places on product are valid.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validatePlaces(final OrderEntryWsDTO cartItem, final Errors errors)
	{
		final SubscribedProductWsDTO product = cartItem.getSubscribedProduct();
		if (product != null && CollectionUtils.isNotEmpty(product.getPlace()))
		{
			final List<PlaceWsDTO> places = product.getPlace();
			int i = 0;
			for (PlaceWsDTO place : places)
			{
				try
				{
					errors.pushNestedPath(PRODUCT_PLACE + "[" + i + "]");
					validateRole(place,errors);
				}
				finally
				{
					errors.popNestedPath();
					i++;
				}
			}
		}
	}

	/**
	 * Checks that the place has a role and that role is of type {@link TmaPlaceRoleType}.
	 *
	 * @param place
	 * 		the place
	 * @param errors
	 * 		the errors found
	 */
	protected void validateRole(final PlaceWsDTO place, final Errors errors)
	{
		if (StringUtils.isBlank(place.getRole()))
		{
			errors.rejectValue(ROLE, ROLE_REQUIRED, ROLE_REQUIRED);
			return;
		}

		try
		{
			TmaPlaceRoleType.valueOf(place.getRole());
		}
		catch (IllegalArgumentException ex)
		{
			errors.rejectValue(ROLE, INVALID_ROLE, INVALID_ROLE_DETAILS);
		}
	}
}

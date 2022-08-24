/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link Place}.
 *
 * @since 1911
 */
public class TmaPlaceValidator implements Validator
{
	private static final String ROLE_REQUIRED = "role.required";
	private static final String INVALID_ROLE = "role.invalid";
	private static final String ROLE = "role";
	private static final String INVALID_ROLE_DETAILS = "role must be one of the allowed enum values";

	@Override
	public boolean supports(Class<?> aClass)
	{
		return Place.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final Place place = (Place) object;
		validateRole(place, errors);
	}

	/**
	 * Checks that the place has a role and that role is of type {@link TmaPlaceRoleType}.
	 *
	 * @param place
	 * 		the place
	 * @param errors
	 * 		the errors found
	 */
	protected void validateRole(final Place place, final Errors errors)
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

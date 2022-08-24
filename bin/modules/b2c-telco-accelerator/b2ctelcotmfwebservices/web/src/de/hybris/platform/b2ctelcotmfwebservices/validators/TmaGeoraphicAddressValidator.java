/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link GeographicAddress}.
 *
 * @since 2003
 */
public class TmaGeoraphicAddressValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String POST_CODE = "postcode";
	private static final String COUNTRY = "country";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return GeographicAddress.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final GeographicAddress geographicAddress = (GeographicAddress) object;
		validateCountry(geographicAddress, errors);
		validatePostCode(geographicAddress, errors);

	}

	/**
	 * Checks is the country code is empty
	 *
	 * @param geographicAddress
	 *           the geographic address
	 * @param errors
	 *           the errors found
	 */
	protected void validateCountry(final GeographicAddress geographicAddress, final Errors errors)
	{
		if (StringUtils.isEmpty(geographicAddress.getCountry()))
		{
			errors.rejectValue(COUNTRY, FIELD_REQUIRED_MESSAGE_ID);
		}
	}

	/**
	 * Checks if the post code is empty
	 *
	 * @param geographicAddress
	 *           the geographic address
	 * @param errors
	 *           the errors found
	 */
	protected void validatePostCode(final GeographicAddress geographicAddress, final Errors errors)
	{
		if (StringUtils.isEmpty(geographicAddress.getPostcode()))
		{
			errors.rejectValue(POST_CODE, FIELD_REQUIRED_MESSAGE_ID);
		}
	}
}
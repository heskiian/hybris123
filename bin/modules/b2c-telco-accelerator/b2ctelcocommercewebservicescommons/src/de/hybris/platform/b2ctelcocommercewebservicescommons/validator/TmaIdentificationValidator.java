/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TmaIdentificationWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


/**
 * Validator for {@link TmaIdentificationWsDTO}
 *
 * @since 1911
 */
public class TmaIdentificationValidator implements Validator
{
	private static final String IDENTIFICATION_TYPE_FIELD_REQUIRED_MESSAGE = "tmaIdentificationType.field.required";
	private static final String IDENTIFICATION_NUMBER_FIELD_REQUIRED_MESSAGE = "tmaIdentificationNumber.field.required";
	private static final String IDENTIFICATION_NUMBER_NOT_VALID = "tmaIdentificationNumber.invalidValue";
	private static final String IDENTIFICATION_TYPE_NOT_VALID = "tmaIdentificationType.invalidValue";
	private static final String DUPLICATE_IDENTIFICATION_MESSAGE = "tmaIdentification.duplicate.message";
	private static final String REGEXP = "[A-Za-z0-9]*";
	private EnumerationService enumerationService;

	public TmaIdentificationValidator(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.equals(arg0);
	}

	/**
	 * Validates if the given {@link TmaIdentificationWsDTO} is valid
	 *
	 * @param target
	 *           the {@link TmaIdentificationWsDTO}
	 * @param errors
	 *           the list of errors
	 */
	@Override
	public void validate(final Object target, final Errors errors)
	{
		validateParameterNotNullStandardMessage("identificationList", target);
		final List<TmaIdentificationWsDTO> identificationList = (List<TmaIdentificationWsDTO>) target;
		final Map<String, String> identificationMap = new HashMap<>();
		identificationList.stream().forEach(identificationWsDTO -> {
			final String identificationType = identificationWsDTO.getIdentificationType();
			final String identificationNumber = identificationWsDTO.getIdentificationNumber();
			validateIdentificationType(identificationType, errors);
			validateIdentificationNumber(identificationNumber, errors);
			validateDuplicateIdentifications(identificationType, identificationNumber, identificationMap, errors);
		});
	}

	private void validateIdentificationType(final String identificationTypeString, final Errors errors)
	{
		if (StringUtils.isBlank(identificationTypeString))
		{
			errors.reject(IDENTIFICATION_TYPE_FIELD_REQUIRED_MESSAGE);
			return;
		}
		try
		{
			getEnumerationService().getEnumerationValue(TmaIdentificationType._TYPECODE,
						identificationTypeString);
		}
		catch (final UnknownIdentifierException ex)
		{
				errors.reject(IDENTIFICATION_TYPE_NOT_VALID, new String[]
				{ identificationTypeString }, "[{0}] is not a valid IdentificationType");
		}
	}

	private void validateIdentificationNumber(final String identificationNumber, final Errors errors)
	{
		if (StringUtils.isBlank(identificationNumber))
		{
			errors.reject(IDENTIFICATION_NUMBER_FIELD_REQUIRED_MESSAGE);
			return;
		}
		final Pattern pattern = Pattern.compile(REGEXP);
		final Matcher matcher = pattern.matcher(identificationNumber);
		if (!matcher.matches())
		{
			errors.reject(IDENTIFICATION_NUMBER_NOT_VALID, new String[]
			{ identificationNumber }, "[{0}] is not a valid IdentificationNumber");
		}
	}

	private void validateDuplicateIdentifications(final String identificationType, final String identificationNumber,
			final Map<String, String> identificationMap, final Errors errors)
	{
		if (identificationMap.containsKey(identificationType)
				&& StringUtils.equals(identificationNumber, identificationMap.get(identificationType)))
		{
			errors.reject(DUPLICATE_IDENTIFICATION_MESSAGE, new String[]
			{ identificationType, identificationNumber }, "Duplicate identification details : [{0}],[{1}]");
			return;
		}
		else
		{
			identificationMap.put(identificationType, identificationNumber);
		}
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}
}

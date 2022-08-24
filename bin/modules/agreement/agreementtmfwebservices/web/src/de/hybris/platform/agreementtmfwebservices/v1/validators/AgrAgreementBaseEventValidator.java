/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validator for {@link Agreement} related events.
 *
 * @since 2111
 */
public abstract class AgrAgreementBaseEventValidator implements Validator
{
	private static final String EVENT_ID = "eventId";

	protected static final String AGREEMENT_ID = "event.agreement.id";
	protected static final String EVENT_TYPE = "eventType";
	protected static final String FIELD_REQUIRED_MESSAGE = "field.required";
	protected static final String WRONG_EVENT_TYPE_MESSAGE = "eventType.invalid";

	public void baseValidate(final Object o, final Errors errors)
	{
		ValidationUtils.rejectIfEmpty(errors, EVENT_ID, FIELD_REQUIRED_MESSAGE);
		ValidationUtils.rejectIfEmpty(errors, EVENT_TYPE, FIELD_REQUIRED_MESSAGE);
		ValidationUtils.rejectIfEmpty(errors, AGREEMENT_ID, FIELD_REQUIRED_MESSAGE);
	}

	public void validateEventType(final String requiredEventType, final String actualEventType, final Errors errors)
	{
		if (!requiredEventType.equals(actualEventType))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}
	}
}

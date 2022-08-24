/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementStateChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementStateChangeEvent}.
 *
 * @since 2111
 */
public class AgrAgreementStateChangeEventValidator extends AgrAgreementBaseEventValidator
{
	private static final String EVENT_TYPE_STATE_UPDATE = "AgreementStateChangeEvent";
	private static final String STATUS = "event.agreement.status";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementStateChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementStateChangeEvent agreementStateChangeEvent = (AgreementStateChangeEvent) o;

		validateEventType(EVENT_TYPE_STATE_UPDATE, agreementStateChangeEvent.getEventType(), errors);

		if (agreementStateChangeEvent.getEvent().getAgreement().getStatus() == null)
		{
			errors.rejectValue(STATUS, FIELD_REQUIRED_MESSAGE);
		}
	}
}


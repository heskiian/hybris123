/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationStateChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementSpecificationStateChangeEvent}.
 *
 * @since 2111
 */
public class AgrAgreementSpecStateChangeEventValidator extends AgrAgreementSpecBaseEventValidator
{
	private static final String EVENT_TYPE_STATE_UPDATE = "AgreementSpecificationStateChangeEvent";
	private static final String STATUS = "event.agreementSpecification.lifecycleStatus";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementSpecificationStateChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementSpecificationStateChangeEvent agreementSpecificationStateChangeEvent = (AgreementSpecificationStateChangeEvent) o;

		validateEventType(EVENT_TYPE_STATE_UPDATE, agreementSpecificationStateChangeEvent.getEventType(), errors);

		if (agreementSpecificationStateChangeEvent.getEvent().getAgreementSpecification().getLifecycleStatus() == null)
		{
			errors.rejectValue(STATUS, FIELD_REQUIRED_MESSAGE);
		}
	}
}

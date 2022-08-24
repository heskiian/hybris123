/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementDeleteEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementDeleteEvent}
 *
 * @since 2111
 */
public class AgrAgreementDeleteEventValidator extends AgrAgreementBaseEventValidator
{
	private static final String EVENT_TYPE_DELETE = "AgreementDeleteEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementDeleteEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementDeleteEvent agreementDeleteEvent = (AgreementDeleteEvent) o;
		validateEventType(EVENT_TYPE_DELETE, agreementDeleteEvent.getEventType(), errors);
	}
}


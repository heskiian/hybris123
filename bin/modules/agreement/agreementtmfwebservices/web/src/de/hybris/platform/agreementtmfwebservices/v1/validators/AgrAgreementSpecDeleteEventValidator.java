/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;


import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationDeleteEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementSpecificationDeleteEvent}
 *
 * @since 2111
 */
public class AgrAgreementSpecDeleteEventValidator extends AgrAgreementSpecBaseEventValidator
{
	private static final String EVENT_TYPE_DELETE = "AgreementSpecificationDeleteEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementSpecificationDeleteEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementSpecificationDeleteEvent agreementSpecificationDeleteEvent = (AgreementSpecificationDeleteEvent) o;
		validateEventType(EVENT_TYPE_DELETE, agreementSpecificationDeleteEvent.getEventType(), errors);
	}
}

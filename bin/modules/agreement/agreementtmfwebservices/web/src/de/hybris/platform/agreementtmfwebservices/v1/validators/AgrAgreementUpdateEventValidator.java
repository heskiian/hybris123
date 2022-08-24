/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementAttributeValueChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementAttributeValueChangeEvent}.
 *
 * @since 2111
 */
public class AgrAgreementUpdateEventValidator extends AgrAgreementBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "AgreementAttributeValueChangeEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementAttributeValueChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementAttributeValueChangeEvent agreementAttributeValueChangeEvent = (AgreementAttributeValueChangeEvent) o;
		validateEventType(EVENT_TYPE_UPDATE, agreementAttributeValueChangeEvent.getEventType(), errors);
	}
}

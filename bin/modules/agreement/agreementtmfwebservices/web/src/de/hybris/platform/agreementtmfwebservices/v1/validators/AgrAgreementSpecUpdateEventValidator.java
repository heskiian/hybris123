/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationAttributeValueChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementSpecificationAttributeValueChangeEvent}.
 *
 * @since 2111
 */
public class AgrAgreementSpecUpdateEventValidator extends AgrAgreementSpecBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "AgreementSpecificationAttributeValueChangeEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementSpecificationAttributeValueChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementSpecificationAttributeValueChangeEvent agreementSpecificationAttributeValueChangeEvent = (AgreementSpecificationAttributeValueChangeEvent) o;
		validateEventType(EVENT_TYPE_UPDATE, agreementSpecificationAttributeValueChangeEvent.getEventType(), errors);
	}
}

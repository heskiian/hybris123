/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationCreateEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link AgreementSpecificationCreateEvent}.
 *
 * @since 2111.
 */
public class AgrAgreementSpecCreateEventValidator extends AgrAgreementSpecBaseEventValidator
{
	private static final String EVENT_TYPE_CREATE = "AgreementSpecificationCreateEvent";
	private static final String AGREEMENT_SPEC_EXISTS_MESSAGE = "agreementSpecification.id.exists";
	private AgrGenericService agrGenericService;

	public AgrAgreementSpecCreateEventValidator(final AgrGenericService agrGenericService)
	{
		this.agrGenericService = agrGenericService;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementSpecificationCreateEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final AgreementSpecificationCreateEvent agreementSpecificationCreateEvent = (AgreementSpecificationCreateEvent) o;
		final AgreementSpecification agreementSpecification = agreementSpecificationCreateEvent.getEvent()
				.getAgreementSpecification();

		validateEventType(EVENT_TYPE_CREATE, agreementSpecificationCreateEvent.getEventType(), errors);

		if (getAgrGenericService().getItem(AgrAgreementSpecificationModel._TYPECODE, agreementSpecification.getId()) != null)
		{
			errors.rejectValue(AGREEMENT_SPEC_ID, AGREEMENT_SPEC_EXISTS_MESSAGE);
		}

	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementCreateEvent;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


/**
 * Validator for {@link AgreementCreateEvent}.
 *
 * @since 2111.
 */
public class AgrAgreementCreateEventValidator extends AgrAgreementBaseEventValidator
{
	private static final String AGREEMENT_TYPE = "event.agreement.agreementType";
	private static final String EVENT_TYPE_CREATE = "AgreementCreateEvent";
	private static final String AGREEMENT_EXISTS_MESSAGE = "agreement.id.exists";
	private AgrGenericService agrGenericService;

	public AgrAgreementCreateEventValidator(final AgrGenericService agrGenericService)
	{
		this.agrGenericService = agrGenericService;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AgreementCreateEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);
		ValidationUtils.rejectIfEmpty(errors, AGREEMENT_TYPE, FIELD_REQUIRED_MESSAGE);

		final AgreementCreateEvent agreementCreateEvent = (AgreementCreateEvent) o;
		final Agreement agreement = agreementCreateEvent.getEvent().getAgreement();

		validateEventType(EVENT_TYPE_CREATE, agreementCreateEvent.getEventType(), errors);

		if (getAgrGenericService().getItem(AgrAgreementModel._TYPECODE, agreement.getId()) != null)
		{
			errors.rejectValue(AGREEMENT_ID, AGREEMENT_EXISTS_MESSAGE);
		}
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}


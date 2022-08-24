/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link AgreementSpecification}.
 *
 * @since 2111
 */
public class AgrAgreementSpecificationCreateValidator implements Validator
{
	protected static final String ATTACHMENT = "attachment";
	protected static final String NAME = "name";
	protected static final String FIELD_REQUIRED_MESSAGE = "field.required";
	protected static final String AGREEMENT_SPECIFICATION_ID = "id";
	private static final String AGREEMENT_SPECIFICATION_EXISTS_MESSAGE = "agreementSpecification.id.exists";
	private AgrGenericService agrGenericService;

	public AgrAgreementSpecificationCreateValidator(final AgrGenericService agrGenericService)
	{
		this.agrGenericService = agrGenericService;
	}

	@Override
	public final boolean supports(final Class<?> aClass)
	{
		return AgreementSpecification.class.equals(aClass);
	}

	@Override
	public final void validate(final Object o, final Errors errors)
	{
		final AgreementSpecification agreementSpecification = (AgreementSpecification) o;

		if (StringUtils.isNotEmpty(agreementSpecification.getId())
				&& getAgrGenericService().getItem(AgrAgreementSpecificationModel._TYPECODE, agreementSpecification.getId()) != null)
		{
			errors.rejectValue(AGREEMENT_SPECIFICATION_ID, AGREEMENT_SPECIFICATION_EXISTS_MESSAGE);
		}

		if (agreementSpecification.getAttachment() == null)
		{
			errors.rejectValue(ATTACHMENT, FIELD_REQUIRED_MESSAGE);
		}
		if (agreementSpecification.getName() == null)
		{
			errors.rejectValue(NAME, FIELD_REQUIRED_MESSAGE);
		}
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}

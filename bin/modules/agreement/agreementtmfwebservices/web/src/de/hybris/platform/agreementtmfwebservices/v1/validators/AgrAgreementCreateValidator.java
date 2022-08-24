/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.validators;

import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link Agreement}.
 *
 * @since 2111
 */
public class AgrAgreementCreateValidator implements Validator
{
	protected static final String AGREEMENT_TYPE = "agreementType";
	protected static final String ENGAGED_PARTY = "engagedParty";
	protected static final String AGREEMENT_ITEM = "agreementItem";
	protected static final String FIELD_REQUIRED_MESSAGE = "field.required";
	protected static final String AGREEMENT_ID = "id";
	private static final String AGREEMENT_EXISTS_MESSAGE = "agreement.id.exists";
	private AgrGenericService agrGenericService;

	public AgrAgreementCreateValidator(final AgrGenericService agrGenericService)
	{
		this.agrGenericService = agrGenericService;
	}

	@Override
	public final boolean supports(final Class<?> aClass)
	{
		return Agreement.class.equals(aClass);
	}

	@Override
	public final void validate(final Object o, final Errors errors)
	{
		final Agreement agreement = (Agreement) o;

		if (StringUtils.isNotEmpty(agreement.getId())
				&& getAgrGenericService().getItem(AgrAgreementModel._TYPECODE, agreement.getId()) != null)
		{
			errors.rejectValue(AGREEMENT_ID, AGREEMENT_EXISTS_MESSAGE);
		}

		if (agreement.getAgreementType() == null)
		{
			errors.rejectValue(AGREEMENT_TYPE, FIELD_REQUIRED_MESSAGE);
		}
		if (agreement.getEngagedParty() == null)
		{
			errors.rejectValue(ENGAGED_PARTY, FIELD_REQUIRED_MESSAGE);
		}
		if (agreement.getAgreementItem() == null)
		{
			errors.rejectValue(AGREEMENT_ITEM, FIELD_REQUIRED_MESSAGE);
		}
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}

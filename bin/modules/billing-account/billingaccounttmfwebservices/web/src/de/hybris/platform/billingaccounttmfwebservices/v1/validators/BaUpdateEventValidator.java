/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.validators;


import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccountAttributeValueChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link BillingAccountAttributeValueChangeEvent}.
 *
 * @since 2111
 */
public class BaUpdateEventValidator extends BaBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "BillingAccountAttributeValueChangeEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return BillingAccountAttributeValueChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final BillingAccountAttributeValueChangeEvent billingAccountAttributeValueChangeEvent = (BillingAccountAttributeValueChangeEvent) o;
		if (!EVENT_TYPE_UPDATE.equals(billingAccountAttributeValueChangeEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}
	}
}


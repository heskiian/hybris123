/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.validators;

import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccountStateChangeEvent;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


/**
 * Validator for {@link BillingAccountStateChangeEvent}.
 *
 * @since 2111
 */
public class BaStateChangeEventValidator extends BaBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "BillingAccountStateChangeEvent";
	private static final String STATUS = "payload.billingAccount.state";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return BillingAccountStateChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final BillingAccountStateChangeEvent billingAccountStateChangeEvent = (BillingAccountStateChangeEvent) o;

		if (!EVENT_TYPE_UPDATE.equals(billingAccountStateChangeEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}

		if (StringUtils.isEmpty(billingAccountStateChangeEvent.getPayload().getBillingAccount().getState()))
		{
			errors.rejectValue(STATUS, FIELD_REQUIRED_MESSAGE);
		}
	}
}

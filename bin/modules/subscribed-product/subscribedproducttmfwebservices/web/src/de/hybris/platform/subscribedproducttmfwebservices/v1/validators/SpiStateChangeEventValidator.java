/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.validators;

import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductStateChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link ProductStateChangeEvent}.
 *
 * @since 2111
 */
public class SpiStateChangeEventValidator extends SpiBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "ProductStateChangeEvent";
	private static final String STATUS = "event.product.status";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ProductStateChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final ProductStateChangeEvent productStateChangeEvent = (ProductStateChangeEvent) o;

		if (!EVENT_TYPE_UPDATE.equals(productStateChangeEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}

		if (productStateChangeEvent.getEvent().getProduct().getStatus() == null)
		{
			errors.rejectValue(STATUS, FIELD_REQUIRED_MESSAGE);
		}
	}
}

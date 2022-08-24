/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.validators;

import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductDeleteEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link ProductDeleteEvent}
 *
 * @since 2111
 */
public class SpiDeleteEventValidator extends SpiBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "ProductDeleteEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ProductDeleteEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final ProductDeleteEvent productDeleteEvent = (ProductDeleteEvent) o;
		if (!EVENT_TYPE_UPDATE.equals(productDeleteEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}
	}
}

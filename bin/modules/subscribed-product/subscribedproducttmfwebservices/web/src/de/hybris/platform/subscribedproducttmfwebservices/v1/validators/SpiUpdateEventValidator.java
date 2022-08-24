/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.validators;

import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductAttributeValueChangeEvent;

import org.springframework.validation.Errors;


/**
 * Validator for {@link ProductAttributeValueChangeEvent}.
 *
 * @since 2111
 */
public class SpiUpdateEventValidator extends SpiBaseEventValidator
{
	private static final String EVENT_TYPE_UPDATE = "ProductAttributeValueChangeEvent";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ProductAttributeValueChangeEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);

		final ProductAttributeValueChangeEvent productAttributeValueChangeEvent = (ProductAttributeValueChangeEvent) o;
		if (!EVENT_TYPE_UPDATE.equals(productAttributeValueChangeEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}
	}
}

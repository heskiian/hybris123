/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.validators;


import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductCreateEvent;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


/**
 * Validator for {@link ProductCreateEvent}.
 *
 * @since 2111.
 */
public class SpiCreateEventValidator extends SpiBaseEventValidator
{
	private static final String PRODUCT_IS_BUNDLE = "event.product.isBundle";
	private static final String PRODUCT_STATUS = "event.product.status";
	private static final String EVENT_TYPE_CREATE = "ProductCreateEvent";
	private static final String PRODUCT_EXISTS_MESSAGE = "product.id.exists";
	private SpiGenericService spiGenericService;

	public SpiCreateEventValidator(final SpiGenericService spiGenericService)
	{
		this.spiGenericService = spiGenericService;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ProductCreateEvent.class.equals(aClass);
	}

	@Override
	public void validate(final Object o, final Errors errors)
	{
		baseValidate(o, errors);
		ValidationUtils.rejectIfEmpty(errors, PRODUCT_STATUS, FIELD_REQUIRED_MESSAGE);

		final ProductCreateEvent productCreateEvent = (ProductCreateEvent) o;
		final Product product = productCreateEvent.getEvent().getProduct();

		if (getSpiGenericService().getItem(SpiProductModel._TYPECODE, product.getId()) != null)
		{
			errors.rejectValue(PRODUCT_ID, PRODUCT_EXISTS_MESSAGE);
		}

		if (product.isIsBundle() == null)
		{
			errors.rejectValue(PRODUCT_IS_BUNDLE, FIELD_REQUIRED_MESSAGE);
		}

		if (!EVENT_TYPE_CREATE.equals(productCreateEvent.getEventType()))
		{
			errors.rejectValue(EVENT_TYPE, WRONG_EVENT_TYPE_MESSAGE);
		}
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}

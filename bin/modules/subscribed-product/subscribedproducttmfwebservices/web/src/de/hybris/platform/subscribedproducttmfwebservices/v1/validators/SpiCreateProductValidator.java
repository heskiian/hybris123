/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproducttmfwebservices.v1.validators;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



/**
 * Validator for POST /product API.
 *
 * @since 2111.
 */
public class SpiCreateProductValidator implements Validator
{
	private static final String PRODUCT_STATUS = "status";
	private static final String PRODUCT_ID = "id";
	private static final String FIELD_REQUIRED_MESSAGE = "field.required";
	private static final String MINIMUM_ONE_FIELD_REQUIRED_MESSAGE = "minimum.one.field.required";
	private static final String PRODUCT_OFFERING = "productOffering";
	private static final String PRODUCT_SPECIFICATION = "productSpecification";
	private static final String PRODUCT_EXISTS_MESSAGE = "product.id.exists";
	private SpiGenericService spiGenericService;

	public SpiCreateProductValidator(final SpiGenericService spiGenericService)
	{
		this.spiGenericService = spiGenericService;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return Product.class.equals(aClass);
	}

	/**
	 * Checks that the {@link Product} has valid id, status and specification.
	 *
	 * @param o
	 * 		the subscribed product
	 * @param errors
	 * 		the errors
	 */
	@Override
	public void validate(final Object o, final Errors errors)
	{
		final Product product = (Product) o;

		if (getSpiGenericService().getItem(SpiProductModel._TYPECODE, product.getId()) != null)
		{
			errors.rejectValue(PRODUCT_ID, PRODUCT_EXISTS_MESSAGE);
		}

		if (product.getStatus() == null)
		{
			errors.rejectValue(PRODUCT_STATUS, FIELD_REQUIRED_MESSAGE);
		}

		if (product.getProductOffering() == null && product.getProductSpecification() == null)
		{
			errors.reject(MINIMUM_ONE_FIELD_REQUIRED_MESSAGE, new String[]
					{ PRODUCT_OFFERING, PRODUCT_SPECIFICATION }, null);
		}
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}

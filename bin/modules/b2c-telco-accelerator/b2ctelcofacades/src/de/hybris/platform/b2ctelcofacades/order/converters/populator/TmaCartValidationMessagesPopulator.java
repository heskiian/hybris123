/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Populator for {@link CartModel} attribute validation messages
 *
 * @since 1911
 */
public class TmaCartValidationMessagesPopulator implements Populator<CartModel, CartData>
{

	private Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter;

	public TmaCartValidationMessagesPopulator(
			Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter)
	{
		this.cartValidationConverter = cartValidationConverter;
	}

	@Override
	public void populate(CartModel cartModel, CartData cartData) throws ConversionException
	{
		final Set<TmaCartValidationModel> cartValidations = cartModel.getCartValidations();
		final Set<TmaCartValidationData> cartValidationData = cartValidations.stream()
				.map(cartValidation -> getCartValidationConverter().convert(cartValidation)).collect(Collectors.toSet());
		cartData.setValidationMessages(cartValidationData);
	}

	protected Converter<TmaCartValidationModel, TmaCartValidationData> getCartValidationConverter()
	{
		return cartValidationConverter;
	}
}

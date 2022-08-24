/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populator for {@link CartModel} attribute invalid messages
 *
 * @since 1911
 * @deprecated since 1911, use {@link TmaCartValidationMessagesPopulator} instead
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaCartInvalidMessagesPopulator implements Populator<CartModel, CartData>
{
	@Override
	public void populate(CartModel cartModel, CartData cartData) throws ConversionException
	{
		cartData.setInvalidMessages(cartModel.getInvalidMessages());
	}
}

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Message;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for validation error attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1907
 * @deprecated since 1911. Use {@link ShoppingCartValidationMessagesAttributeMapper} mapper instead
 */
@Deprecated(since = "1911", forRemoval= true)
public class ShoppingCartMessageAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{

	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		if (source.getInvalidMessages() == null)
		{
			return;
		}
		final List<Message> messages = new ArrayList<>();
		source.getInvalidMessages().forEach((String data) ->
		{
			final Message message = getMapperFacade().map(data, Message.class, context);
			messages.add(message);
		});

		target.setMessage(messages);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

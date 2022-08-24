/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ValidationItem;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for validation messages attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1911
 */
public class ShoppingCartValidationMessagesAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	public ShoppingCartValidationMessagesAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		final Set<TmaCartValidationData> validationMessages = source.getValidationMessages();
		if (CollectionUtils.isEmpty(validationMessages))
		{
			return;
		}

		setValidationMessages(target, context, validationMessages);
	}

	protected void setValidationMessages(ShoppingCart target, MappingContext context,
			Set<TmaCartValidationData> validationMessages)
	{
		final List<ValidationItem> validationDataList = validationMessages.stream()
				.map(invalidMessage -> getMapperFacade().map(invalidMessage, ValidationItem.class, context))
				.collect(Collectors.toList());
		target.setValidation(validationDataList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

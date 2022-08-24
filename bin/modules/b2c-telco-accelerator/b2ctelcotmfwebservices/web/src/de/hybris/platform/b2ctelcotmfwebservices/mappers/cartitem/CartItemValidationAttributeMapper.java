/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ValidationItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps validation attribute between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1911
 */
public class CartItemValidationAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	private MapperFacade mapperFacade;

	public CartItemValidationAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target, MappingContext context)
	{
		final Set<TmaCartValidationData> validationMessages = source.getValidationMessages();
		if (CollectionUtils.isNotEmpty(validationMessages))
		{
			final List<ValidationItem> validationDataSet = validationMessages.stream()
					.map(invalidMessage -> getMapperFacade().map(invalidMessage, ValidationItem.class, context))
					.collect(Collectors.toList());
			target.setValidation(validationDataSet);
		}

	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

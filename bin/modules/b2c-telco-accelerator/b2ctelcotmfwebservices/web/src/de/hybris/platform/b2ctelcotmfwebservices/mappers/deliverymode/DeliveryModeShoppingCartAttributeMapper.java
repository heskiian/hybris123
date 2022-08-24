/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.deliverymode;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryMode;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCartRef;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for shopping cart attribute between {@link DeliveryModeData} and {@link DeliveryMode}
 *
 * @since 1907
 */
public class DeliveryModeShoppingCartAttributeMapper extends TmaAttributeMapper<DeliveryModeData, DeliveryMode>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final DeliveryModeData source, final DeliveryMode target,
			final MappingContext context)
	{
		if (source.getCart() != null)
		{
			target.setShoppingCart(getMapperFacade().map(source.getCart(), ShoppingCartRef.class, context));
		}
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

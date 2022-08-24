/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for place attribute between {@link CartData} and {@link ShoppingCart}
 *
 * @since 1911
 */
public class ShoppingCartPlaceAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	private static final String PLACE_ROLE = "placeRole";
	/**
	 * Mapper facade
	 */
	private final MapperFacade mapperFacade;

	public ShoppingCartPlaceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final CartData source, final ShoppingCart target,
			final MappingContext context)
	{
		final List<Place> places = CollectionUtils.isNotEmpty(target.getPlace()) ? target.getPlace() : new ArrayList<>();
		if (source.getDeliveryAddress() != null)
		{
			context.setProperty(PLACE_ROLE, TmaPlaceRoleType.DELIVERY_ADDRESS);
			final Place place = getMapperFacade().map(source.getDeliveryAddress(), Place.class, context);
			places.add(place);
		}

		if (source.getBillingAddress() != null)
		{
			context.setProperty(PLACE_ROLE, TmaPlaceRoleType.BILLING_ADDRESS);
			final Place place = getMapperFacade().map(source.getBillingAddress(), Place.class, context);
			places.add(place);
		}

		target.setPlace(CollectionUtils.isNotEmpty(places) ? places : null);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

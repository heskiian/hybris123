/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.order.data.OrderData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute mapper class maps data for place attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 2102
 */
public class ProductOrderPlaceAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	private static final String PLACE_ROLE = "placeRole";

	private final MapperFacade mapperFacade;

	public ProductOrderPlaceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
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

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}

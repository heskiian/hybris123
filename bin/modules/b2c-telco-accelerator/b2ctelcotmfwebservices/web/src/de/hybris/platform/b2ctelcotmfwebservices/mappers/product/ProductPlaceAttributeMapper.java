/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.product;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for place attribute between {@link OrderEntryData} and {@link Product}
 *
 * @since 1911
 */
public class ProductPlaceAttributeMapper extends TmaAttributeMapper<OrderEntryData, Product>
{
	private static final String PLACE_ROLE = "placeRole";

	private final MapperFacade mapperFacade;

	public ProductPlaceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final Product target,
			final MappingContext context)
	{
		final List<Place> places = CollectionUtils.isNotEmpty(target.getPlace()) ? target.getPlace() : new ArrayList<>();
		if (source.getInstallationAddress() != null)
		{
			context.setProperty(PLACE_ROLE, TmaPlaceRoleType.INSTALLATION_ADDRESS);
			final Place place = getMapperFacade().map(source.getInstallationAddress(), Place.class, context);
			places.add(place);

		}
		if (!ObjectUtils.isEmpty(source.getRegion()))
		{
			context.setProperty("placeRole", TmaPlaceRoleType.PRODUCT_REGION);
			final Place place = getMapperFacade().map(source.getRegion(), Place.class, context);
			places.add(place);
		}
		target.setPlace(CollectionUtils.isNotEmpty(places) ? places : null);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

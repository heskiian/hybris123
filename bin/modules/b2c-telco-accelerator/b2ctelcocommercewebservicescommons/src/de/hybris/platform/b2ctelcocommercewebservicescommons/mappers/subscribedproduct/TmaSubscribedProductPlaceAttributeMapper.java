/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.subscribedproduct;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PlaceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.SubscribedProductWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for place attribute between {@link OrderEntryData} and
 * {@link SubscribedProductWsDTO}
 *
 * @since 1911
 */
public class TmaSubscribedProductPlaceAttributeMapper extends TmaAttributeMapper<OrderEntryData, SubscribedProductWsDTO>
{
	private static final String PLACE_ROLE = "placeRole";

	private final MapperFacade mapperFacade;

	public TmaSubscribedProductPlaceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final SubscribedProductWsDTO target,
			final MappingContext context)
	{
		final List<PlaceWsDTO> places = CollectionUtils.isNotEmpty(target.getPlace()) ? target.getPlace() : new ArrayList<>();
		if (source.getInstallationAddress() != null)
		{
			context.setProperty(PLACE_ROLE, TmaPlaceRoleType.INSTALLATION_ADDRESS);
			final PlaceWsDTO place = getMapperFacade().map(source.getInstallationAddress(), PlaceWsDTO.class, context);
			places.add(place);
		}

		if (!ObjectUtils.isEmpty(source.getRegion()))
		{
			context.setProperty(PLACE_ROLE, TmaPlaceRoleType.PRODUCT_REGION);
			places.add(getMapperFacade().map(source.getRegion(), PlaceWsDTO.class, context));
		}
		target.setPlace(CollectionUtils.isNotEmpty(places) ? places : null);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

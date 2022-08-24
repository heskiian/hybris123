/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.place;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PlaceWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.commercefacades.user.data.RegionData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for Role attribute between {@link RegionData} and {@link PlaceWsDTO}
 *
 * @since 2003
 */
public class TmaRegionPlaceRoleAttributeMapper extends TmaAttributeMapper<RegionData, PlaceWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final RegionData source, final PlaceWsDTO target,
			final MappingContext context)
	{
		target.setRole(TmaPlaceRoleType.PRODUCT_REGION.name());
	}
}

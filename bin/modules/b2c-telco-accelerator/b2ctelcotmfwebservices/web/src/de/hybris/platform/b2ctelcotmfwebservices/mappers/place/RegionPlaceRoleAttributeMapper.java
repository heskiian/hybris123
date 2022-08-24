/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.place;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.commercefacades.user.data.RegionData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ID attribute between {@link RegionData} and {@link Place}
 *
 * @since 2003
 */
public class RegionPlaceRoleAttributeMapper extends TmaAttributeMapper<RegionData, Place>
{

	@Override
	public void populateTargetAttributeFromSource(final RegionData source, final Place target,
			final MappingContext context)
	{
		target.setRole(TmaPlaceRoleType.PRODUCT_REGION.name());
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.place;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PlaceWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.RegionData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for Name attribute between {@link RegionData} and {@link PlaceWsDTO}
 *
 * @since 2003
 */
public class TmaRegionPlaceNameAttributeMapper extends TmaAttributeMapper<RegionData, PlaceWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final RegionData source, final PlaceWsDTO target, final MappingContext context)
	{
		if (StringUtils.isNotBlank(source.getName()))
		{
			target.setName(source.getName());
		}
	}
}

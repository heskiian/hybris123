/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.place;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.commercefacades.user.data.RegionData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ID attribute between {@link RegionData} and {@link Place}
 *
 * @since 2003
 */
public class RegionPlaceIdAttributeMapper extends TmaAttributeMapper<RegionData, Place>
{

	@Override
	public void populateTargetAttributeFromSource(final RegionData source, final Place target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getIsocode()))
		{
			target.setId(source.getIsocode());
		}
	}
}

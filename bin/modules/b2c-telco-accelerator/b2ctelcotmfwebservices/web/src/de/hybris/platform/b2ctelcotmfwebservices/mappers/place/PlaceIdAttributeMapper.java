/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.place;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.commercefacades.user.data.AddressData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link AddressData} and {@link Place}
 *
 * @since 1911
 */
public class PlaceIdAttributeMapper extends TmaAttributeMapper<AddressData, Place>
{
	@Override
	public void populateTargetAttributeFromSource(AddressData source, Place target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}

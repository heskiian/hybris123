/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.place;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PlaceWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.AddressData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link AddressData} and
 * {@link PlaceWsDTO}
 *
 * @since 1911
 */
public class TmaPlaceIdAttributeMapper extends TmaAttributeMapper<AddressData, PlaceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final PlaceWsDTO target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}
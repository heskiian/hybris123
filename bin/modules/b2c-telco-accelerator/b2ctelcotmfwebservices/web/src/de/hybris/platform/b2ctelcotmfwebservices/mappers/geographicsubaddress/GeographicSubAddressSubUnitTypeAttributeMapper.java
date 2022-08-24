/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicsubaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicSubAddress;
import de.hybris.platform.commercefacades.user.data.AddressData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for SubUnitType and SubUnitNumber attribute between {@link AddressData} and
 * {@link GeographicSubAddress}
 *
 * @since 2007
 */
public class GeographicSubAddressSubUnitTypeAttributeMapper extends TmaAttributeMapper<AddressData, GeographicSubAddress>
{
	public static final int DEFAULT_APARTMENT_LENGTH = 2;

	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final GeographicSubAddress target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getApartment()))
		{
			final String[] apartment = source.getApartment().split(" ", DEFAULT_APARTMENT_LENGTH);
			target.setSubUnitType(apartment[0]);
			if (apartment.length >= DEFAULT_APARTMENT_LENGTH)
			{
				target.setSubUnitNumber(apartment[1]);
			}
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final GeographicSubAddress target, final AddressData source,
			final MappingContext context)
	{
		if (StringUtils.isNotBlank(target.getSubUnitType()) || StringUtils.isNotBlank(target.getSubUnitNumber()))
		{
			source.setApartment((StringUtils.isNotEmpty(target.getSubUnitType()) ? (target.getSubUnitType()) : "") + " "
					+ (StringUtils.isNotEmpty(target.getSubUnitNumber()) ? (target.getSubUnitNumber()) : ""));
		}
	}
}

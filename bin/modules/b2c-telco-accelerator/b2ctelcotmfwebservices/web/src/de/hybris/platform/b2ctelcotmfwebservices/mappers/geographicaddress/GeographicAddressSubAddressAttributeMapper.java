/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicSubAddress;
import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for GeographicSubAddress attribute between {@link AddressData} and
 * {@link GeographicAddress}
 *
 * @since 2007
 */
public class GeographicAddressSubAddressAttributeMapper extends TmaAttributeMapper<AddressData, GeographicAddress>
{
	private final MapperFacade mapperFacade;

	public GeographicAddressSubAddressAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final GeographicAddress target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getBuilding()) || StringUtils.isNotEmpty(source.getApartment()))
		{
			final List<GeographicSubAddress> subAddresses = new ArrayList<>();

			final GeographicSubAddress address = getMapperFacade().map(source, GeographicSubAddress.class, context);
			subAddresses.add(address);
			target.setGeographicSubAddress(subAddresses);
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final GeographicAddress target, final AddressData source, final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(target.getGeographicSubAddress()))
		{
			getMapperFacade().map(target.getGeographicSubAddress().get(0), source, context);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

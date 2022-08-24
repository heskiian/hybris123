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
 * This attribute Mapper class maps data for Building attribute between {@link AddressData} and
 * {@link GeographicSubAddress}
 *
 * @since 2007
 */
public class GeographicSubAddressBuildingAttributeMapper extends TmaAttributeMapper<AddressData, GeographicSubAddress>
{
	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final GeographicSubAddress target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getBuilding()))
		{
			target.setBuildingName(source.getBuilding());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final GeographicSubAddress target, final AddressData source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getBuildingName()))
		{
			source.setBuilding(target.getBuildingName());
		}
	}
}

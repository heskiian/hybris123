/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;
import de.hybris.platform.commercefacades.user.data.AddressData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isUnloadingAddress attribute between {@link AddressData} and
 * {@link GeographicAddress}
 *
 * @since 1907
 */
public class GeographicAddressIsUnloadingAddressAttributeMapper extends TmaAttributeMapper<AddressData, GeographicAddress>
{
	@Override
	public void populateTargetAttributeFromSource(AddressData source, GeographicAddress target, MappingContext context)
	{
		target.isUnloadingAddress(source.isUnloadingAddress());
	}


	@Override
	public void populateSourceAttributeFromTarget(GeographicAddress target, AddressData source, MappingContext context)
	{
		if (target.isIsUnloadingAddress() != null)
		{
			source.setUnloadingAddress(target.isIsUnloadingAddress());
		}
	}
}


/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;
import de.hybris.platform.commercefacades.user.data.RegionData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for locality attribute between {@link AddressData} and {@link GeographicAddress}
 *
 * @since 1907
 */
public class GeographicAddressStateOfProvinceAttributeMapper extends TmaAttributeMapper<AddressData, GeographicAddress>
{
	@Override
	public void populateTargetAttributeFromSource(AddressData source, GeographicAddress target, MappingContext context)
	{
		if (source.getRegion() != null)
		{
			target.setStateOfProvince(source.getRegion().getIsocode());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(GeographicAddress target, AddressData source,
			MappingContext context)
	{
		if (target.getStateOfProvince() == null)
		{
			return;
		}

		RegionData regionData = new RegionData();
		regionData.setIsocode(target.getStateOfProvince());
		source.setRegion(regionData);
	}
}

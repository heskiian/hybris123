/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for postCode attribute between {@link AddressData} and {@link GeographicAddress}
 *
 * @since 1907
 */
public class GeographicAddressPostCodeAttributeMapper extends TmaAttributeMapper<AddressData, GeographicAddress>
{
	@Override
	public void populateTargetAttributeFromSource(AddressData source, GeographicAddress target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getPostalCode()))
		{
			target.setPostcode(source.getPostalCode());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(GeographicAddress target, AddressData source,
			MappingContext context)
	{
		if (target.getPostcode() != null)
		{
			source.setPostalCode(target.getPostcode());
		}
	}
}

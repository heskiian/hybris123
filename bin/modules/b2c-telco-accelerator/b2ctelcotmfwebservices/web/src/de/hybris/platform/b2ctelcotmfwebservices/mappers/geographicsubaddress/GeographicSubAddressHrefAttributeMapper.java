/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicsubaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicSubAddress;
import de.hybris.platform.commercefacades.user.data.AddressData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link AddressData} and {@link GeographicSubAddress}
 *
 * @since 1907
 */
public class GeographicSubAddressHrefAttributeMapper extends TmaAttributeMapper<AddressData, GeographicSubAddress>
{
	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final GeographicSubAddress target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.ADDRESS_API_URL + source.getCode());
		}
	}
}

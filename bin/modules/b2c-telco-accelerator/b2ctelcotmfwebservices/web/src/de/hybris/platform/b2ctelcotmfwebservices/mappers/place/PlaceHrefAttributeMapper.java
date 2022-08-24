/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.place;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.commercefacades.user.data.AddressData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link AddressData} and {@link Place}
 *
 * @since 1911
 */
public class PlaceHrefAttributeMapper extends TmaAttributeMapper<AddressData, Place>
{

	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final Place target,
			final MappingContext context)
	{
		target.setHref(B2ctelcotmfwebservicesConstants.ADDRESS_API_URL + source.getCode());
	}
}

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.place;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.util.Config;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link AddressData} and {@link Place}
 *
 * @since 1911
 */
public class PlaceAtReferredTypeAttributeMapper extends TmaAttributeMapper<AddressData, Place>
{

	@Override
	public void populateTargetAttributeFromSource(final AddressData source, final Place target,
			final MappingContext context)
	{
		target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.PLACE_DEFAULT_REFFERED));
	}
}

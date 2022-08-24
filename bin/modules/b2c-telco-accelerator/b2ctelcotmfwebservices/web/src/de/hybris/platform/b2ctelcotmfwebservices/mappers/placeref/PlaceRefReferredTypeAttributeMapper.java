/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.placeref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PlaceRef;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.util.Config;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atreferredType attribute between {@link RegionData} and
 * {@link PlaceRef}
 *
 * @since 1907
 */
public class PlaceRefReferredTypeAttributeMapper extends TmaAttributeMapper<RegionData, PlaceRef>
{

	@Override
	public void populateTargetAttributeFromSource(final RegionData source, final PlaceRef target,
			final MappingContext context)
	{
		target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_PLACE_REF_DEFAULT_REFERRED));
	}
}

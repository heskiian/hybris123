/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaPlaceData;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.converters.Populator;


/**
 * Populator implementation for {@link TmaPlaceData} as source and
 * {@link TmaRegionPlace} as target type, handles region information.
 *
 * @since 2003
 */
public class TmaRegionPlacePopulator implements Populator<TmaPlaceData, TmaRegionPlace>
{
	private final TmaRegionService tmaRegionService;

	public TmaRegionPlacePopulator(final TmaRegionService tmaRegionService)
	{
		this.tmaRegionService = tmaRegionService;
	}

	@Override
	public void populate(final TmaPlaceData placeData, final TmaRegionPlace regionPlace)
	{
		validateParameterNotNullStandardMessage("source", placeData);
		validateParameterNotNullStandardMessage("target", regionPlace);

		final String regionIsoCode = placeData.getId();
		regionPlace.setRegion(getTmaRegionService().findRegionByIsocode(regionIsoCode));
		regionPlace.setRole(placeData.getRole());
	}

	protected TmaRegionService getTmaRegionService()
	{
		return tmaRegionService;
	}
}

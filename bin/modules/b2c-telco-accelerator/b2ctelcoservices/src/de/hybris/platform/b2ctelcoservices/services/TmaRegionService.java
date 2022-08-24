/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.core.model.c2l.RegionModel;


/**
 * Service for operations related to the {@link RegionModel}.
 *
 * @since 1907
 */
public interface TmaRegionService
{
	/**
	 * Searches for the {@link RegionModel} with the given isocode.
	 *
	 * @param isocode
	 *           isocode of the region
	 * @return the {@link RegionModel} with the isocode given or null if it cannot be found
	 */
	RegionModel findRegionByIsocode(final String isocode);

}

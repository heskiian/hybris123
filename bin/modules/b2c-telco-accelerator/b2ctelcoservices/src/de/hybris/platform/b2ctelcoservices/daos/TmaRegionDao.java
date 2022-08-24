/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;


/**
 * Data Access object for operations related to the {@link RegionModel}.
 *
 * @since 1907
 */
public interface TmaRegionDao extends RegionDao
{

	/**
	 * Retrieves the {@link RegionModel} identified by given isocode.
	 *
	 * @param isocode
	 *           the isocode of the region
	 *
	 * @return {@link RegionModel} object for given isocode
	 */
	RegionModel findRegionByIsocode(String isocode);
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaRegionDao;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.core.model.c2l.RegionModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaRegionService}.
 *
 * @since 1907
 */
public class DefaultTmaRegionService implements TmaRegionService
{

	private TmaRegionDao tmaRegionDao;

	@Override
	public RegionModel findRegionByIsocode(final String isocode)
	{
		return getTmaRegionDao().findRegionByIsocode(isocode);
	}

	protected TmaRegionDao getTmaRegionDao()
	{
		return tmaRegionDao;
	}

	@Required
	public void setTmaRegionDao(final TmaRegionDao tmaRegionDao)
	{
		this.tmaRegionDao = tmaRegionDao;
	}
}

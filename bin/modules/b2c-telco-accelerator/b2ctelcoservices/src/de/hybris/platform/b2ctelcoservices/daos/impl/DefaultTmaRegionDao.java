/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static java.lang.String.format;

import de.hybris.platform.b2ctelcoservices.daos.TmaRegionDao;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.daos.impl.DefaultRegionDao;

import java.util.Collection;
import java.util.Collections;


/**
 * Default implementation of the {@link TmaRegionDao}.
 *
 * @since 1907
 */
public class DefaultTmaRegionDao extends DefaultRegionDao implements TmaRegionDao
{

	@Override
	public RegionModel findRegionByIsocode(final String isocode)
	{
		validateParameterNotNullStandardMessage(RegionModel.ISOCODE, isocode);
		final Collection<RegionModel> regions = find(Collections.singletonMap(RegionModel.ISOCODE, isocode));
		if (regions.isEmpty())
		{
			throw new ModelNotFoundException(RegionModel._TYPECODE + format("  with code '%s' not found!", isocode));
		}
		if (regions.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + regions.size() + " regions with the unique isocode '" + isocode + "'");
		}

		return regions.iterator().next();
	}

}

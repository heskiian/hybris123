/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.catalog;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Catalog;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link CatalogData} and {@link Catalog}
 *
 * @since 1907
 */
public class CatalogValidForAttributeMapper extends TmaAttributeMapper<CatalogData, Catalog>
{
	@Override
	public void populateTargetAttributeFromSource(CatalogData source, Catalog target, MappingContext context)
	{
		if (source.getStartDateTime() == null)
		{
			return;
		}
		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDateTime());
		target.setValidFor(timePeriod);
	}
}

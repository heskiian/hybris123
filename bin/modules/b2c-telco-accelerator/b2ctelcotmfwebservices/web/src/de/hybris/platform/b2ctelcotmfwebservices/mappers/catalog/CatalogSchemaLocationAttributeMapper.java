/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.catalog;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Catalog;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link CatalogData} and {@link Catalog}
 *
 * @since 1907
 */
public class CatalogSchemaLocationAttributeMapper extends TmaAttributeMapper<CatalogData, Catalog>
{
	@Override
	public void populateTargetAttributeFromSource(CatalogData source, Catalog target, MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.catalog;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Catalog;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attype attribute between {@link CatalogData} and {@link Catalog}
 *
 * @since 1907
 */
public class CatalogAtTypeAttributeMapper extends TmaAttributeMapper<CatalogData, Catalog>
{
	@Override
	public void populateTargetAttributeFromSource(CatalogData source, Catalog target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}

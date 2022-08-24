/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.catalog.converters.populators;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.catalog.data.CatalogData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of the {@link CatalogData} entity from {@link CatalogModel}
 *
 * @since 1907
 */
public class TmaBaseCatalogPopulator implements Populator<CatalogModel, CatalogData>
{

	@Override
	public void populate(CatalogModel source, CatalogData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());
		target.setStartDateTime(source.getCreationtime());
		target.setLastModified(source.getModifiedtime());
		target.setType(getCatalogType(source));
	}

	private String getCatalogType(CatalogModel catalog)
	{
		if (catalog instanceof ClassificationSystemModel)
		{
			return ClassificationSystemModel._TYPECODE;
		}

		if (catalog instanceof ContentCatalogModel)
		{
			return ContentCatalogModel._TYPECODE;
		}

		return CatalogModel._TYPECODE;
	}
}

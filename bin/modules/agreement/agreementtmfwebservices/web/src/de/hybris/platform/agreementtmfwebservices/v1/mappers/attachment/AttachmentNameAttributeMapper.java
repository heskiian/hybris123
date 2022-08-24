/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.attachment;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentNameAttributeMapper extends AgrAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentNameAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getRealFileName()))
		{
			return;
		}

		target.setName(source.getRealFileName());
	}

	@Override
	public void populateSourceAttributeFromTarget(final AttachmentRefOrValue target, final CatalogUnawareMediaModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getName()))
		{
			source.setRealFileName(target.getName());
		}
	}

}

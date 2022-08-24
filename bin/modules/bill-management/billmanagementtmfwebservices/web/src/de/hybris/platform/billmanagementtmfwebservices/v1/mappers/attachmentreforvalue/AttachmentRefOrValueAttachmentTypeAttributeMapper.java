/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.attachmentreforvalue;

import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attachmentType attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentRefOrValueAttachmentTypeAttributeMapper
		extends BmAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentRefOrValueAttachmentTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (source.getMediaFormat() != null && StringUtils.isNotEmpty(source.getMediaFormat().getName()))
		{
			target.setAttachmentType(source.getMediaFormat().getName());
		}
	}
}

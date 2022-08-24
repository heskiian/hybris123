/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.attachmentreforvalue;

import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentRefOrValueTypeAttributeMapper extends BmAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentRefOrValueTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}

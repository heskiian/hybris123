/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.attachment;

import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaFormatModel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attachmentType attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentTypeAttributeMapper
		extends AgrAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AttachmentTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (source.getMediaFormat() != null)
		{
			target.setAttachmentType(source.getMediaFormat().getName());
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final AttachmentRefOrValue target, final CatalogUnawareMediaModel source,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(target.getAttachmentType()))
		{
			return;
		}

		final Map parameters = new HashMap();
		parameters.put(MediaFormatModel.QUALIFIER, target.getAttachmentType());
		MediaFormatModel mediaFormatModel = (MediaFormatModel) getAgrGenericService()
				.getItem(MediaFormatModel._TYPECODE, parameters);

		if (mediaFormatModel == null)
		{
			mediaFormatModel = (MediaFormatModel) getAgrGenericService()
					.createItem(MediaFormatModel.class);
		}

		mediaFormatModel.setName(target.getAttachmentType());
		mediaFormatModel.setQualifier(target.getAttachmentType());
		getAgrGenericService().saveItem(mediaFormatModel);
		source.setMediaFormat(mediaFormatModel);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}


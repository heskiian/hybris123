/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.attachment;

import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referredType attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentReferredTypeAttributeMapper
		extends AgrAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.atreferredType(AgreementtmfwebservicesConstants.AGR_ATTACHMENT_REFERRED);
		}
	}
}

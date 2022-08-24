/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.externalReference;

import de.hybris.platform.partyservices.model.PmExternalReferenceModel;
import de.hybris.platform.partytmfwebservices.v1.dto.ExternalReference;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link PmExternalReferenceModel} and {@link ExternalReference}
 *
 * @since 2108
 */
public class ExternalReferenceAtTypeAttributeMapper extends PmAttributeMapper<PmExternalReferenceModel, ExternalReference>
{
	public ExternalReferenceAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmExternalReferenceModel source, final ExternalReference target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAttype(target.getClass().getSimpleName());
	}
}


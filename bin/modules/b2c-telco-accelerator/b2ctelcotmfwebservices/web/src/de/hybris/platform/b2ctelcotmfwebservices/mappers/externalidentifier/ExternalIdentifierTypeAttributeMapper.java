/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.externalidentifier;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ExternalIdentifier;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external identifier type between {@link TmaExternalIdentifierData} and {@link ExternalIdentifier}
 *
 * @since 2102
 */
public class ExternalIdentifierTypeAttributeMapper extends TmaAttributeMapper<TmaExternalIdentifierData, ExternalIdentifier>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaExternalIdentifierData source, final ExternalIdentifier target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getResourceType()))
		{
			target.setExternalIdentifierType(source.getResourceType());
		}
	}
}
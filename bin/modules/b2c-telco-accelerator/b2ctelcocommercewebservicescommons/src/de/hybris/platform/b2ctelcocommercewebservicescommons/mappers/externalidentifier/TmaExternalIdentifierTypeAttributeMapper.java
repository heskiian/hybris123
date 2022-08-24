/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.externalidentifier;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ExternalIdentifierWsDto;
import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external identifier type between {@link TmaExternalIdentifierData} and {@link ExternalIdentifierWsDto}
 *
 * @since 2102
 */
public class TmaExternalIdentifierTypeAttributeMapper
		extends TmaAttributeMapper<TmaExternalIdentifierData, ExternalIdentifierWsDto>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaExternalIdentifierData source, final ExternalIdentifierWsDto target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getResourceType()))
		{
			target.setExternalIdentifierType(source.getResourceType());
		}
	}
}
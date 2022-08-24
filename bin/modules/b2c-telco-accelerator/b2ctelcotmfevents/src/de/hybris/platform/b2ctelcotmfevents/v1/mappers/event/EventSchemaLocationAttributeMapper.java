/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaAbstractEventPayload;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link TmaAbstractEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class EventSchemaLocationAttributeMapper extends TmaAttributeMapper<TmaAbstractEventPayload, Event>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractEventPayload source, final Event target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source))
		{
			target.setAtschemaLocation(B2ctelcotmfresourcesConstants.TMA_API_SCHEMA_V3);
		}
	}
}

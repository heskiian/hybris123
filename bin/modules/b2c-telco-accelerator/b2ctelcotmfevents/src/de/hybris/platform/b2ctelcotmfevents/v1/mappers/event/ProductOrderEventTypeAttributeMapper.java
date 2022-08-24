/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaOrderEventPayload;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.util.Config;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for eventType attribute between {@link TmaOrderEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class ProductOrderEventTypeAttributeMapper extends TmaAttributeMapper<TmaOrderEventPayload, Event>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaOrderEventPayload source, final Event target,
			final MappingContext context)
	{
		if (source.getEventType() != null)
		{
			target.setEventType(
					Config.getParameter(B2ctelcotmfresourcesConstants.TMA_PRODUCT_ORDER_DEFAULT_REFERRED) + source.getEventType()
							.getCode());
		}
	}
}

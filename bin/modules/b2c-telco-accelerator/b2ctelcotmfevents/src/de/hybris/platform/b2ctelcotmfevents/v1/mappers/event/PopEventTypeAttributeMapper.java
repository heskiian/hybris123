/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaProductOfferingPriceEventPayload;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.util.Config;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for eventType attribute between {@link TmaProductOfferingPriceEventPayload} and
 * {@link Event}
 *
 * @since 2105
 */
public class PopEventTypeAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceEventPayload, Event>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceEventPayload source, final Event target,
			final MappingContext context)
	{
		if (source.getEventType() != null)
		{
			target.setEventType(
					Config.getParameter(B2ctelcotmfresourcesConstants.TMA_PRODUCT_OFFERING_PRICE_DEFAULT_REFERRED) + source.getEventType()
							.getCode());
		}
	}
}

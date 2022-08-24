/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfeventshttpclient.builders.impl;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.b2ctelcotmfevents.events.TmaAbstractEvent;
import de.hybris.platform.b2ctelcotmfeventshttpclient.builders.TmaEventExportDeadLetterDataBuilder;

import java.time.Instant;

import org.springframework.http.RequestEntity;


/**
 * Default implementation of {@link TmaEventExportDeadLetterDataBuilder}.
 *
 * @since 2105
 */
public class DefaultTmaEventExportDeadLetterDataBuilder implements TmaEventExportDeadLetterDataBuilder
{

	@Override
	public EventExportDeadLetterData build(final EventSourceData eventSourceData, final RequestEntity requestEntity,
			final String errorMessage)
	{
		final EventExportDeadLetterData eventExportDeadLetterData = new EventExportDeadLetterData();
		final String id = ((TmaAbstractEvent) eventSourceData.getEvent()).getPayload().getEventId();
		eventExportDeadLetterData.setId(id);
		eventExportDeadLetterData.setTimestamp(java.util.Date.from(Instant.ofEpochMilli(eventSourceData.getEvent().getTimestamp())));
		eventExportDeadLetterData.setError(errorMessage);
		eventExportDeadLetterData.setPayload(requestEntity.toString());
		eventExportDeadLetterData.setDestinationTarget(eventSourceData.getEventConfig().getDestinationTarget());
		eventExportDeadLetterData.setEventType(((TmaAbstractEvent) eventSourceData.getEvent()).getPayload().getEventType());

		return eventExportDeadLetterData;
	}
}

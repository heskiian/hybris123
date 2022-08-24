/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfeventshttpclient.builders;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;

import org.springframework.http.RequestEntity;


/**
 * Builder responsible for creating a {@link EventExportDeadLetterData} for a given failed event.
 *
 * @since 2105
 */
public interface TmaEventExportDeadLetterDataBuilder
{
	/**
	 * Creates a new instance of {@link EventExportDeadLetterData} for a given event details.
	 *
	 * @param event
	 * 		the event information.
	 * @param requestEntity
	 * 		the request which failed to be sent
	 * @param errorMessage
	 * 		the error message received for the given failed request
	 * @return an instance of {@link EventExportDeadLetterData} which all details about the failed request
	 */
	EventExportDeadLetterData build(final EventSourceData event, final RequestEntity requestEntity,
			final String errorMessage);
}

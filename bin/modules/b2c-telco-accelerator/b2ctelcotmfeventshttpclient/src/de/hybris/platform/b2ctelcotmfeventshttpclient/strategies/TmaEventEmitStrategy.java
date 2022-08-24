/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfeventshttpclient.strategies;

import de.hybris.platform.apiregistryservices.dto.EventExportDeadLetterData;
import de.hybris.platform.apiregistryservices.dto.EventSourceData;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.services.EventDlqService;
import de.hybris.platform.apiregistryservices.strategies.EventEmitStrategy;
import de.hybris.platform.b2ctelcotmfevents.events.TmaAbstractEvent;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.b2ctelcotmfeventshttpclient.builders.TmaEventExportDeadLetterDataBuilder;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Custom implementation of {@link EventEmitStrategy} for sending events on TMF channel.
 *
 * @since 2105
 */
public class TmaEventEmitStrategy implements EventEmitStrategy
{
	private IntegrationRestTemplateFactory restTemplateFactory;
	private TmaEventExportDeadLetterDataBuilder tmaEventExportDeadLetterDataBuilder;
	private EventDlqService eventDlqService;

	public TmaEventEmitStrategy(final IntegrationRestTemplateFactory integrationRestTemplateFactory,
			final TmaEventExportDeadLetterDataBuilder tmaEventExportDeadLetterDataBuilder, final EventDlqService eventDlqService)
	{
		this.restTemplateFactory = integrationRestTemplateFactory;
		this.tmaEventExportDeadLetterDataBuilder = tmaEventExportDeadLetterDataBuilder;
		this.eventDlqService = eventDlqService;
	}

	/**
	 * Will send an HTTP call with the Event data to the {@link ConsumedDestinationModel}s configured for the event
	 * using {@link IntegrationRestTemplateFactory} and {@link RestOperations} for the HTTP calls.
	 * In case of HTTP exception will add the event to the event dead letter queue.
	 *
	 * @param payload
	 * 		containing the event data and its configurations.
	 */
	@Override
	public void sendEvent(final Object payload)
	{
		final EventSourceData eventSourceData = (EventSourceData) ((GenericMessage) payload).getPayload();
		final EventConfigurationModel eventConfig = eventSourceData.getEventConfig();
		final TmaAbstractEvent tmaEvent = (TmaAbstractEvent) eventSourceData.getEvent();
		final Event eventDto = tmaEvent.getPayload();

		eventConfig.getDestinationTarget().getDestinations().stream()
				.filter(destination -> destination instanceof ConsumedDestinationModel && destination.isActive())
				.forEach(destination ->
				{
					final ConsumedDestinationModel destinationModel = (ConsumedDestinationModel) destination;
					final String requestUrl = destinationModel.getUrl();
					final RequestEntity<Event> requestEntity = createRequestEntity(eventDto, requestUrl);
					final RestOperations restTemplate = getRestTemplateFactory().create(destinationModel);

					try
					{
						restTemplate.postForEntity(requestUrl, requestEntity, null);
					}
					catch (final HttpClientErrorException exception)
					{
						final EventExportDeadLetterData eventExportDeadLetterData = getTmaEventExportDeadLetterDataBuilder()
								.build(eventSourceData, requestEntity, exception.getLocalizedMessage());
						getEventDlqService().sendToQueue(eventExportDeadLetterData);
					}
				});
	}

	protected RequestEntity<Event> createRequestEntity(final Event event, final String url)
	{
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		final RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(HttpMethod.POST, builder.build().toUri());

		return requestBuilder.body(event);
	}

	protected TmaEventExportDeadLetterDataBuilder getTmaEventExportDeadLetterDataBuilder()
	{
		return tmaEventExportDeadLetterDataBuilder;
	}

	protected EventDlqService getEventDlqService()
	{
		return eventDlqService;
	}

	protected IntegrationRestTemplateFactory getRestTemplateFactory()
	{
		return restTemplateFactory;
	}
}

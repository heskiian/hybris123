/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.listeners;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaProductOfferingEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import ma.glasnost.orika.MapperFacade;


/**
 * AfterSaveListener for a {@link TmaProductOfferingModel} related event.
 *
 * @since 2105
 */
public class TmaProductOfferingAfterSaveListener extends TmaAbstractAfterSaveListener<TmaProductOfferingModel>
{

	private static final Integer PRODUCT_TYPECODE = 1;

	private MapperFacade dataMapper;

	public TmaProductOfferingAfterSaveListener(final ModelService modelService, final MapperFacade dataMapper,
			final TmaAbstractEventPayloadBuilder eventPayloadBuilder, final TmaAbstractEventsService eventsService,
			final Set<String> eventAllowedCatalogVersions)
	{
		super(modelService, eventPayloadBuilder, eventsService, eventAllowedCatalogVersions);
		this.dataMapper = dataMapper;
	}

	@Override
	protected boolean checkVersionMatch(PK pk)
	{
		final TmaProductOfferingModel productOffering = getModelService().get(pk);
		return getEventAllowedCatalogVersions().contains(productOffering.getCatalogVersion().getVersion());
	}

	@Override
	protected Integer getObjectTypeCode()
	{
		return PRODUCT_TYPECODE;
	}

	@Override
	protected TmaProductOfferingEvent createEventWithPayload(final TmaProductOfferingModel item, final TmaEventType eventType)
	{
		final TmaProductOfferingEvent productOfferingEvent = new TmaProductOfferingEvent();
		productOfferingEvent.setPayload(getDataMapper().map(getEventPayloadBuilder().build(item, eventType), Event.class));
		return productOfferingEvent;
	}

	protected MapperFacade getDataMapper()
	{
		return dataMapper;
	}
}

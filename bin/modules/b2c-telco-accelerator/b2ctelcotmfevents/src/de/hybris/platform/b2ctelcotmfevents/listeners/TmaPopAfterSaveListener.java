/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.listeners;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaProductOfferingPriceEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import ma.glasnost.orika.MapperFacade;


/**
 * AfterSaveListener for a {@link TmaProductOfferingPriceModel} related event.
 *
 * @since 2105
 */
public class TmaPopAfterSaveListener extends TmaAbstractAfterSaveListener<TmaProductOfferingPriceModel>
{
	private static final Integer PRODUCT_OFFERING_PRICE_TYPECODE = 7609;

	private MapperFacade dataMapper;

	public TmaPopAfterSaveListener(final ModelService modelService, final MapperFacade dataMapper,
			final TmaAbstractEventPayloadBuilder eventPayloadBuilder, final TmaAbstractEventsService eventsService,
			final Set<String> eventAllowedCatalogVersions)
	{
		super(modelService, eventPayloadBuilder, eventsService, eventAllowedCatalogVersions);
		this.dataMapper = dataMapper;
	}

	@Override
	protected boolean checkVersionMatch(PK pk)
	{
		final TmaProductOfferingPriceModel pop = getModelService().get(pk);
		return getEventAllowedCatalogVersions().contains(pop.getCatalogVersion().getVersion());
	}

	@Override
	protected Integer getObjectTypeCode()
	{
		return PRODUCT_OFFERING_PRICE_TYPECODE;
	}

	@Override
	protected TmaProductOfferingPriceEvent createEventWithPayload(final TmaProductOfferingPriceModel item,
			final TmaEventType eventType)
	{
		final TmaProductOfferingPriceEvent productOfferingPriceEvent = new TmaProductOfferingPriceEvent();
		productOfferingPriceEvent.setPayload(getDataMapper().map(getEventPayloadBuilder().build(item, eventType), Event.class));
		return productOfferingPriceEvent;
	}

	protected MapperFacade getDataMapper()
	{
		return dataMapper;
	}
}

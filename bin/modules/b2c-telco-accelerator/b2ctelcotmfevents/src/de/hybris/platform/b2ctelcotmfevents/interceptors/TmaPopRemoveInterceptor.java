/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaProductOfferingPriceEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;


/**
 * Interceptor that will publish a tmfEvent on removal of a {@link TmaProductOfferingPriceModel}.
 *
 * @since 2105
 */
public class TmaPopRemoveInterceptor implements RemoveInterceptor<TmaProductOfferingPriceModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private TmaAbstractEventsService eventService;
	private MapperFacade dataMapper;
	private TmaAbstractEventPayloadBuilder eventPayloadBuilder;
	private Set<String> eventAllowedCatalogVersions;
	private ModelService modelService;
	private SessionService sessionService;

	public TmaPopRemoveInterceptor(final TmaAbstractEventsService eventService, final MapperFacade dataMapper,
			final TmaAbstractEventPayloadBuilder eventPayloadBuilder, final Set<String> eventAllowedCatalogVersions,
			final ModelService modelService, final SessionService sessionService)
	{
		this.dataMapper = dataMapper;
		this.eventService = eventService;
		this.eventPayloadBuilder = eventPayloadBuilder;
		this.eventAllowedCatalogVersions = eventAllowedCatalogVersions;
		this.modelService = modelService;
		this.sessionService = sessionService;
	}

	@Override
	public void onRemove(final TmaProductOfferingPriceModel productOfferingPrice, final InterceptorContext context)
	{
		if (checkVersionMarch(productOfferingPrice))
		{
			final TmaProductOfferingPriceEvent productOfferingPriceEvent = new TmaProductOfferingPriceEvent();
			productOfferingPriceEvent.setPayload(getDataMapper()
					.map(getEventPayloadBuilder().build(productOfferingPrice, TmaEventType.DELETEEVENT), Event.class));
			getEventService().publishEvent(productOfferingPriceEvent);
		}

		removePriceRowsFor(productOfferingPrice);
	}

	/**
	 * Will remove the {@link PriceRowModel}s where the removed {@link TmaProductOfferingPriceModel} was directly
	 * used.
	 *
	 * @param productOfferingPrice
	 * 		the given productOfferingPrice
	 */
	private void removePriceRowsFor(final TmaProductOfferingPriceModel productOfferingPrice)
	{
		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		if (Boolean.TRUE.equals(isSyncActive) || CollectionUtils.isEmpty(productOfferingPrice.getPriceRows()))
		{
			return;
		}

		productOfferingPrice.getPriceRows().forEach(priceRow -> getModelService().remove(priceRow));
	}

	private boolean checkVersionMarch(final TmaProductOfferingPriceModel productOffering)
	{
		return getEventAllowedCatalogVersions().contains(productOffering.getCatalogVersion().getVersion());
	}

	protected TmaAbstractEventsService getEventService()
	{
		return eventService;
	}

	protected MapperFacade getDataMapper()
	{
		return dataMapper;
	}

	protected TmaAbstractEventPayloadBuilder getEventPayloadBuilder()
	{
		return eventPayloadBuilder;
	}

	protected Set<String> getEventAllowedCatalogVersions()
	{
		return eventAllowedCatalogVersions;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}

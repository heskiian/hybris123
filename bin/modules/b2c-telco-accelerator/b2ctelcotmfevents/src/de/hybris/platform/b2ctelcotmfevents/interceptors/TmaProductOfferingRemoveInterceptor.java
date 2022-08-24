/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfevents.builders.TmaAbstractEventPayloadBuilder;
import de.hybris.platform.b2ctelcotmfevents.enums.TmaEventType;
import de.hybris.platform.b2ctelcotmfevents.events.TmaProductOfferingEvent;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.Event;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;


/**
 * Interceptor that will publish a tmfEvent on removal of a {@link TmaProductOfferingModel}.
 *
 * @since 2105
 */
public class TmaProductOfferingRemoveInterceptor implements RemoveInterceptor<TmaProductOfferingModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private TmaAbstractEventsService eventService;
	private MapperFacade dataMapper;
	private TmaAbstractEventPayloadBuilder eventPayloadBuilder;
	private Set<String> eventAllowedCatalogVersions;
	private ModelService modelService;
	private SessionService sessionService;

	public TmaProductOfferingRemoveInterceptor(final TmaAbstractEventsService eventService, final MapperFacade dataMapper, final
	TmaAbstractEventPayloadBuilder eventPayloadBuilder, final Set<String> eventAllowedCatalogVersions,
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
	public void onRemove(final TmaProductOfferingModel productOffering, final InterceptorContext context)
	{
		if (checkVersionMatch(productOffering))
		{
			final TmaProductOfferingEvent productOfferingEvent = new TmaProductOfferingEvent();
			productOfferingEvent.setPayload(
					getDataMapper().map(getEventPayloadBuilder().build(productOffering, TmaEventType.DELETEEVENT), Event.class));
			getEventService().publishEvent(productOfferingEvent);
		}

		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		if (!Boolean.TRUE.equals(isSyncActive) && !context.isRemoved(productOffering))
		{
			if (CollectionUtils.isEmpty(productOffering.getParentBpoOptions()))
			{
				return;
			}
			productOffering.getParentBpoOptions().forEach(bundledProdOfferOptionModel -> {
				if (bundledProdOfferOptionModel.getProductOffering().getCode().equals(productOffering.getCode()))
				{
					getModelService().remove(bundledProdOfferOptionModel);
				}
			});
		}
	}

	private boolean checkVersionMatch(final TmaProductOfferingModel productOffering)
	{
		return geteventAllowedCatalogVersions().contains(productOffering.getCatalogVersion().getVersion());
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

	protected Set<String> geteventAllowedCatalogVersions()
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


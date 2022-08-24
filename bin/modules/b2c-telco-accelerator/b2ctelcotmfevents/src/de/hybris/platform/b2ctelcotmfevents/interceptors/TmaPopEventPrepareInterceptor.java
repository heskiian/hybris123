/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import org.springframework.util.ObjectUtils;


/**
 * Prepare interceptor that will add {@link TmaProductOfferingPriceModel}s to a events whitelist used for publishing tmf events.
 *
 * @since 2105
 */
public class TmaPopEventPrepareInterceptor implements PrepareInterceptor<TmaProductOfferingPriceModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private SessionService sessionService;
	private TmaAbstractEventsService eventsService;
	private Set<String> eventAllowedCatalogVersions;

	public TmaPopEventPrepareInterceptor(final SessionService sessionService, final TmaAbstractEventsService eventsService,
			final Set<String> eventAllowedCatalogVersions)
	{
		this.sessionService = sessionService;
		this.eventsService = eventsService;
		this.eventAllowedCatalogVersions = eventAllowedCatalogVersions;
	}

	@Override
	public void onPrepare(final TmaProductOfferingPriceModel productOfferingPrice, final InterceptorContext interceptorContext)
	{

		if (interceptorContext.isNew(productOfferingPrice) || getEventsService()
				.isItemInEventsWhiteList(productOfferingPrice.getPk().toString()))
		{
			return;
		}

		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		if (Boolean.TRUE.equals(isSyncActive) && getEventsService()
				.shouldSendEventOnSynchronize(productOfferingPrice))
		{
			getEventsService().addItemToEventsWhiteList(productOfferingPrice.getPk().toString());
		}

		if ((ObjectUtils.isEmpty(isSyncActive) || Boolean.FALSE.equals(isSyncActive)) && checkVersionMatch(productOfferingPrice))
		{
			getEventsService().addItemToEventsWhiteList(productOfferingPrice.getPk().toString());
		}
	}

	private boolean checkVersionMatch(final TmaProductOfferingPriceModel item)
	{
		return getEventAllowedCatalogVersions().contains(item.getCatalogVersion().getVersion());
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	protected TmaAbstractEventsService getEventsService()
	{
		return eventsService;
	}

	protected Set<String> getEventAllowedCatalogVersions()
	{
		return eventAllowedCatalogVersions;
	}
}

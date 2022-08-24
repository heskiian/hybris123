/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcotmfevents.services.TmaAbstractEventsService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import org.springframework.util.ObjectUtils;


/**
 * Prepare interceptor that will add {@link TmaProductOfferingModel}s to a events blacklist if the flow is synchronize and the
 * model is not outdated.
 *
 * @since 2105
 */
public class TmaProductOfferingEventPrepareInterceptor implements PrepareInterceptor<TmaProductOfferingModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";

	private SessionService sessionService;
	private TmaAbstractEventsService eventsService;
	private Set<String> eventAllowedCatalogVersions;

	public TmaProductOfferingEventPrepareInterceptor(final SessionService sessionService, final TmaAbstractEventsService eventsService,
			final Set<String> eventAllowedCatalogVersions)
	{
		this.sessionService = sessionService;
		this.eventsService = eventsService;
		this.eventAllowedCatalogVersions = eventAllowedCatalogVersions;
	}

	@Override
	public void onPrepare(final TmaProductOfferingModel productOffering, final InterceptorContext interceptorContext)
	{

		if (interceptorContext.isNew(productOffering) || getEventsService()
				.isItemInEventsWhiteList(productOffering.getPk().toString()))
		{
			return;
		}

		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		if (Boolean.TRUE.equals(isSyncActive) && getEventsService()
				.shouldSendEventOnSynchronize(productOffering))
		{
			getEventsService().addItemToEventsWhiteList(productOffering.getPk().toString());
		}

		if ((ObjectUtils.isEmpty(isSyncActive) || Boolean.FALSE.equals(isSyncActive)) && checkVersionMatch(productOffering))
		{
			getEventsService().addItemToEventsWhiteList(productOffering.getPk().toString());
		}
	}

	private boolean checkVersionMatch(final TmaProductOfferingModel item)
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

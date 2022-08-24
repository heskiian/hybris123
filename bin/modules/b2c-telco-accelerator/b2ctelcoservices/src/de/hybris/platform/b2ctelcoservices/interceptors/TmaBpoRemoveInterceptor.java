/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Removes product offering groups when a BPO is removed.
 *
 * @since 2108
 */
public class TmaBpoRemoveInterceptor implements RemoveInterceptor<TmaBundledProductOfferingModel>
{
	private static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
	private SessionService sessionService;

	public TmaBpoRemoveInterceptor(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	public void onRemove(final TmaBundledProductOfferingModel bpo, final InterceptorContext ctx)
	{
		final Boolean isSyncActive = this.getSessionService().getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		final List<TmaProductOfferingGroupModel> productOfferingGroups = bpo.getProductOfferingGroups();

		if (!Boolean.TRUE.equals(isSyncActive) && CollectionUtils.isNotEmpty(productOfferingGroups))
		{
			ctx.getModelService().removeAll(productOfferingGroups);
		}
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

}


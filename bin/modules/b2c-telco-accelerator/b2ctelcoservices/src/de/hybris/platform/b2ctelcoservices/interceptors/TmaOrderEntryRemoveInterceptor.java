/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;


/**
 * Removes duplicated installation address when an order entry is removed
 *
 * @since 2003
 */
public class TmaOrderEntryRemoveInterceptor implements RemoveInterceptor<OrderEntryModel>
{
	@Override
	public void onRemove(final OrderEntryModel orderEntry, final InterceptorContext ctx)
	{
		final AddressModel installationAddress = orderEntry.getInstallationAddress();
		if (installationAddress != null && installationAddress.getDuplicate())
		{
			ctx.getModelService().remove(installationAddress);
		}
	}
}

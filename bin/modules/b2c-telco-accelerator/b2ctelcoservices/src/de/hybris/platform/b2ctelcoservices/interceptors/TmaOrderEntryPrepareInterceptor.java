/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * Clones the installation address for the orderEntry
 *
 * @since 2003
 */
public class TmaOrderEntryPrepareInterceptor implements PrepareInterceptor<OrderEntryModel>
{

	private TmaAddressService addressService;

	public TmaOrderEntryPrepareInterceptor(TmaAddressService addressService)
	{
		this.addressService = addressService;
	}

	@Override
	public void onPrepare(final OrderEntryModel orderEntry, InterceptorContext ctx)
	{
		if (ctx.isModified(orderEntry, AbstractOrderEntryModel.INSTALLATIONADDRESS) && orderEntry.getInstallationAddress() != null)
		{
			final AddressModel installationAddress = orderEntry.getInstallationAddress();
			if (getAddressService().doesAddressNeedCloning(installationAddress, orderEntry))
			{
				final AddressModel clonedInstallationAddress = getAddressService()
						.cloneAddressForOwner(installationAddress, orderEntry);
				orderEntry.setInstallationAddress(clonedInstallationAddress);
				ctx.registerElementFor(clonedInstallationAddress, PersistenceOperation.SAVE);
			}
		}
	}

	protected TmaAddressService getAddressService()
	{
		return addressService;
	}


}

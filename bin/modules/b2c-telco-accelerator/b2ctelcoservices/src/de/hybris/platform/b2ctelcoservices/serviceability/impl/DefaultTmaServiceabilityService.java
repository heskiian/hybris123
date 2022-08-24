/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.serviceability.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.serviceability.TmaServiceabilityCheckService;
import de.hybris.platform.b2ctelcoservices.serviceability.TmaServiceabilityService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;


/**
 * Default implementation of {@link TmaServiceabilityService}
 *
 * @since 2102
 */
public class DefaultTmaServiceabilityService implements TmaServiceabilityService
{
	List<TmaServiceabilityCheckService> tmaServiceabilityCheckServices;

	/**
	 * This is a class constructor used to specify the required dependencies required by class for creation.
	 *
	 * @param tmaServiceabilityCheckServices
	 *           the b2ctelcoServiceabilityClientService
	 * @return the class constructor initialized with required dependencies
	 */
	public DefaultTmaServiceabilityService(final List<TmaServiceabilityCheckService> tmaServiceabilityCheckServices)
	{
		this.tmaServiceabilityCheckServices = tmaServiceabilityCheckServices;
	}

	@Override
	public boolean isProductOfferingServiceable(final ProductModel product, final AddressModel installationAddress)
	{
		if (!(product instanceof TmaProductOfferingModel))
		{
			return true;
		}
		for (final TmaServiceabilityCheckService serviceabilityCheckService : getTmaServiceabilityCheckServices())
		{
			if (!serviceabilityCheckService.checkServiceability(product, installationAddress))
			{
				return false;
			}
		}
		return true;
	}

	protected List<TmaServiceabilityCheckService> getTmaServiceabilityCheckServices()
	{
		return tmaServiceabilityCheckServices;
	}
}

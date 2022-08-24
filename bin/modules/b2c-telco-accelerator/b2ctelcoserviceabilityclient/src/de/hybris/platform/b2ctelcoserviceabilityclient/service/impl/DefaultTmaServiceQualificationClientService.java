/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;

import de.hybris.platform.b2ctelcoserviceabilityclient.client.ServiceabilityHttpClient;
import de.hybris.platform.b2ctelcoserviceabilityclient.service.TmaServiceQualificationClientService;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;


/**
 * Default implementation of {@link TmaServiceQualificationClientService}
 *
 * @since 2102
 */

public class DefaultTmaServiceQualificationClientService implements TmaServiceQualificationClientService
{
	private final ServiceabilityHttpClient serviceabilityHttpClient;

	/**
	 * This is a class constructor used to specify the required dependencies required by class for creation.
	 *
	 * @param serviceabilityHttpClient
	 *           the serviceabilityHttpClient
	 * @return the class constructor initialized with required dependencies
	 */
	public DefaultTmaServiceQualificationClientService(final ServiceabilityHttpClient serviceabilityHttpClient)
	{
		this.serviceabilityHttpClient = serviceabilityHttpClient;
	}

	@Override
	public QueryServiceQualification createQueryServiceQualification(final QueryServiceQualification searchCriteria)
	{
		return getServiceabilityClient().queryServiceQualification(searchCriteria);

	}

	protected ServiceabilityHttpClient getServiceabilityClient()
	{
		return serviceabilityHttpClient;
	}
}

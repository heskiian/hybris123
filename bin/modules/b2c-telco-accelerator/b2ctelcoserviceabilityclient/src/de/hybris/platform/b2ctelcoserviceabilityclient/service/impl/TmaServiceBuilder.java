/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;


import de.hybris.platform.b2ctelcotmfresources.v4.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceRefOrValue;

import java.util.List;


/**
 * Builder class for the creation of the {@link Service}
 *
 * @since 2102
 *
 */
public class TmaServiceBuilder
{
	private String id;
	private List<GeographicAddress> place;

	public ServiceRefOrValue build()
	{
		final ServiceRefOrValue service = new ServiceRefOrValue();
		service.setId(id);
		service.setPlace(place);
		return service;
	}

	public static TmaServiceBuilder newTmaServiceContextBuilder()
	{
		return new TmaServiceBuilder();
	}

	public TmaServiceBuilder withPlace(final List<GeographicAddress> place)
	{
		this.place = place;
		return this;
	}
}

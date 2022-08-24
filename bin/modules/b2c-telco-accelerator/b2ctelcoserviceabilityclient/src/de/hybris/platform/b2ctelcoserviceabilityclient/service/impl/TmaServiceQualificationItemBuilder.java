/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;

import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceQualificationItem;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceRefOrValue;


/**
 * Builder class for the creation of the {@link ServiceQualificationItem}
 *
 * @since 2102
 *
 */
public class TmaServiceQualificationItemBuilder
{
	private ServiceRefOrValue service;
	private String type;


	public ServiceQualificationItem build()
	{
		final ServiceQualificationItem serviceQualificationItem = new ServiceQualificationItem();
		serviceQualificationItem.setService(service);
		serviceQualificationItem.setAttype(type);
		return serviceQualificationItem;
	}

	public static TmaServiceQualificationItemBuilder newTmaServiceQualificationItemBuilder()
	{
		return new TmaServiceQualificationItemBuilder();
	}

	public TmaServiceQualificationItemBuilder withService(final ServiceRefOrValue service)
	{
		this.service = service;
		return this;
	}

	public TmaServiceQualificationItemBuilder withType(final String type)
	{
		this.type = type;
		return this;
	}

}

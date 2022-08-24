/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.data;

import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseDetailRefWsDto;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * TmaSubscriptionBaseDetailRefWrapperData class to wrap {@link list} of {@link TmaSubscriptionBaseDetailRefWsDto}s .
 *
 * @since 1810
 */
public class TmaSubscriptionBaseDetailRefWrapperData implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<TmaSubscriptionBaseDetailRefWsDto> subscriptionBaseDetailRefWsDtos;

	protected List<TmaSubscriptionBaseDetailRefWsDto> getSubscriptionBaseDetailRefWsDtos()
	{
		return subscriptionBaseDetailRefWsDtos;
	}

	@Required
	public void setSubscriptionBaseDetailRefWsDtos(final List<TmaSubscriptionBaseDetailRefWsDto> subscriptionBaseDetailRefWsDtos)
	{
		this.subscriptionBaseDetailRefWsDtos = subscriptionBaseDetailRefWsDtos;
	}
}

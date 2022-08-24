/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcoservices.strategy.impl;

import de.hybris.platform.b2b.strategies.impl.DefaultB2BCreateOrderFromCartStrategy;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * Strategy for creating B2B order.
 *
 * @since 2105
 */
public class DefaultTmaB2bCreateOrderFromCartStrategy extends DefaultB2BCreateOrderFromCartStrategy
{
	@Override
	public void createB2BBusinessProcess(final OrderModel order)
	{
		//No implementation needed
	}
}

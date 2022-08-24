/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaOrderDao;
import de.hybris.platform.b2ctelcoservices.order.TmaOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.impl.DefaultOrderService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Service handling business logic acting upon orders.
 *
 * @since 2105
 */
public class DefaultTmaOrderService extends DefaultOrderService implements TmaOrderService
{

	@Override
	public List<OrderModel> getOrders(final Set<PrincipalModel> principals)
	{
		if (CollectionUtils.isEmpty(principals))
		{
			return Collections.emptyList();
		}

		return getOrderDao().findOrders(principals);
	}

	@Override
	protected TmaOrderDao getOrderDao()
	{
		return (TmaOrderDao) super.getOrderDao();
	}
}

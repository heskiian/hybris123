/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.impl;


import de.hybris.platform.b2ctelcofacades.order.TmaOrderFacade;
import de.hybris.platform.b2ctelcoservices.order.TmaOrderService;
import de.hybris.platform.b2ctelcoservices.services.PrincipalService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Default implementation of {@link TmaOrderFacade}
 *
 * @since 2102
 */
public class DefaultTmaOrderFacade extends DefaultOrderFacade implements TmaOrderFacade
{
	private PrincipalService principalService;
	private TmaOrderService orderService;

	public DefaultTmaOrderFacade(final PrincipalService principalService, final TmaOrderService orderService)
	{
		this.principalService = principalService;
		this.orderService = orderService;
	}

	@Override
	public OrderData getOrderDetails(final String code, final String relatedPartyId)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		final OrderModel orderModel;

		orderModel = getCustomerAccountService()
				.getOrderForCode((CustomerModel) getUserService().getUserForUID(relatedPartyId), code,
						baseStoreModel);

		return getOrderConverter().convert(orderModel);
	}

	@Override
	public List<OrderData> getOrders(final String principalId)
	{
		final PrincipalModel principal = getPrincipalService().findPrincipalByUid(principalId);

		return getOrderConverter().convertAll(getOrderService().getOrders(new HashSet<>(Collections.singletonList(principal))));
	}

	@Override
	public void updateStatus(final String code, final String principalId, final String status) throws IllegalAccessException
	{
		throw new IllegalAccessException("Order status update not allowed.");
	}

	@Override
	public boolean hasUserAccessToOrders(final String authenticatedUserId, final String principalId)
	{
		return authenticatedUserId.equalsIgnoreCase(principalId);
	}

	@Override
	public boolean canUserUpdateOrderStatus(final String principalId, final String authenticatedUserId, final String orderId)
	{
		return false;
	}

	protected PrincipalService getPrincipalService()
	{
		return principalService;
	}

	protected TmaOrderService getOrderService()
	{
		return orderService;
	}
}

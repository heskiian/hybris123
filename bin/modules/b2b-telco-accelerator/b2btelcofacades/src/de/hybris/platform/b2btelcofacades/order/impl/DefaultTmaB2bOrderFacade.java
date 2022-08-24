/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcofacades.order.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2btelcoservices.customer.TmaB2bCustomerAccountService;
import de.hybris.platform.b2ctelcofacades.order.TmaOrderFacade;
import de.hybris.platform.b2ctelcofacades.order.impl.DefaultTmaOrderFacade;
import de.hybris.platform.b2ctelcoservices.order.TmaOrderService;
import de.hybris.platform.b2ctelcoservices.services.PrincipalService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link TmaOrderFacade} in B2B context.
 *
 * @since 2105
 */
public class DefaultTmaB2bOrderFacade extends DefaultTmaOrderFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTmaB2bOrderFacade.class);

	private static final String ORGANIZATION_APPROVER_DECISION_EVENT_NAME = "_OrganizationApproverDecision";
	private static final String B2B_APPROVER_GROUP = "b2bapprovergroup";

	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	public DefaultTmaB2bOrderFacade(final PrincipalService principalService, final TmaOrderService orderService,
			final ModelService modelService, final BusinessProcessService businessProcessService)
	{
		super(principalService, orderService);
		this.modelService = modelService;
		this.businessProcessService = businessProcessService;
	}

	@Override
	public List<OrderData> getOrders(final String principalId)
	{
		final PrincipalModel principal = getPrincipalService().findPrincipalByUid(principalId);

		if (principal instanceof B2BUnitModel)
		{
			final Set<PrincipalModel> usersInUnit = getCustomerAccountService().getUsersFrom((B2BUnitModel) principal);
			return getOrderConverter().convertAll(getOrderService().getOrders(usersInUnit));
		}

		if (principal instanceof B2BCustomerModel)
		{
			final Set<PrincipalModel> usersInOrganization = getCustomerAccountService().getUsersInSameOrganizationWith(principal);
			return getOrderConverter().convertAll(getOrderService().getOrders(usersInOrganization));
		}

		return getOrderConverter().convertAll(getOrderService().getOrders(new HashSet<>(Collections.singletonList(principal))));
	}

	@Override
	public void updateStatus(final String code, final String principalId, final String status)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		final CustomerModel user = (CustomerModel) getUserService().getUserForUID(principalId);
		final OrderModel orderModel = getCustomerAccountService()
				.getOrderForCode(user, code, baseStoreModel);

		if (!OrderStatus.PENDING_APPROVAL.equals(orderModel.getStatus()))
		{
			final String orderStatusCode = orderModel.getStatus() != null ? orderModel.getStatus().getCode() : "-";
			throw new IllegalArgumentException(
					String.format("Order status '%s' update to '%s' not allowed.", orderStatusCode, status));
		}

		orderModel.setStatus(OrderStatus.valueOf(status));
		getModelService().save(orderModel);

		getBusinessProcessService().triggerEvent(orderModel.getCode() + ORGANIZATION_APPROVER_DECISION_EVENT_NAME);
	}

	@Override
	public boolean hasUserAccessToOrders(final String authenticatedUserId, final String principalId)
	{
		if (authenticatedUserId.equalsIgnoreCase(principalId))
		{
			return Boolean.TRUE;
		}

		PrincipalModel user;
		try
		{
			user = getPrincipalService().findPrincipalByUid(authenticatedUserId);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.warn("There is no principal with given id", e);
			return Boolean.FALSE;
		}

		final Set<PrincipalModel> usersInOrganization = getCustomerAccountService().getUsersInSameOrganizationWith(user);
		usersInOrganization.add(user);

		final Set<PrincipalModel> unitsInOrganization = getCustomerAccountService().getUnits(user);
		final Set<PrincipalGroupModel> principalGroups = user.getGroups().stream()
				.filter((PrincipalGroupModel principalGroup) -> principalGroup instanceof B2BUnitModel).collect(Collectors.toSet());
		unitsInOrganization.addAll(principalGroups);

		return getCustomerAccountService().isPrincipalMemberOfOrganizations(principalId, usersInOrganization, unitsInOrganization);
	}

	@Override
	public boolean canUserUpdateOrderStatus(final String principalId, final String authenticatedUserId, final String orderId)
	{

		final PrincipalModel principal = getPrincipalService().findPrincipalByUid(principalId);
		final PrincipalModel authenticatedUser = getPrincipalService().findPrincipalByUid(authenticatedUserId);

		final Set<PrincipalGroupModel> principalUnit = principal.getGroups().stream()
				.filter((PrincipalModel principalModel) -> principalModel instanceof B2BUnitModel).collect(Collectors.toSet());

		final boolean isApprover = authenticatedUser.getAllGroups().stream()
				.anyMatch((PrincipalModel principalModel) -> principalModel.getUid().equals(B2B_APPROVER_GROUP));

		final boolean isPartOfTheOrganization = getCustomerAccountService().getUnits(authenticatedUser).stream()
				.distinct().anyMatch(principalUnit::contains);

		return isApprover && isPartOfTheOrganization;
	}

	@Override
	protected TmaB2bCustomerAccountService getCustomerAccountService()
	{
		return (TmaB2bCustomerAccountService) super.getCustomerAccountService();
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}
}

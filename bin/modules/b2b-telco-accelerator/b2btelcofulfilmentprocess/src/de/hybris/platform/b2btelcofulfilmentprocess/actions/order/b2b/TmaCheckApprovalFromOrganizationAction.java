/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcofulfilmentprocess.actions.order.b2b;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Action responsible for checking if the order has been approved by the organization approver.
 *
 * @since 2105
 */
public class TmaCheckApprovalFromOrganizationAction extends AbstractAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(TmaCheckApprovalFromOrganizationAction.class);

	private static final String B2BCUSTOMERGROUP_REQUIRESAPPROVAL = "b2bcustomergroup_requiresapproval";

	public enum Transition
	{
		OK, NOK, NO_ANSWER;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	/**
	 * Checks if the order has been approved/rejected by the organization approver.
	 *
	 * @param process
	 * 		The order process
	 * @return OK if the order has been approved, NOK if the order has been rejected or NO_ANSWER if it is waiting for the approver's decision
	 */
	@Override
	public final String execute(final OrderProcessModel process)
	{
		LOG.debug("Organization approval process started.");

		final OrderModel order = process.getOrder();

		LOG.debug("Processing order '" + order.getCode() + "'.");

		if (OrderStatus.APPROVED.equals(order.getStatus()))
		{
			LOG.debug("Order '" + order.getCode() + "' approved by organization.");
			order.setStatus(OrderStatus.COMPLETED);
			getModelService().save(order);
			return Transition.OK.toString();
		}

		if (OrderStatus.REJECTED.equals(order.getStatus()))
		{
			LOG.debug("Order '" + order.getCode() + "' rejected by organization.");
			return Transition.NOK.toString();
		}

		if (!requiresApproval(order))
		{
			LOG.debug("Order '" + order.getCode() + "' does not require approval from organization. Order automatically approved.");
			order.setStatus(OrderStatus.COMPLETED);
			getModelService().save(order);
			return Transition.OK.toString();
		}

		order.setStatus(OrderStatus.PENDING_APPROVAL);
		getModelService().save(order);

		LOG.debug("Order pending approval from organization.");
		return Transition.NO_ANSWER.toString();
	}

	/**
	 * Checks if the provided order requires approval from organization approver.
	 *
	 * @param order
	 * 		The order
	 * @return True if the order requires approval, otherwise false.
	 */
	protected boolean requiresApproval(final OrderModel order)
	{
		final PrincipalModel customer = order.getUser();
		final Set<PrincipalGroupModel> customerGroups = customer.getAllGroups();

		return customerGroups.stream().anyMatch(
				(PrincipalGroupModel group) -> group.getUid().equals(B2BCUSTOMERGROUP_REQUIRESAPPROVAL));
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}

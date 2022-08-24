/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcofulfilmentprocess.actions.order.b2b;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Action responsible for checking if the order context is b2b or b2c.
 *
 * @since 2105
 */
public class TmaCheckOrderContextAction extends AbstractAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(TmaCheckOrderContextAction.class);

	public enum Transition
	{
		B2C, B2B, NOK;

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
	 * Checks if the order context is B2B or B2C.
	 *
	 * @param process
	 * 		The order process
	 * @return NOK in case of error, B2B if the order context is B2B or B2C in case of B2C orders
	 */
	@Override
	public final String execute(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (order == null)
		{
			LOG.error("Missing the order, exiting the process");
			return Transition.NOK.toString();
		}

		if (order.getUser() instanceof B2BCustomerModel)
		{
			return Transition.B2B.toString();
		}

		return Transition.B2C.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}

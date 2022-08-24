/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcofulfilmentprocess.actions.order.b2c;

import de.hybris.platform.b2btelcofulfilmentprocess.constants.B2btelcofulfilmentprocessConstants;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Action responsible for checking if the b2c fulfilment process finished successfully or failed.
 *
 * @since 2105
 */
public class TmaCheckB2cOrderProcessAction extends AbstractAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(TmaCheckB2cOrderProcessAction.class);

	private static final String SEPARATOR = "-";

	public enum Transition
	{
		SUCCESS, FAILED, ERROR, NO_ANSWER;

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
	 * Checks if the B2C order process finished.
	 *
	 * @param process
	 * 		The order process
	 * @return SUCCESS if the B2C order process finished successfully; FAILED if the B2C order process failed; ERROR if the B2C order process finished with errors; NO_ANSWER if the B2C order process is not finished
	 */
	@Override
	public final String execute(final OrderProcessModel process)
	{
		final OrderProcessModel childProcess = process.getChildProcesses().iterator().next();
		final OrderModel order = process.getOrder();
		final String processCode =
				Config.getString(B2btelcofulfilmentprocessConstants.B2C_ORDER_PROCESS_NAME, StringUtils.EMPTY) + SEPARATOR + order
						.getCode()
						+ SEPARATOR + System.currentTimeMillis();

		if (ProcessState.SUCCEEDED.equals(childProcess.getState()))
		{
			LOG.debug(String.format("Process %s finished successfully.", processCode));
			return Transition.SUCCESS.toString();
		}

		if (ProcessState.FAILED.equals(childProcess.getState()))
		{
			LOG.warn(String.format("Process %s failed.", processCode));
			return Transition.FAILED.toString();
		}

		if (ProcessState.ERROR.equals(childProcess.getState()))
		{
			LOG.error(String.format("Process %s finished with error.", processCode));
			return Transition.ERROR.toString();
		}

		LOG.debug(String.format("Process %s still running.", processCode));
		return Transition.NO_ANSWER.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}

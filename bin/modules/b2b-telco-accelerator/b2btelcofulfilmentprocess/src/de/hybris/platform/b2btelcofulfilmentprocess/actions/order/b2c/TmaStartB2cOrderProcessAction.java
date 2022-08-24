/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcofulfilmentprocess.actions.order.b2c;

import de.hybris.platform.b2btelcofulfilmentprocess.constants.B2btelcofulfilmentprocessConstants;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Action responsible for starting the B2C fulfilment process.
 *
 * @since 2105
 */
public class TmaStartB2cOrderProcessAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(TmaStartB2cOrderProcessAction.class);

	private static final String SEPARATOR = "-";

	private BusinessProcessService businessProcessService;

	public TmaStartB2cOrderProcessAction(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * Starts the B2C order process.
	 *
	 * @param process
	 * 		The order process
	 */
	@Override
	public void executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		final String processCode =
				Config.getString(B2btelcofulfilmentprocessConstants.B2C_ORDER_PROCESS_NAME, StringUtils.EMPTY) + SEPARATOR + order
						.getCode() + SEPARATOR + System.currentTimeMillis();
		final OrderProcessModel businessProcessModel = getBusinessProcessService().createProcess(processCode,
				Config.getString(B2btelcofulfilmentprocessConstants.B2C_ORDER_PROCESS_NAME, StringUtils.EMPTY));
		businessProcessModel.setOrder(order);
		businessProcessModel.setParentProcess(process);
		getModelService().save(businessProcessModel);
		getBusinessProcessService().startProcess(businessProcessModel);

		LOG.debug(String.format("Started the process %s", processCode));
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}
}

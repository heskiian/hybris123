/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofulfillmentprocess.service;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * Service handling B2C order fulfilment processes.
 *
 * @since 2105
 */
public interface TmaB2cOrderProcessFulfilmentService
{
	/**
	 * Triggers B2C order process end event.
	 *
	 * @param process
	 * 		The order process
	 */
	void triggerB2cOrderProcessEndEvent(final OrderProcessModel process);
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.actions;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;


/**
 * Class blocks default behavior of
 * de.hybris.platform.yacceleratorfulfilmentprocess.actions.consignment.SendDeliveryMessageAction class.
 */
public class SendDeliveryMessageActionStub extends AbstractProceduralAction<ConsignmentProcessModel>
{
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		// empty stub implementation
	}
}

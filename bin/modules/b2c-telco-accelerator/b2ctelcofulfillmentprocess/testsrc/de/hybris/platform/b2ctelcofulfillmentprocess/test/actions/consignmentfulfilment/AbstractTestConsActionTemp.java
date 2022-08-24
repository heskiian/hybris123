/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.test.actions.consignmentfulfilment;

import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.test.actions.TestActionTemp;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public abstract class AbstractTestConsActionTemp extends TestActionTemp
{
	private static final Logger LOG = Logger.getLogger(AbstractTestConsActionTemp.class);

	@Override
	public String execute(final BusinessProcessModel process) throws Exception
	{
		//getQueueService().actionExecuted(getParentProcess(process), this);
		LOG.info(getResult());
		return getResult();
	}


	public BusinessProcessModel getParentProcess(final BusinessProcessModel process)
	{
		final String parentCode = (String) getProcessParameterValue(process, B2ctelcofulfillmentprocessConstants.PARENT_PROCESS);
		return getBusinessProcessService().getProcess(parentCode);
	}
}

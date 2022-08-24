/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.jalo;

import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


public class B2ctelcofulfillmentprocessManager extends GeneratedB2ctelcofulfillmentprocessManager
{
	public static final B2ctelcofulfillmentprocessManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcofulfillmentprocessManager) em.getExtension(B2ctelcofulfillmentprocessConstants.EXTENSIONNAME);
	}

}

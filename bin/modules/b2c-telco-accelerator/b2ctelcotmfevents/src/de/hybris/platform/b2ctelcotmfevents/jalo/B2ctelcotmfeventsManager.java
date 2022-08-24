/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfevents.jalo;

import de.hybris.platform.b2ctelcotmfevents.constants.B2ctelcotmfeventsConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


public class B2ctelcotmfeventsManager extends GeneratedB2ctelcotmfeventsManager
{
	public static final B2ctelcotmfeventsManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcotmfeventsManager) em.getExtension(B2ctelcotmfeventsConstants.EXTENSIONNAME);
	}
	
}

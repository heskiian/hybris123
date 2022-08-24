/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptiontmfwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;

import org.apache.log4j.Logger;


public class UsageconsumptiontmfwebservicesManager extends GeneratedUsageconsumptiontmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UsageconsumptiontmfwebservicesManager.class.getName());

	public static final UsageconsumptiontmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (UsageconsumptiontmfwebservicesManager) em.getExtension(UsageconsumptiontmfwebservicesConstants.EXTENSIONNAME);
	}

}

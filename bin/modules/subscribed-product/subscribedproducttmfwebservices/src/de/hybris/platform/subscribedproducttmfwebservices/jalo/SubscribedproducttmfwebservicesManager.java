/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproducttmfwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;

import org.apache.log4j.Logger;


public class SubscribedproducttmfwebservicesManager extends GeneratedSubscribedproducttmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SubscribedproducttmfwebservicesManager.class.getName());

	public static final SubscribedproducttmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SubscribedproducttmfwebservicesManager) em.getExtension(SubscribedproducttmfwebservicesConstants.EXTENSIONNAME);
	}

}

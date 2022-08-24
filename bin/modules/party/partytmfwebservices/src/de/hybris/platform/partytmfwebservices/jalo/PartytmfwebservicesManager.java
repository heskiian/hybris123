/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partytmfwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;

import org.apache.log4j.Logger;


/**
 * Jalo class
 *
 * @since 2108
 */

public class PartytmfwebservicesManager extends GeneratedPartytmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PartytmfwebservicesManager.class.getName());

	public static final PartytmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PartytmfwebservicesManager) em.getExtension(PartytmfwebservicesConstants.EXTENSIONNAME);
	}

}

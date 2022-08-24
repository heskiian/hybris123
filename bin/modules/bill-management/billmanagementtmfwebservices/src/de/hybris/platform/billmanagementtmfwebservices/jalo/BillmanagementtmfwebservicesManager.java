/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.jalo;

import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


public class BillmanagementtmfwebservicesManager extends GeneratedBillmanagementtmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BillmanagementtmfwebservicesManager.class.getName());

	public static final BillmanagementtmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (BillmanagementtmfwebservicesManager) em.getExtension(BillmanagementtmfwebservicesConstants.EXTENSIONNAME);
	}

}

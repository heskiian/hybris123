/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscribedproductservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.subscribedproductservices.constants.SubscribedproductservicesConstants;
import org.apache.log4j.Logger;

public class SubscribedproductservicesManager extends GeneratedSubscribedproductservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SubscribedproductservicesManager.class.getName() );
	
	public static final SubscribedproductservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SubscribedproductservicesManager) em.getExtension(SubscribedproductservicesConstants.EXTENSIONNAME);
	}
	
}

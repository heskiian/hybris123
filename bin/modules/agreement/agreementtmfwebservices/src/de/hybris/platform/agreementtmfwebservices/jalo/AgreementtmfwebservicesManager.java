/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.agreementtmfwebservices.jalo;

import de.hybris.platform.agreementtmfwebservices.constants.AgreementtmfwebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AgreementtmfwebservicesManager extends GeneratedAgreementtmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( AgreementtmfwebservicesManager.class.getName() );
	
	public static final AgreementtmfwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AgreementtmfwebservicesManager) em.getExtension(AgreementtmfwebservicesConstants.EXTENSIONNAME);
	}
	
}

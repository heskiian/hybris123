/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.jalo;

import de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class B2ctelcoservicesManager extends GeneratedB2ctelcoservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( B2ctelcoservicesManager.class.getName() );
	
	public static final B2ctelcoservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcoservicesManager) em.getExtension(B2ctelcoservicesConstants.EXTENSIONNAME);
	}
	
}

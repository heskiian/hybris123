/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.b2ctelcowebservices.constants.B2ctelcowebservicesConstants;

@SuppressWarnings("PMD")
public class B2ctelcowebservicesManager extends GeneratedB2ctelcowebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( B2ctelcowebservicesManager.class.getName() );
	
	public static final B2ctelcowebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcowebservicesManager) em.getExtension(B2ctelcowebservicesConstants.EXTENSIONNAME);
	}
	
}

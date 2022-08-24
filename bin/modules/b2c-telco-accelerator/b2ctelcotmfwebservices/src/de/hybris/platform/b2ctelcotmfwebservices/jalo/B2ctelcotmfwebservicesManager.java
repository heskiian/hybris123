/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;

@SuppressWarnings("PMD")
public class B2ctelcotmfwebservicesManager extends GeneratedB2ctelcotmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( B2ctelcotmfwebservicesManager.class.getName() );
	
	public static final B2ctelcotmfwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcotmfwebservicesManager) em.getExtension(B2ctelcotmfwebservicesConstants.EXTENSIONNAME);
	}
	
}

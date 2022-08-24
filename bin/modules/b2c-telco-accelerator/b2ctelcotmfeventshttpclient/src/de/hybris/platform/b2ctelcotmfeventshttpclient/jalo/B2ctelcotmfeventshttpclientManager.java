/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfeventshttpclient.jalo;

import de.hybris.platform.b2ctelcotmfeventshttpclient.constants.B2ctelcotmfeventshttpclientConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


public class B2ctelcotmfeventshttpclientManager extends GeneratedB2ctelcotmfeventshttpclientManager
{
	public static final B2ctelcotmfeventshttpclientManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcotmfeventshttpclientManager) em.getExtension(B2ctelcotmfeventshttpclientConstants.EXTENSIONNAME);
	}
	
}

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.utilitiesstore.jalo;

import de.hybris.platform.utilitiesstore.constants.UtilitiesstoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class UtilitiesstoreManager extends GeneratedUtilitiesstoreManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UtilitiesstoreManager.class.getName());

	public static UtilitiesstoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (UtilitiesstoreManager) em.getExtension(UtilitiesstoreConstants.EXTENSIONNAME);
	}

}

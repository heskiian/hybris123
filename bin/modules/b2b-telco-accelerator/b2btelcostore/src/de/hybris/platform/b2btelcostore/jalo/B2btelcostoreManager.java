/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2btelcostore.jalo;

import de.hybris.platform.b2btelcostore.constants.B2btelcostoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class B2btelcostoreManager extends GeneratedB2btelcostoreManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(B2btelcostoreManager.class.getName());

	public static B2btelcostoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2btelcostoreManager) em.getExtension(B2btelcostoreConstants.EXTENSIONNAME);
	}

}

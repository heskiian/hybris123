/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcostore.jalo;

import de.hybris.platform.b2ctelcostore.constants.B2ctelcostoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class B2ctelcostoreManager extends GeneratedB2ctelcostoreManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(B2ctelcostoreManager.class.getName());

	public static B2ctelcostoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcostoreManager) em.getExtension(B2ctelcostoreConstants.EXTENSIONNAME);
	}

}

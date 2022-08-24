/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcofulfilmentprocess.jalo;

import de.hybris.platform.b2btelcofulfilmentprocess.constants.B2btelcofulfilmentprocessConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


/**
 * B2B Telco fulfilment process manager.
 *
 * @since 2105
 */
public class B2btelcofulfilmentprocessManager extends GeneratedB2btelcofulfilmentprocessManager
{
	public static final B2btelcofulfilmentprocessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2btelcofulfilmentprocessManager) em.getExtension(B2btelcofulfilmentprocessConstants.EXTENSIONNAME);
	}

}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccounttmfwebservices.jalo;

import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;


public class BillingaccounttmfwebservicesManager extends GeneratedBillingaccounttmfwebservicesManager
{
	public static final BillingaccounttmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (BillingaccounttmfwebservicesManager) em.getExtension(BillingaccounttmfwebservicesConstants.EXTENSIONNAME);
	}
}

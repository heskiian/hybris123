/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccountservices.jalo;

import de.hybris.platform.billingaccountservices.constants.BillingaccountservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class BillingaccountservicesManager extends GeneratedBillingaccountservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( BillingaccountservicesManager.class.getName() );
	
	public static final BillingaccountservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (BillingaccountservicesManager) em.getExtension(BillingaccountservicesConstants.EXTENSIONNAME);
	}
	
}

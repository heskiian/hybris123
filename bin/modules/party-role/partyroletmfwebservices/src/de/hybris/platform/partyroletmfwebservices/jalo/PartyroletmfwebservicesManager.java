/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyroletmfwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;

public class PartyroletmfwebservicesManager extends GeneratedPartyroletmfwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( PartyroletmfwebservicesManager.class.getName() );
	
	public static final PartyroletmfwebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PartyroletmfwebservicesManager) em.getExtension(PartyroletmfwebservicesConstants.EXTENSIONNAME);
	}
	
}

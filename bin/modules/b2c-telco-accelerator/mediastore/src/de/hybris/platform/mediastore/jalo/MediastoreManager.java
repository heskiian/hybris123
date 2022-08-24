/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediastore.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.mediastore.constants.MediastoreConstants;
import org.apache.log4j.Logger;

public class MediastoreManager extends GeneratedMediastoreManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MediastoreManager.class.getName() );
	
	public static final MediastoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MediastoreManager) em.getExtension(MediastoreConstants.EXTENSIONNAME);
	}
	
}

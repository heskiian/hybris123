/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfevents.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all B2ctelcotmfevents constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("deprecation")
public final class B2ctelcotmfeventsConstants extends GeneratedB2ctelcotmfeventsConstants
{
	public static final String EXTENSIONNAME = "b2ctelcotmfevents";

	// implement here constants used by this extension
	private static final String API_BASE_URL = EXTENSIONNAME + ".api.base.url";
	private static final String TMA_API_WEBROOT = EXTENSIONNAME + ".api.webmodule.webroot";
	private static final String TMA_EVENT_API_VERSION = EXTENSIONNAME + ".tmaEventApi.version";
	private static final String EVENT_API_HREF = EXTENSIONNAME + ".eventApi.href";

	public static final String EVENT_API_URL =
			Config.getParameter(API_BASE_URL) + Config.getParameter(TMA_API_WEBROOT) + Config.getParameter(TMA_EVENT_API_VERSION)
					+ Config.getParameter(EVENT_API_HREF);

	private B2ctelcotmfeventsConstants()
	{
		//empty to avoid instantiating this constant class
	}
}

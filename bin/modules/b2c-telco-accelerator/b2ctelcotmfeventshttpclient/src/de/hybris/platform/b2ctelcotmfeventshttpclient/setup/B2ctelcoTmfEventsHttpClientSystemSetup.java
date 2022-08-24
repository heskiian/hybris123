/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfeventshttpclient.setup;

import de.hybris.platform.b2ctelcotmfeventshttpclient.constants.B2ctelcotmfeventshttpclientConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import java.util.Collections;
import java.util.List;


/**
 * System setup for this extension used to import the tmfEvents sample data.
 *
 * @since 2105
 */
@SystemSetup(extension = B2ctelcotmfeventshttpclientConstants.EXTENSIONNAME)
public class B2ctelcoTmfEventsHttpClientSystemSetup extends AbstractSystemSetup
{
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return Collections.emptyList();
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/events/consumed-destinations.impex",
						B2ctelcotmfeventshttpclientConstants.EXTENSIONNAME), false);
	}
}

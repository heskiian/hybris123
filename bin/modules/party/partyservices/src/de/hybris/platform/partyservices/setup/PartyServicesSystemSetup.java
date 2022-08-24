/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.partyservices.setup;


import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.partyservices.constants.PartyservicesConstants;

import java.util.Collections;
import java.util.List;


/**
 * System setup for this extension used to import the sample data.
 *
 * @since 2108
 */

@SystemSetup(extension = PartyservicesConstants.EXTENSIONNAME)
public class PartyServicesSystemSetup extends AbstractSystemSetup
{
	private static final String API_TENANT = "api";

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return Collections.emptyList();
	}

	/**
	 * This method will be called during the system initialization and update process.
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		if (Registry.getCurrentTenant() != null && API_TENANT.equalsIgnoreCase(Registry.getCurrentTenant().getTenantID()))
		{
			importTestData();
		}
	}

	private void importTestData()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/oauthclients.impex", PartyservicesConstants.EXTENSIONNAME), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/individuals.impex", PartyservicesConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/organizations.impex", PartyservicesConstants.EXTENSIONNAME), false);
	}
}

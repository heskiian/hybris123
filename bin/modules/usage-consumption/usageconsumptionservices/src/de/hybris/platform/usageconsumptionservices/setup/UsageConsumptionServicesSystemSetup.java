/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptionservices.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.usageconsumptionservices.constants.UsageconsumptionservicesConstants;

import java.util.Collections;
import java.util.List;


/**
 * System setup for this extension used to import the sample data.
 *
 * @since 2108
 */
@SystemSetup(extension = UsageconsumptionservicesConstants.EXTENSIONNAME)
public class UsageConsumptionServicesSystemSetup extends AbstractSystemSetup
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
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/apidata/oauthclients.impex",
						UsageconsumptionservicesConstants.EXTENSIONNAME), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/apidata/usageconsumptions.impex",
						UsageconsumptionservicesConstants.EXTENSIONNAME), false);

	}
}

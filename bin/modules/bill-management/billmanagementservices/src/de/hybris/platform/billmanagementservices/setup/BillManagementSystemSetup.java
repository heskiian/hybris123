/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementservices.setup;

import de.hybris.platform.billmanagementservices.constants.BillmanagementservicesConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import java.util.Collections;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes. For further info, check the
 * documentation regarding Hooks for Initialization and Update Process.
 *
 * @since 2108
 */
@SystemSetup(extension = BillmanagementservicesConstants.EXTENSIONNAME)
public class BillManagementSystemSetup extends AbstractSystemSetup
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
						BillmanagementservicesConstants.EXTENSIONNAME), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/apidata/billmanagement.impex",
						BillmanagementservicesConstants.EXTENSIONNAME), false);

	}

}

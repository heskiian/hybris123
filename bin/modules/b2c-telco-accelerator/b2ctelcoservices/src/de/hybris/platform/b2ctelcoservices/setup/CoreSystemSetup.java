/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.setup;

import de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes. For further info, check the
 * documentation regarding Hooks for Initialization and Update Process.
 */
@SystemSetup(extension = B2ctelcoservicesConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	private static final String IMPORT_ACCESS_RIGHTS = "accessRights";

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import.
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		importImpexFile(context, "/b2ctelcoservices/import/common/b2ctelcoservices-constraints.impex");
	}

}

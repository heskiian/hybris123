/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.setup;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @since 1911
 */
@SystemSetup(extension = UpilintegrationservicesConstants.EXTENSIONNAME)
public class UpilintegrationservicesSystemSetup extends AbstractSystemSetup
{
	public static final String UTILITIES = "utilities";
	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String UTILITIES_INDEX = "utilitiesIndex";

	private SampleDataImportService sampleDataImportService;

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	/**
	 * This method will be called during initialization and system update.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<>();

		final ImportData utilitiesImportData = new ImportData();
		utilitiesImportData.setProductCatalogName(UTILITIES);
		utilitiesImportData.setContentCatalogNames(Arrays.asList(UTILITIES));
		utilitiesImportData.setStoreNames(Arrays.asList(UTILITIES));
		importData.add(utilitiesImportData);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productimport/essentialdata-SimpleProductOffering.impex",
						UpilintegrationservicesConstants.EXTENSIONNAME),
				true);

		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		executeSolrIndexerCronJob(UTILITIES_INDEX, true);
	}

	protected SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}

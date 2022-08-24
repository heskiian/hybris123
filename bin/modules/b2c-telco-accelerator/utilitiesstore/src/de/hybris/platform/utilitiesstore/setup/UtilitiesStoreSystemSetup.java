/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.utilitiesstore.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.b2ctelcoservices.setup.CoreSystemSetup;
import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.utilitiesstore.constants.UtilitiesstoreConstants;


/**
 * System setup.
 */
@SystemSetup(extension = UtilitiesstoreConstants.EXTENSIONNAME)
public class UtilitiesStoreSystemSetup extends AbstractSystemSetup
{
	public static final String UTILITIES = "utilities";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	private static final String UTILITIES_CONTENT_CATALOG = "utilitiesContentCatalog";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private CoreSystemSetup coreSystemSetup;
	private ConfigurationService configurationService;

	public UtilitiesStoreSystemSetup(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));
		return params;
	}

	/**
	 * This method will be called during the system initialization.
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
		

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		if (Registry.getCurrentTenant() != null && "api".equalsIgnoreCase(Registry.getCurrentTenant().getTenantID()))
		{
			importTestData();
		}
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);
		processCockpit(context, importAccessRights,
				String.format("/%s/import/sampledata/cockpits/productcockpit/productcockpit-users.impex",
						UtilitiesstoreConstants.EXTENSIONNAME));
		// sync the telco content catalog again as sbg is using the telco content catalog, too
		executeCatalogSyncJob(context, UTILITIES_CONTENT_CATALOG);

		// as promotions are not catalog aware,
		// we must import them into the online catalog which is only possible after the synchronization
		// as some online products are required
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/promotions.impex",
						UtilitiesstoreConstants.EXTENSIONNAME, UTILITIES),
				false);

		executeSolrIndexerCronJob("utilitiesIndex", true);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/customerInventory/customerInventory.impex",
						UtilitiesstoreConstants.EXTENSIONNAME),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/common/user-groups.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);

	}

	protected void processCockpit(final SystemSetupContext context, final boolean importAccessRights, final String... files)
	{
		if (importAccessRights)
		{
			for (final String file : files)
			{
				importImpexFile(context, file);
			}
		}
	}

	private void importTestData()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/customers.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/addresses.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/paymentdata.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/shoppingcarts.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/policies.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/apidata/productofferings.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/apidata/stocklevels.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);

		if (getConfigurationService().getConfiguration().getBoolean(UtilitiesstoreConstants.TEST_DATA_API_PRODUCTOFFERINGPRICES))
		{
			importTestDataProductOfferingPrices();
		}
		else
		{
			importTestDataCharges();
		}

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/orders.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
	}

	private void importTestDataProductOfferingPrices()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/productofferingprices/productofferingprices.impex", UtilitiesstoreConstants.EXTENSIONNAME),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/productofferingprices/prices.impex", UtilitiesstoreConstants.EXTENSIONNAME),
				false);
	}

	private void importTestDataCharges()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/charges/prices.impex", UtilitiesstoreConstants.EXTENSIONNAME), false);
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Autowired
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Autowired
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}

	public CoreSystemSetup getCoreSystemSetup()
	{
		return coreSystemSetup;
	}

	@Autowired
	public void setCoreSystemSetup(CoreSystemSetup coreSystemSetup)
	{
		this.coreSystemSetup = coreSystemSetup;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}

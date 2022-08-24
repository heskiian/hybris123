/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcostore.setup;

import de.hybris.platform.b2btelcostore.constants.B2btelcostoreConstants;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * System setup.
 *
 * @since 2105
 */
@SystemSetup(extension = B2btelcostoreConstants.EXTENSIONNAME)
public class B2btelcoStoreSystemSetup extends AbstractSystemSetup
{
	public static final String B2BTELCO = "b2btelco";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String B2BTELCO_CONTENT_CATALOG = "b2btelcoContentCatalog";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private CoreSystemSetup coreSystemSetup;
	private ConfigurationService configurationService;

	public B2btelcoStoreSystemSetup(final CoreDataImportService coreDataImportService,
			final SampleDataImportService sampleDataImportService, final ConfigurationService configurationService)
	{
		this.coreDataImportService = coreDataImportService;
		this.sampleDataImportService = sampleDataImportService;
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
	 *
	 * @param context
	 * 		the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<>();
		final ImportData b2btelcoImportData = new ImportData();
		b2btelcoImportData.setProductCatalogName(B2BTELCO);
		b2btelcoImportData.setContentCatalogNames(Arrays.asList(B2BTELCO));
		b2btelcoImportData.setStoreNames(Arrays.asList(B2BTELCO));
		importData.add(b2btelcoImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/common/userpricegroup.impex", B2btelcostoreConstants.EXTENSIONNAME), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/common/user-groups.impex", B2btelcostoreConstants.EXTENSIONNAME), false);

		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		if (Registry.getCurrentTenant() != null && "api".equalsIgnoreCase(Registry.getCurrentTenant().getTenantID()))
		{
			importTestData();
		}

		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);
		processCockpit(context, importAccessRights, String.format(
				"/%s/import/sampledata/cockpits/productcockpit/productcockpit-users.impex", B2btelcostoreConstants.EXTENSIONNAME));
		// sync the b2b telco content catalog again as sbg is using the b2b telco content catalog, too
		executeCatalogSyncJob(context, B2BTELCO_CONTENT_CATALOG);

		// as promotions are not catalog aware,
		// we must import them into the online catalog which is only possible after the synchronization
		// as some online products are required
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/promotions/promotions-engine-configuration.impex",
						B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/promotions.impex",
						B2btelcostoreConstants.EXTENSIONNAME, B2BTELCO), false);

		executeSolrIndexerCronJob("b2btelcoIndex", true);

		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/customerInventory/customerInventory.impex",
				B2btelcostoreConstants.EXTENSIONNAME), false);

		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/productimport/essentialdata-SimpleProductOffering.impex",
						B2btelcostoreConstants.EXTENSIONNAME), true);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/productimport/projectdata-SimpleProductOffering.impex",
						B2btelcostoreConstants.EXTENSIONNAME), true);
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
				String.format("/%s/import/sampledata/apidata/solr.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/user-groups.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/customers.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/promotiongroups.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/cmssites.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/basestores.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/catalogs.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/categories.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/productspecifications.impex", B2btelcostoreConstants.EXTENSIONNAME),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/productofferings.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/stocklevels.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/bundledprodofferoption.impex", B2btelcostoreConstants.EXTENSIONNAME),
				false);

		if (getConfigurationService().getConfiguration().getBoolean(B2btelcostoreConstants.TEST_DATA_API_PRODUCTOFFERINGPRICES))
		{
			importTestDataProductOfferingPrices();
		}
		else
		{
			importTestDataCharges();
		}

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/discounts.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/discountrows.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/reviews.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/addresses.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/paymentdata.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/coupons.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/promotions.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/shoppingcarts.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/orders.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/subscriptiondata.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/policies.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/pos-stocklevel.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/attachments.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
	}

	private void importTestDataProductOfferingPrices()
	{
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/apidata/productofferingprices/productofferingprices.impex",
						B2btelcostoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/apidata/productofferingprices/prices.impex",
				B2btelcostoreConstants.EXTENSIONNAME), false);
	}

	private void importTestDataCharges()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/charges/prices.impex", B2btelcostoreConstants.EXTENSIONNAME), false);
	}


	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	public CoreSystemSetup getCoreSystemSetup()
	{
		return coreSystemSetup;
	}

	@Autowired
	public void setCoreSystemSetup(final CoreSystemSetup coreSystemSetup)
	{
		this.coreSystemSetup = coreSystemSetup;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}

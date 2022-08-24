/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediastore.setup;

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
import de.hybris.platform.mediastore.constants.MediastoreConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * System setup of the Media Store.
 *
 * @since 1810
 */
@SystemSetup(extension = MediastoreConstants.EXTENSIONNAME)
public class MediaStoreSystemSetup extends AbstractSystemSetup
{
	private static final String MEDIA = "media";
	private static final List<String> STORE_NAMES = Collections.singletonList(MEDIA);
	private static final List<String> CONTENT_CATALOG_NAMES = Collections.singletonList(MEDIA);

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	private static final String MEDIA_CONTENT_CATALOG = "mediaContentCatalog";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private CoreSystemSetup coreSystemSetup;

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

		final ImportData mediaImportData = new ImportData();
		mediaImportData.setProductCatalogName(MEDIA);
		mediaImportData.setContentCatalogNames(CONTENT_CATALOG_NAMES);
		mediaImportData.setStoreNames(STORE_NAMES);
		importData.add(mediaImportData);

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
						MediastoreConstants.EXTENSIONNAME));
		// sync the media content catalog again as sbg is using the media content catalog, too
		executeCatalogSyncJob(context, MEDIA_CONTENT_CATALOG);

		// as promotions are not catalog aware,
		// we must import them into the online catalog which is only possible after the synchronization
		// as some online products are required
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/promotions.impex",
						MediastoreConstants.EXTENSIONNAME, MEDIA), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/common/user-groups.impex", MediastoreConstants.EXTENSIONNAME), false);

		executeSolrIndexerCronJob("mediaIndex", true);
		
	}

	private void importTestData()
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/customers.impex", MediastoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/addresses.impex", MediastoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/paymentdata.impex", MediastoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/shoppingcarts.impex", MediastoreConstants.EXTENSIONNAME), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/apidata/orders.impex", MediastoreConstants.EXTENSIONNAME), false);
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

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
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
}

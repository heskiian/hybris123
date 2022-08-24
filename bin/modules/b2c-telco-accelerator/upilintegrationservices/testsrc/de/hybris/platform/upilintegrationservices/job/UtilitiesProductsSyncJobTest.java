/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.job;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.model.IsuProductSyncCronJobModel;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link UtilitiesProdcutsSyncJob}.
 *
 * @since 1911
 */
@IntegrationTest
public class UtilitiesProductsSyncJobTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private CronJobService cronJobService;
	@Resource
	private UtilitiesProductsSyncJob utilitiesProductsSyncJob;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_product-prices.impex", "utf-8");
	}

	@Test
	public void performBatchSyncTest()
	{
		final IsuProductSyncCronJobModel isuProductSyncCronJob = getIsuSyncCronJob();
		final PerformResult result = utilitiesProductsSyncJob.perform(isuProductSyncCronJob);
		Assert.assertNotNull(result);
	}

	@Test
	public void performItemSyncTest()
	{
		final IsuProductSyncCronJobModel isuProductSyncCronJob = getIsuSyncCronJob();
		isuProductSyncCronJob.setAppliedPricePlans(getPricePlanList());
		final PerformResult result = utilitiesProductsSyncJob.perform(isuProductSyncCronJob);
		Assert.assertNotNull(result);
	}

	private IsuProductSyncCronJobModel getIsuSyncCronJob()
	{
		final IsuProductSyncCronJobModel isuProductSyncCronJob = new IsuProductSyncCronJobModel();
		isuProductSyncCronJob
				.setAppliedCatalogVersions(Arrays.asList(catalogVersionService.getCatalogVersion("testCatalog", "Online")));
		isuProductSyncCronJob.setAppliedProductTypes(getSpecTypeList());
		return isuProductSyncCronJob;
	}

	private List<SubscriptionPricePlanModel> getPricePlanList()
	{
		final String queryStr = "select {pk} from {SubscriptionPricePlan} where {code}='test_pp_electric_plan'";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		final SearchResult<SubscriptionPricePlanModel> specTypeList = flexibleSearchService.search(query);
		return specTypeList.getResult();
	}

	private List<TmaProductSpecTypeModel> getSpecTypeList()
	{
		final String queryStr = "select {pk} from {TmaProductSpecType}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		final SearchResult<TmaProductSpecTypeModel> specTypeList = flexibleSearchService.search(query);
		return specTypeList.getResult();
	}

}

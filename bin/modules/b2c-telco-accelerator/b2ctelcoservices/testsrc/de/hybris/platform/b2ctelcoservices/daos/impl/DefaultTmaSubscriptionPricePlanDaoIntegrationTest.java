/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaGenericSearchProcessor;
import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.ChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link DefaultTmaSubscriptionPricePlanDao} class.
 *
 * @since 1907
 */
@IntegrationTest
public class DefaultTmaSubscriptionPricePlanDaoIntegrationTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaSubscriptionPricePlanDaoIntegrationTest.class);

	private static final String BUNDLED_PO = "BundledPO";
	private static final String BUNDLED_PO_1 = "BundledPO1";
	private static final String PRODUCT = "P1";
	private static final String PRODUCT_2 = "P2";
	private static final String PRODUCT_3 = "P3";
	private static final String PRODUCT_4 = "P4";
	private static final String PRODUCT_6 = "P6";
	private static final String PRODUCT_5 = "P5";
	private static final String PRODUCT_7 = "P7";
	private static final String USD = "USD";
	private static final String MOBILE_DEAL = "mobileDeal1";
	private static final HashSet<TmaProcessType> PROCESS_TYPE_ACQUISITION = new HashSet<>(
			Collections.singletonList(TmaProcessType.ACQUISITION));

	@Resource
	private DefaultTmaSubscriptionPricePlanDao defaultTmaSubscriptionPricePlanDao;

	@Resource
	private ProductService productService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private TmaGenericSearchProcessor tmaPriceRowFilterSearchProcessor;

	@Resource
	private TmaGenericSearchProcessor tmaSppFilterSearchProcessor;

	@Before
	public void setUp() throws Exception
	{
		LOG.info("Creating data for DefaultTmaSubscriptionPricePlanDaoIntegrationTest ...");
		final long startTime = System.currentTimeMillis();
		importCsv("/test/impex/testPriceRetrieval.impex", "utf-8");
		LOG.info("Finished creating data for DefaultTmaSubscriptionPricePlanDaoIntegrationTest in "
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	@Test
	public void testRetrieveSPP()
	{
		final ProductModel product = productService.getProductForCode(PRODUCT);
		final TmaPriceContext priceContext = createPriceContext(product, PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> applicableSubscriptionPricePlans = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext);

		Assert.assertEquals(1, applicableSubscriptionPricePlans.size());

		final SubscriptionPricePlanModel spp = applicableSubscriptionPricePlans.iterator().next();
		Assert.assertEquals(2, spp.getOneTimeChargeEntries().size());

		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = applicableSubscriptionPricePlans.iterator().next()
				.getOneTimeChargeEntries();

		final HashSet<Double> priceValues = oneTimeChargeEntries.stream().map(ChargeEntryModel::getPrice)
				.collect(Collectors.toCollection(HashSet::new));

		Assert.assertTrue(priceValues.contains(5.75));
		Assert.assertTrue(priceValues.contains(10.0));

		Assert.assertEquals(1, spp.getRecurringChargeEntries().size());
		Assert.assertEquals(20.99, spp.getRecurringChargeEntries().iterator().next().getPrice(), 0);

	}

	@Test
	public void testRetrievePriceRow()
	{
		final ProductModel product = productService.getProductForCode(PRODUCT_2);

		final TmaPriceContext priceContext = createPriceContext(product, PROCESS_TYPE_ACQUISITION);

		final Set<PriceRowModel> applicablePriceRows = defaultTmaSubscriptionPricePlanDao.findApplicablePriceRows(priceContext);

		Assert.assertEquals(1, applicablePriceRows.size());
		Assert.assertEquals(50.0, applicablePriceRows.iterator().next().getPrice(), 0);
	}

	@Test
	public void testRetrieveSppForBpoWithRequiredProduct()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO_1);
		final ProductModel product = productService.getProductForCode(PRODUCT_6);
		final ProductModel product2 = productService.getProductForCode(PRODUCT_5);

		final TmaPriceContext priceContext = createBpoPriceContext(bpo, product, createRequiredProductsList(product2),
				PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> sPPsWithAffected = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext);

		Assert.assertEquals(1, sPPsWithAffected.size());

		final SubscriptionPricePlanModel subscriptionPricePlanModel = sPPsWithAffected.iterator().next();

		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = subscriptionPricePlanModel.getOneTimeChargeEntries();
		Assert.assertEquals(1, oneTimeChargeEntries.size());

		final Double price = oneTimeChargeEntries.iterator().next().getPrice();
		Assert.assertEquals(15.0, price, 0);
	}

	@Test
	public void testRetrieveSppForBpoWithWrongRequiredProduct()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO_1);
		final ProductModel product = productService.getProductForCode(PRODUCT_6);
		final ProductModel product3 = productService.getProductForCode(PRODUCT);

		final TmaPriceContext priceContext2 = createBpoPriceContext(bpo, product, createRequiredProductsList(product3),
				PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> sppWithWrongRequired = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext2);

		Assert.assertEquals(0, sppWithWrongRequired.size());
	}

	@Test
	public void testRetrieveSppForBpoWithRequiredProductClass()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO);
		final ProductModel product = productService.getProductForCode(PRODUCT);
		final ProductModel product2 = productService.getProductForCode(PRODUCT_2);

		final TmaPriceContext priceContext = createBpoPriceContext(bpo, product, createRequiredProductsList(product2),
				PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> compatibleSPPs = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext);

		Assert.assertEquals(1, compatibleSPPs.size());

		final SubscriptionPricePlanModel subscriptionPricePlan = compatibleSPPs.iterator().next();

		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = subscriptionPricePlan.getOneTimeChargeEntries();
		Assert.assertEquals(1, oneTimeChargeEntries.size());

		final Double price = oneTimeChargeEntries.iterator().next().getPrice();
		Assert.assertEquals(5.0, price, 0);
	}

	@Test
	public void testRetrieveSppForBpoWithRequiredProductAndRequiredProductClass()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO);
		final ProductModel product = productService.getProductForCode(PRODUCT);
		final ProductModel product2 = productService.getProductForCode(PRODUCT_2);

		final TmaPriceContext priceContext = createBpoPriceContext(bpo, product, createRequiredProductsList(product2),
				PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> compatibleSPPs = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext);

		Assert.assertEquals(1, compatibleSPPs.size());

		final SubscriptionPricePlanModel subscriptionPricePlanModel = compatibleSPPs.iterator().next();

		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = subscriptionPricePlanModel.getOneTimeChargeEntries();
		Assert.assertEquals(1, oneTimeChargeEntries.size());

		final Double price = oneTimeChargeEntries.iterator().next().getPrice();
		Assert.assertEquals(5.0, price, 0);
	}

	@Test
	public void testRetrieveSppForBPOWith2RequiredPosAndRequiredClass()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO);
		final ProductModel product = productService.getProductForCode(PRODUCT);
		final ProductModel product2 = productService.getProductForCode(PRODUCT_2);
		final ProductModel product3 = productService.getProductForCode(PRODUCT_3);

		final TmaPriceContext priceContext = createBpoPriceContext(bpo, product, createRequiredProductsList(product2, product3),
				PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> compatibleSPPs = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext);

		Assert.assertEquals(2, compatibleSPPs.size());

		final HashSet<Double> priceValues = compatibleSPPs.stream().map(PriceRowModel::getPrice)
				.collect(Collectors.toCollection(HashSet::new));

		Assert.assertTrue(priceValues.contains(5.0));
		Assert.assertTrue(priceValues.contains(2.0));
	}

	@Test
	public void testRetrieveSppForBPOWith3RequiredPosAndRequiredClass()
	{
		final ProductModel bpo = productService.getProductForCode(BUNDLED_PO);
		final ProductModel product = productService.getProductForCode(PRODUCT);
		final ProductModel product2 = productService.getProductForCode(PRODUCT_2);
		final ProductModel product3 = productService.getProductForCode(PRODUCT_3);
		final ProductModel product4 = productService.getProductForCode(PRODUCT_4);
		final TmaPriceContext priceContext2 = createBpoPriceContext(bpo, product,
				createRequiredProductsList(product2, product3, product4), PROCESS_TYPE_ACQUISITION);

		final Set<SubscriptionPricePlanModel> compatibleSPPs = defaultTmaSubscriptionPricePlanDao
				.findApplicableSubscriptionPricePlans(priceContext2);

		Assert.assertEquals(3, compatibleSPPs.size());

		final HashSet<Double> priceValues = compatibleSPPs.stream().map(PriceRowModel::getPrice)
				.collect(Collectors.toCollection(HashSet::new));

		Assert.assertTrue(priceValues.contains(5.0));
		Assert.assertTrue(priceValues.contains(2.0));
		Assert.assertTrue(priceValues.contains(1.0));
	}

	@Test
	public void testfilterPricesForContext() throws TmaSearchQueryException
	{
		final ProductModel product = productService.getProductForCode(PRODUCT_2);
		final GenericQuery query = new GenericQuery(PriceRowModel._TYPECODE, true);
		final TmaPriceContext priceContext = createPriceContext(product, PROCESS_TYPE_ACQUISITION);
		tmaSppFilterSearchProcessor.enhanceQuery(query, priceContext);
		tmaPriceRowFilterSearchProcessor.enhanceQuery(query, priceContext);
		final Set<PriceRowModel> resultingPrices = defaultTmaSubscriptionPricePlanDao.filterPricesForContext(priceContext);
		Assert.assertNotNull(resultingPrices);
	}

	@Test
	public void testFindAllApplicablePricesForContext()
	{
		final ProductModel product = productService.getProductForCode(PRODUCT_7);
		final TmaPriceContext priceContext = createPriceContext(product, PROCESS_TYPE_ACQUISITION);
		final Set<PriceRowModel> allApplicablePricesForContext = defaultTmaSubscriptionPricePlanDao
				.findAllApplicablePricesForContext(priceContext);

		Assert.assertEquals(2, allApplicablePricesForContext.size());
	}

	protected Set<ProductModel> createRequiredProductsList(final ProductModel... required)
	{
		return new HashSet<>(Arrays.asList(required));
	}

	protected TmaPriceContext createBpoPriceContext(final ProductModel bpo, final ProductModel affected,
			final Set<ProductModel> required, final HashSet<TmaProcessType> processTypes)
	{
		final TmaPriceContext context = createPriceContext(bpo, processTypes);
		context.setAffectedProduct(affected);
		context.setRequiredProducts(required);
		return context;
	}

	protected TmaPriceContext createPriceContext(final ProductModel product, final HashSet<TmaProcessType> processTypes)
	{
		final TmaPriceContext priceContext = new TmaPriceContext();
		priceContext.setProduct(product);
		priceContext.setCurrency(commonI18NService.getCurrency(USD));
		priceContext.setProcessTypes(processTypes);

		return priceContext;
	}
}

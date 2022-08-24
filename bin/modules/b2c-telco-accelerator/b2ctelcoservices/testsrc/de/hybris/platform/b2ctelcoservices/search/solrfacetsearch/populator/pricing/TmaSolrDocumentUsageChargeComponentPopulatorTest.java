/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProdUsageSpecification;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentUsageChargeComponentPop;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Default Unit Test for the {@link TmaSolrDocumentUsageChargeComponentPopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaSolrDocumentUsageChargeComponentPopulatorTest extends TmaSolrDocumentComponentPopPopulatorTest
{

	private static final int DEFAULT_TIER_START = 100;
	private static final int DEFAULT_TIER_END = 200;
	private static final String DEFAULT_USAGE_UNIT = "SMS";

	private TmaSolrDocumentUsageChargeComponentPopulator ucPopulator;
	private TmaSolrDocumentUsageChargeComponentPop solrDocumentUcPop;

	@Mock
	private Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvConverter;

	@Mock
	private Converter<TmaProductUsageSpecificationModel, TmaSolrDocumentProdUsageSpecification> prodUsageSpecificationConverter;

	@Mock
	private Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter;

	@Before
	public void setUp()
	{
		super.setUp();

		ucPopulator = new TmaSolrDocumentUsageChargeComponentPopulator(pscvConverter, prodUsageSpecificationConverter,
				pricingLogicAlgorithmConverter);
		solrDocumentUcPop = new TmaSolrDocumentUsageChargeComponentPop();
	}

	@Test
	public void testPopulateSolrDocumentPopFromUsageChargeSourceWithoutTiers()
	{
		final TmaUsageProdOfferPriceChargeModel ucModel = getUcPopWithoutTiers();
		ucPopulator.populate(ucModel, solrDocumentUcPop);

		assertComponentPopFieldsArePopulatedCorrectly(solrDocumentUcPop);
		assertNull(solrDocumentUcPop.getTierStart());
		assertNull(solrDocumentUcPop.getTierEnd());
	}

	@Test
	public void testPopulateSolrDocumentPopFromUsageChargeSourceWithTiers()
	{
		final TmaUsageProdOfferPriceChargeModel ucModel = getUcPopWithTiers();
		ucPopulator.populate(ucModel, solrDocumentUcPop);

		assertComponentPopFieldsArePopulatedCorrectly(solrDocumentUcPop);
		assertEquals(DEFAULT_TIER_START, solrDocumentUcPop.getTierStart(), 0.0);
		assertEquals(DEFAULT_TIER_END, solrDocumentUcPop.getTierEnd(), 0.0);
	}

	@Test
	public void testPopulateSolrDocumentPopFromUsageChargeSourceWithUsageUnit()
	{
		final TmaUsageProdOfferPriceChargeModel ucModel = getUcPopWithUsageUnit();
		ucPopulator.populate(ucModel, solrDocumentUcPop);

		assertComponentPopFieldsArePopulatedCorrectly(solrDocumentUcPop);
		assertEquals(DEFAULT_USAGE_UNIT, solrDocumentUcPop.getUsageUnit().getId());
	}


	private TmaUsageProdOfferPriceChargeModel getUcPopWithoutTiers()
	{
		final TmaUsageProdOfferPriceChargeModel ucPopModel = new TmaUsageProdOfferPriceChargeModel();
		setLocaleOnModel(ucPopModel);

		ucPopModel.setId(DEFAULT_ID);
		ucPopModel.setName(DEFAULT_NAME);
		ucPopModel.setDescription(DEFAULT_DESCRIPTION);
		ucPopModel.setPriceEvent(getPriceFrequency());
		ucPopModel.setValue(DEFAULT_PRICE_VALUE);
		ucPopModel.setCurrency(getCurrency());

		return ucPopModel;
	}

	private TmaUsageProdOfferPriceChargeModel getUcPopWithTiers()
	{
		final TmaUsageProdOfferPriceChargeModel ucPopModel = getUcPopWithoutTiers();
		ucPopModel.setTierStart(DEFAULT_TIER_START);
		ucPopModel.setTierEnd(DEFAULT_TIER_END);

		return ucPopModel;
	}

	private TmaUsageProdOfferPriceChargeModel getUcPopWithUsageUnit()
	{
		final TmaUsageProdOfferPriceChargeModel ucPopModel = getUcPopWithTiers();
		ucPopModel.setUsageUnit(getUsageUnit());

		return ucPopModel;
	}

	private UsageUnitModel getUsageUnit()
	{
		final UsageUnitModel usageUnit = new UsageUnitModel();
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) usageUnit.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		usageUnit.setId(DEFAULT_USAGE_UNIT);
		usageUnit.setAccumulative(true);

		return usageUnit;
	}

}

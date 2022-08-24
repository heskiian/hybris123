/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentRecurringChargeComponentPop;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Default Unit Test for the {@link TmaSolrDocumentRecurringChargeComponentPopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaSolrDocumentRecurringChargeComponentPopulatorTest extends TmaSolrDocumentComponentPopPopulatorTest
{
	private TmaSolrDocumentRecurringChargeComponentPopulator rcPopulator;
	private TmaSolrDocumentRecurringChargeComponentPop solrDocumentRcPop;

	@Mock
	private Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvConverter;

	@Mock
	private Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter;

	@Before
	public void setUp()
	{
		super.setUp();

		rcPopulator = new TmaSolrDocumentRecurringChargeComponentPopulator(pscvConverter, pricingLogicAlgorithmConverter);
		solrDocumentRcPop = new TmaSolrDocumentRecurringChargeComponentPop();
	}

	@Test
	public void testPopulateSolrDocumentPopFromRecurringChargeSourceWithNoCycleEnd()
	{
		//		given a One Time Charge Pop Model as Source
		final TmaRecurringProdOfferPriceChargeModel rcModel = getRcPopWithoutCycleEnd();

		//		when the rcPopulator is called
		rcPopulator.populate(rcModel, solrDocumentRcPop);

		//		then the populated solr pop is returned
		assertComponentPopFieldsArePopulatedCorrectly(solrDocumentRcPop);
		assertEquals(DEFAULT_CYCLE_START, solrDocumentRcPop.getCycleStart(), 0.0);
		assertNull(solrDocumentRcPop.getCycleEnd());
	}

	@Test
	public void testPopulateSolrDocumentPopFromRecurringChargeSourceWithCycleEnd()
	{
		final TmaRecurringProdOfferPriceChargeModel rcModel = getRcPopWithCycleEnd();
		rcPopulator.populate(rcModel, solrDocumentRcPop);

		assertComponentPopFieldsArePopulatedCorrectly(solrDocumentRcPop);
		assertEquals(DEFAULT_CYCLE_START, solrDocumentRcPop.getCycleStart(), 0.0);
		assertEquals(DEFAULT_CYCLE_END, solrDocumentRcPop.getCycleEnd(), 0.0);
	}

	private TmaRecurringProdOfferPriceChargeModel getRcPopWithCycleEnd()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = getRcPopWithoutCycleEnd();
		rcPopModel.setCycleEnd(DEFAULT_CYCLE_END);

		return rcPopModel;
	}

	private TmaRecurringProdOfferPriceChargeModel getRcPopWithoutCycleEnd()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = new TmaRecurringProdOfferPriceChargeModel();
		setLocaleOnModel(rcPopModel);

		rcPopModel.setId(DEFAULT_ID);
		rcPopModel.setName(DEFAULT_NAME);
		rcPopModel.setDescription(DEFAULT_DESCRIPTION);
		rcPopModel.setPriceEvent(getPriceFrequency());
		rcPopModel.setValue(DEFAULT_PRICE_VALUE);
		rcPopModel.setCurrency(getCurrency());
		rcPopModel.setCycleStart(DEFAULT_CYCLE_START);

		return rcPopModel;
	}
}

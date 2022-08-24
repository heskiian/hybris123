/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaComponentProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentOneTimeChargeComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPricingLogicAlgorithm;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;


/**
 * Default Unit Test for the {@link TmaSolrDocumentComponentPopPopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaSolrDocumentComponentPopPopulatorTest extends TmaSolrDocumentProductOfferingPricePopulatorTest
{
	private TmaSolrDocumentComponentPopPopulator<TmaComponentProdOfferPriceModel, TmaSolrDocumentComponentPop> componentPopulator;
	private TmaSolrDocumentComponentPop solrDocumentComponentPop;

	@Mock
	private Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvConverter;

	@Mock
	private Converter<TmaPricingLogicAlgorithmModel, TmaSolrDocumentPricingLogicAlgorithm> pricingLogicAlgorithmConverter;

	@Before
	public void setUp()
	{
		super.setUp();

		componentPopulator = new TmaSolrDocumentComponentPopPopulator<>(pscvConverter, pricingLogicAlgorithmConverter);
		solrDocumentComponentPop = new TmaSolrDocumentOneTimeChargeComponentPop();
	}

	@Test
	public void testPopulateSolrDocumentPopFromOneTimeChargeSourceWithNoPriceEvent()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPopWithoutPriceEvent();
		componentPopulator.populate(otcPopModel, solrDocumentComponentPop);
		assertPopFieldsArePopulatedCorrectly(solrDocumentComponentPop);
	}

	@Test
	public void testPopulateSolrDocumentPopFromOneTimeChargeSourceWithPriceEvent()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPopWithPriceEvent();
		componentPopulator.populate(otcPopModel, solrDocumentComponentPop);

		assertEquals(DEFAULT_ID, solrDocumentComponentPop.getId());
		assertEquals(DEFAULT_NAME, solrDocumentComponentPop.getName());
		assertEquals(DEFAULT_DESCRIPTION, solrDocumentComponentPop.getDescription());
		assertEquals(DEFAULT_PRICE_EVENT, solrDocumentComponentPop.getPriceEvent().getCode());
	}


	@Test
	public void testPopulateSolrDocumentPopFromOneTimeChargeSource()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPop();
		componentPopulator.populate(otcPopModel, solrDocumentComponentPop);

		assertEquals(DEFAULT_ID, solrDocumentComponentPop.getId());
		assertEquals(DEFAULT_NAME, solrDocumentComponentPop.getName());
		assertEquals(DEFAULT_DESCRIPTION, solrDocumentComponentPop.getDescription());
		assertEquals(DEFAULT_PRICE_EVENT, solrDocumentComponentPop.getPriceEvent().getCode());
		assertEquals(DEFAULT_PRICE_VALUE, solrDocumentComponentPop.getValue(), 0.0);
		assertEquals(DEFAULT_CURRENCY_ISO_CODE, solrDocumentComponentPop.getCurrency().getName());
		assertEquals(DEFAULT_CURRENCY_ISO_CODE, solrDocumentComponentPop.getCurrency().getIsocode());
	}

	protected void assertComponentPopFieldsArePopulatedCorrectly(final TmaSolrDocumentComponentPop solrDocumentComponentPop)
	{
		assertPopFieldsArePopulatedCorrectly(solrDocumentComponentPop);
		assertEquals(DEFAULT_PRICE_VALUE, solrDocumentComponentPop.getValue(), 0.0);
		assertEquals(DEFAULT_CURRENCY_ISO_CODE, solrDocumentComponentPop.getCurrency().getName());
		assertEquals(DEFAULT_CURRENCY_ISO_CODE, solrDocumentComponentPop.getCurrency().getIsocode());
	}

	protected TmaOneTimeProdOfferPriceChargeModel getOtcPop()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPopWithPriceEvent();
		otcPopModel.setValue(DEFAULT_PRICE_VALUE);

		return otcPopModel;
	}
}

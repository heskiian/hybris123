/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.pricing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentCompositePop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentOneTimeChargeComponentPop;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentProductOfferingPrice;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentPscvUse;
import de.hybris.platform.b2ctelcoservices.pricing.TmaSolrDocumentRecurringChargeComponentPop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


/**
 * Default Unit Test for the {@link TmaSolrDocumentCompositePopPopulator}.
 *
 * @since 2007
 */
@UnitTest
public class TmaSolrDocumentCompositePopPopulatorTest extends TmaSolrDocumentProductOfferingPricePopulatorTest
{
	protected static final String FIRST_YEAR_RC_ID = "first_year_monthly_fee";
	protected static final String SECOND_YEAR_RC_ID = "second_year_monthly_fee";
	protected static final double PROMO_PRICE_VALUE = 0.0;
	protected static final String MOCK_CHARGE_ID = "MOCK_CHARGE_ID";

	private TmaSolrDocumentCompositePopPopulator<TmaCompositeProdOfferPriceModel, TmaSolrDocumentCompositePop> compositePopPopulator;
	private TmaSolrDocumentCompositePop solrDocumentCompositePop;

	@Mock
	private Map<String, Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice>> popConvertersMap;

	@Mock
	private Converter<TmaProductOfferingPriceModel, TmaSolrDocumentProductOfferingPrice> tmaSolrDocumentOneTimeChargeComponentConverter;

	@Mock
	private Converter<TmaProductSpecCharacteristicValueModel, TmaSolrDocumentPscvUse> pscvConverter;

	@Before
	public void setUp()
	{
		super.setUp();
		compositePopPopulator = new TmaSolrDocumentCompositePopPopulator<>(popConvertersMap,pscvConverter);
		solrDocumentCompositePop = new TmaSolrDocumentCompositePop();

		when(popConvertersMap.get(TmaOneTimeProdOfferPriceChargeModel._TYPECODE))
				.thenReturn(tmaSolrDocumentOneTimeChargeComponentConverter);
		when(popConvertersMap.get(TmaRecurringProdOfferPriceChargeModel._TYPECODE))
				.thenReturn(tmaSolrDocumentOneTimeChargeComponentConverter);
		when(popConvertersMap.get(TmaCompositeProdOfferPriceModel._TYPECODE))
				.thenReturn(tmaSolrDocumentOneTimeChargeComponentConverter);
	}

	@Test
	public void testPopulateCompositePopWithNoChildren()
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPrice = getCompositeProdOfferPrice();
		compositePopPopulator.populate(compositeProdOfferPrice, solrDocumentCompositePop);

		assertPopFieldsArePopulatedCorrectly(solrDocumentCompositePop);
		assertNull(solrDocumentCompositePop.getChildren());
	}

	@Test
	public void testPopulateCompositePopWithOneOtc()
	{
		final TmaCompositeProdOfferPriceModel rootPop = getRootPop();

		final Set<TmaProductOfferingPriceModel> children = new HashSet<>();
		final TmaOneTimeProdOfferPriceChargeModel defaultOtc = getOtcPopWithPriceEvent();
		children.add(defaultOtc);
		rootPop.setChildren(children);
		final TmaSolrDocumentOneTimeChargeComponentPop mockOtc = setupMockOtc(defaultOtc);

		compositePopPopulator.populate(rootPop, solrDocumentCompositePop);
		assertPopFieldsArePopulatedCorrectly(solrDocumentCompositePop);

		assertEquals(mockOtc.getId(), solrDocumentCompositePop.getChildren().get(0).getId());
	}

	private TmaSolrDocumentOneTimeChargeComponentPop setupMockOtc(TmaOneTimeProdOfferPriceChargeModel defaultOtc)
	{
		final TmaSolrDocumentOneTimeChargeComponentPop mockOtcPop = new TmaSolrDocumentOneTimeChargeComponentPop();
		mockOtcPop.setId(MOCK_CHARGE_ID);
		when(tmaSolrDocumentOneTimeChargeComponentConverter.convert(defaultOtc)).thenReturn(mockOtcPop);
		return mockOtcPop;
	}

	private TmaCompositeProdOfferPriceModel getRootPop()
	{
		final TmaCompositeProdOfferPriceModel rootPop = new TmaCompositeProdOfferPriceModel();
		setLocaleOnModel(rootPop);
		rootPop.setId(DEFAULT_ID);
		rootPop.setName(DEFAULT_NAME);
		rootPop.setDescription(DEFAULT_DESCRIPTION);

		return rootPop;
	}

	@Test
	public void testPopulateCompositePopWithOneRc()
	{
		final TmaCompositeProdOfferPriceModel rootPop = getRootPop();

		final Set<TmaProductOfferingPriceModel> children = new HashSet<>();
		final TmaRecurringProdOfferPriceChargeModel defaultRc = getRcPopWithPriceEvent();
		children.add(defaultRc);
		rootPop.setChildren(children);
		final TmaSolrDocumentRecurringChargeComponentPop mockRcPop = setupMockRc(defaultRc);

		compositePopPopulator.populate(rootPop, solrDocumentCompositePop);
		assertEquals(mockRcPop.getId(), solrDocumentCompositePop.getChildren().get(0).getId());
	}

	private TmaSolrDocumentRecurringChargeComponentPop setupMockRc(final TmaRecurringProdOfferPriceChargeModel defaultRc)
	{
		final TmaSolrDocumentRecurringChargeComponentPop mockRcPop = new TmaSolrDocumentRecurringChargeComponentPop();
		mockRcPop.setId(MOCK_CHARGE_ID);
		when(tmaSolrDocumentOneTimeChargeComponentConverter.convert(defaultRc)).thenReturn(mockRcPop);
		return mockRcPop;
	}

	@Test
	public void testPopulateCompositePopWithMultipleLayersAndComponents()
	{
		final TmaCompositeProdOfferPriceModel rootProdOfferPrice = getRootPop();
		final Set<TmaProductOfferingPriceModel> rootChildren = new HashSet<>();

		rootChildren.add(getRecurringChargeCompositePrice());
		rootChildren.add(getOtcPopWithPriceEvent());

		rootProdOfferPrice.setChildren(rootChildren);

		compositePopPopulator.populate(rootProdOfferPrice, solrDocumentCompositePop);
		assertPopFieldsArePopulatedCorrectly(solrDocumentCompositePop);
		final TmaSolrDocumentProductOfferingPrice firstPop = solrDocumentCompositePop.getChildren().get(0);
		if (firstPop instanceof TmaSolrDocumentCompositePop)
		{
			//			recurring charges have been stored first
			final TmaSolrDocumentCompositePop recurringChargeCompositePop = (TmaSolrDocumentCompositePop) solrDocumentCompositePop
					.getChildren().get(0);
			verifyRecurringChargeCompositePop(recurringChargeCompositePop);
			assertEquals(MOCK_CHARGE_ID, solrDocumentCompositePop.getChildren().get(1).getId());
		}
		else
		{
			//			one time charge has been stored first
			assertEquals(MOCK_CHARGE_ID, solrDocumentCompositePop.getChildren().get(0).getId());
			final TmaSolrDocumentCompositePop recurringChargeCompositePop = (TmaSolrDocumentCompositePop) solrDocumentCompositePop
					.getChildren().get(1);
			verifyRecurringChargeCompositePop(recurringChargeCompositePop);
		}
	}

	private void verifyRecurringChargeCompositePop(final TmaSolrDocumentCompositePop recurringChargeCompositePop)
	{
		final List<TmaSolrDocumentProductOfferingPrice> recurringChargeCompositePopChildren = recurringChargeCompositePop
				.getChildren();
		assertEquals(2, recurringChargeCompositePopChildren.size());
		assertEquals(MOCK_CHARGE_ID, recurringChargeCompositePopChildren.get(0).getId());
		assertEquals(MOCK_CHARGE_ID, recurringChargeCompositePopChildren.get(1).getId());
	}

	private TmaCompositeProdOfferPriceModel getRecurringChargeCompositePrice()
	{
		final TmaCompositeProdOfferPriceModel rcCompositeProdOfferPrice = new TmaCompositeProdOfferPriceModel();
		setLocaleOnModel(rcCompositeProdOfferPrice);

		final Set<TmaProductOfferingPriceModel> recurringChargeChildren = new HashSet<>();
		List<TmaSolrDocumentProductOfferingPrice> mockRcPops = new ArrayList<>();

		final TmaRecurringProdOfferPriceChargeModel firstYearRc = getFirstYearRcPop();
		final TmaSolrDocumentRecurringChargeComponentPop firstRcMock = setupMockRc(firstYearRc);
		mockRcPops.add(firstRcMock);

		final TmaRecurringProdOfferPriceChargeModel secondYearRc = getSecondYearRcPop();
		final TmaSolrDocumentRecurringChargeComponentPop secondRcMock = setupMockRc(secondYearRc);
		mockRcPops.add(secondRcMock);

		recurringChargeChildren.add(firstYearRc);
		recurringChargeChildren.add(secondYearRc);
		rcCompositeProdOfferPrice.setChildren(recurringChargeChildren);

		final TmaSolrDocumentCompositePop compositePop = new TmaSolrDocumentCompositePop();
		compositePop.setChildren(mockRcPops);

		when(tmaSolrDocumentOneTimeChargeComponentConverter.convert(rcCompositeProdOfferPrice)).thenReturn(compositePop);

		return rcCompositeProdOfferPrice;
	}

	protected TmaOneTimeProdOfferPriceChargeModel getOtcPopWithPriceEvent()
	{
		final TmaOneTimeProdOfferPriceChargeModel otcPopModel = getOtcPopWithoutPriceEvent();
		otcPopModel.setPriceEvent(getPriceEvent());
		otcPopModel.setCurrency(getCurrency());
		otcPopModel.setValue(DEFAULT_PRICE_VALUE);

		setupMockOtc(otcPopModel);

		return otcPopModel;
	}

	protected TmaRecurringProdOfferPriceChargeModel getRcPopWithPriceEvent()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = createRcModelWithLocale();

		rcPopModel.setId(DEFAULT_ID);
		rcPopModel.setName(DEFAULT_NAME);
		rcPopModel.setDescription(DEFAULT_DESCRIPTION);

		rcPopModel.setPriceEvent(getPriceFrequency());
		rcPopModel.setCurrency(getCurrency());
		rcPopModel.setValue(DEFAULT_PRICE_VALUE);
		rcPopModel.setCycleStart(DEFAULT_CYCLE_START);
		rcPopModel.setCycleEnd(DEFAULT_CYCLE_END);

		return rcPopModel;
	}

	protected TmaRecurringProdOfferPriceChargeModel getFirstYearRcPop()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = createRcModelWithLocale();

		rcPopModel.setId(FIRST_YEAR_RC_ID);
		rcPopModel.setPriceEvent(getPriceFrequency());
		rcPopModel.setCurrency(getCurrency());

		rcPopModel.setValue(DEFAULT_PRICE_VALUE);
		rcPopModel.setCycleStart(DEFAULT_CYCLE_START);
		rcPopModel.setCycleEnd(DEFAULT_CYCLE_END);

		return rcPopModel;
	}

	protected TmaRecurringProdOfferPriceChargeModel createRcModelWithLocale()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = new TmaRecurringProdOfferPriceChargeModel();
		setLocaleOnModel(rcPopModel);
		return rcPopModel;
	}


	protected TmaRecurringProdOfferPriceChargeModel getSecondYearRcPop()
	{
		final TmaRecurringProdOfferPriceChargeModel rcPopModel = createRcModelWithLocale();

		rcPopModel.setId(SECOND_YEAR_RC_ID);
		rcPopModel.setPriceEvent(getPriceFrequency());
		rcPopModel.setCurrency(getCurrency());

		rcPopModel.setValue(PROMO_PRICE_VALUE);
		rcPopModel.setCycleStart(DEFAULT_CYCLE_END);

		return rcPopModel;
	}


	protected TmaCompositeProdOfferPriceModel getCompositeProdOfferPrice()
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPrice = new TmaCompositeProdOfferPriceModel();
		setLocaleOnModel(compositeProdOfferPrice);

		compositeProdOfferPrice.setId(DEFAULT_ID);
		compositeProdOfferPrice.setName(DEFAULT_NAME);
		compositeProdOfferPrice.setDescription(DEFAULT_DESCRIPTION);
		return compositeProdOfferPrice;
	}

}

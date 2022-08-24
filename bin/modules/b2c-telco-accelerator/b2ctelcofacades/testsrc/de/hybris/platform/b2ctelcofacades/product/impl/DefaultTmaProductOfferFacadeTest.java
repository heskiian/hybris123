/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcofacades.strategy.TmaProcessFlowStrategy;
import de.hybris.platform.b2ctelcofacades.strategy.impl.TmaProcessFlowStrategyMapping;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;


@UnitTest
public class DefaultTmaProductOfferFacadeTest
{
	private static final String RETENTION_PROCESS_TYPE = "RETENTION";
	private static final String AFFECTED_PRODUCT = "spo1";
	private static final String BPO = "bpo1";
	private static final String REQUIRED_PRODUCT = "spo2";

	private DefaultTmaProductOfferFacade tmaProductOfferFacade;
	private List<TmaOfferData> offers;
	@Mock
	private TmaProcessFlowStrategy retentionStrategy;
	@Mock
	private TmaPoService tmaPoService;
	@Mock
	private Converter<ProductModel, ProductData> productConverter;
	@Mock
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;
	@Mock
	private Converter<TmaPriceContextData, TmaPriceContext> priceContextDataReverseConverter;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.tmaProductOfferFacade = new DefaultTmaProductOfferFacade();
		tmaProductOfferFacade.setTmaPoService(tmaPoService);
		tmaProductOfferFacade.setProductConverter(productConverter);
		tmaProductOfferFacade.setProductConfiguredPopulator(productConfiguredPopulator);
		tmaProductOfferFacade.setPriceContextDataReverseConverter(priceContextDataReverseConverter);
	}

	@Test
	public void givenStrategyNotFound_thenReturnEmptyList()
	{
		givenStrategies(new HashMap<>());
		whenOffersAreFetched(AFFECTED_PRODUCT, BPO, REQUIRED_PRODUCT);
		thenOffersAre(Collections.emptyList());
	}

	@Test
	public void givenDeviceOnly_thenOffersAreReturned()
	{
		final List<TmaOfferData> expectedOffers = Arrays.asList(new TmaOfferData());
		Mockito.when(retentionStrategy.getOffersForDeviceOnly(AFFECTED_PRODUCT)).thenReturn(expectedOffers);

		givenStrategies(getRetentionStrategy());
		whenOffersAreFetched(AFFECTED_PRODUCT, "", null);
		thenOffersAre(expectedOffers);
	}


	@Test
	public void givenDeviceInBpoFound_thenOffersAreReturned()
	{
		final List<TmaOfferData> expectedOffers = Arrays.asList(new TmaOfferData());
		Mockito.when(
				retentionStrategy.getOffersForDeviceInBpo(AFFECTED_PRODUCT, BPO, new HashSet<>(Arrays.asList(REQUIRED_PRODUCT))))
				.thenReturn(expectedOffers);
		givenStrategies(getRetentionStrategy());
		whenOffersAreFetched(AFFECTED_PRODUCT, BPO, REQUIRED_PRODUCT);
		thenOffersAre(expectedOffers);
	}

	@Test
	public void getPoForCodeTest()
	{
		final TmaPriceContext priceContext = new TmaPriceContext();
		final TmaProductOfferingModel productOffering = new TmaProductOfferingModel();
		final ProductData productData = new ProductData();
		productData.setPrice(new PriceData());
		Mockito.when(productOffering.getParents()).thenReturn(null);
		Mockito.when(tmaPoService.getPoForCodeAndPriceContext("POCODE", null)).thenReturn(productOffering);
		Mockito.when(productConverter.convert(productOffering)).thenReturn(productData);
		productConfiguredPopulator.populate(productOffering, productData, Arrays.asList(ProductOption.BASIC));
		tmaProductOfferFacade.getPoForCode("POCODE", Arrays.asList(ProductOption.BASIC), new TmaPriceContextData());
		assertNotNull(productData);
	}

	private void givenStrategies(final Map<String, TmaProcessFlowStrategy> strategies)
	{
		final TmaProcessFlowStrategyMapping strategyMapping = new TmaProcessFlowStrategyMapping();
		strategyMapping.setStrategyMap(strategies);
		this.tmaProductOfferFacade.setStrategyMapping(strategyMapping);
	}

	private void whenOffersAreFetched(final String affectedProduct, final String bpo, final String requiredProduct)
	{
		this.offers = tmaProductOfferFacade.getOffers(RETENTION_PROCESS_TYPE, affectedProduct, bpo, new HashSet<>(Arrays.asList(requiredProduct)));
	}

	private void thenOffersAre(final List<TmaOfferData> expected)
	{
		assertEquals(expected, offers);
	}


	private Map<String, TmaProcessFlowStrategy> getRetentionStrategy()
	{
		final Map<String, TmaProcessFlowStrategy> strategies = new HashMap<>();
		strategies.put(RETENTION_PROCESS_TYPE, retentionStrategy);
		return strategies;
	}
}

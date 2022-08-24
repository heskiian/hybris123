/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.UpilResponseList;
import de.hybris.platform.upilintegrationservices.datamapper.UpilUtilitiesProductDataMapper;
import de.hybris.platform.upilintegrationservices.exceptions.UpilintegrationservicesException;
import de.hybris.platform.upilintegrationservices.service.UpilIntegrationClientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link DefaultUpilUtilitiesProductServiceImpl}.
 * 
 * @since 1911
 */
@UnitTest
public class DefaultUpilUtilitiesProductServiceUnitTest
{
	@Mock
	private Converter<SubscriptionPricePlanModel, C_UtilitiesProductType> upilUtilitiesProductConverter;
	@Mock
	private UpilUtilitiesProductDataMapper upilUtilitiesProductDataMapper;
	@Mock
	private UpilIntegrationClientService upilIntegrationClientService;

	private DefaultUpilUtilitiesProductServiceImpl upilUtilitiesProductService;

	private List<SubscriptionPricePlanModel> prices;
	private C_UtilitiesProductType utilsproduct;
	private List<C_UtilitiesProductType> utilsProductList;
	private Map<String, Object> utilsProductDataMap;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		upilUtilitiesProductService = new DefaultUpilUtilitiesProductServiceImpl();
		upilUtilitiesProductService.setUpilUtilitiesProductConverter(upilUtilitiesProductConverter);
		upilUtilitiesProductService.setUpilUtilitiesProductDataMapper(upilUtilitiesProductDataMapper);
		upilUtilitiesProductService.setUpilIntegrationClientService(upilIntegrationClientService);
	}

	@Test
	public void testcreateUpilProduct() throws UpilintegrationservicesException
	{
		setUpProduct();
		when(upilUtilitiesProductConverter.convertAll(prices)).thenReturn(utilsProductList);
		when(upilUtilitiesProductDataMapper.getUtilitiesProductDataMap(utilsproduct)).thenReturn(utilsProductDataMap);
		final UpilResponseList responseList = upilUtilitiesProductService.createUpilProduct(prices);
		Assert.assertFalse(responseList.getUpilResponses().isEmpty());
	}

	@Test
	public void testcreateUpilProductNull() throws UpilintegrationservicesException
	{
		final UpilResponseList responseList = upilUtilitiesProductService.createUpilProduct(null);
		Assert.assertTrue(responseList.getUpilResponses().isEmpty());
	}

	private void setUpProduct()
	{
		final SubscriptionPricePlanModel price = new SubscriptionPricePlanModel();
		price.setCode("test");
		prices = new ArrayList<>();
		prices.add(price);
		utilsproduct = new C_UtilitiesProductType();
		utilsproduct.setUtilitiesProduct(price.getCode());
		utilsProductList = new ArrayList<>();
		utilsProductList.add(utilsproduct);
		utilsProductDataMap = new HashMap<>();
	}
}



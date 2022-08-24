/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.constants.UpilintegrationservicesConstants;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.UpilResponse;
import de.hybris.platform.upilintegrationservices.data.UpilResponseList;
import de.hybris.platform.upilintegrationservices.datamapper.UpilUtilitiesProductDataMapper;
import de.hybris.platform.upilintegrationservices.service.UpilIntegrationClientService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ObjectUtils;


/**
 * Test class for {@link DefaultUpilIntegrationClientService}.
 *
 * @since 1911
 */
@IntegrationTest
public class DefaultUpilIntegrationClientServiceIntegerationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultUpilIntegrationClientServiceIntegerationTest.class);
	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTHORIZATION_TYPE = "Basic ";
	private static final String UTF_8 = "UTF-8";
	private static final String ELECTRIC_PLAN_PO_CODE = "test_electric_plan";
	private static final String GAS_PLAN_PO_CODE = "test_gas_plan";



	private final Random randomIdGenerator = new Random();

	@Resource
	private UpilIntegrationClientService upilIntegrationClientService;

	@Resource
	private ModelService modelService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	private Populator<SubscriptionPricePlanModel, C_UtilitiesProductType> upilUtilitiesProductPopulator;

	@Resource
	private UpilUtilitiesProductDataMapper upilUtilitiesProductDataMapper;

	@Before
	public void setUp() throws ImpExException
	{
		Assume.assumeTrue(checkUpilConnection());
		importCsv("/test/impex/test_product-prices.impex", "utf-8");
	}

	@Test
	public void testCreateUpilProductSuccess()
	{
		final List<String> productIds = new ArrayList<>();
		final UpilResponseList upilResponseList = createProductData(productIds, ELECTRIC_PLAN_PO_CODE);
		final List<String> successProductCodes = new ArrayList<>();
		upilResponseList.getUpilResponses().stream().map(UpilResponse::getSuccessCode).forEachOrdered(successProductCodes::add);
		if (productIds.containsAll(successProductCodes))
		{
			assertTrue("synced products successfully to upil", Boolean.TRUE);
		}
		else
		{
			assertFalse("there is an error to sync the products with upil", Boolean.FALSE);
		}

	}

	@Test
	public void tesCreateUpilProductFail()
	{
		final List<String> productIds = new ArrayList<>();
		final UpilResponseList upilResponseList = createProductData(productIds, GAS_PLAN_PO_CODE);
		final Map<String, String> errors = new HashMap<>();
		upilResponseList.getUpilResponses().stream().filter(upilResponse -> !ObjectUtils.isEmpty(upilResponse.getError()))
				.map(UpilResponse::getError).forEach(errors::putAll);
		assertTrue("upilResponses are having errors", !ObjectUtils.isEmpty(errors));
	}

	private UpilResponseList createProductData(final List<String> productIds, final String poCode)
	{
		final UpilResponseList upilResponseList = new UpilResponseList();
		final List<UpilResponse> upilResponses = new ArrayList<>();
		final TmaProductOfferingModel source = modelService.create(TmaProductOfferingModel.class);
		source.setCode(poCode);
		source.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		final TmaProductOfferingModel po = flexibleSearchService.getModelByExample(source);
		final List<SubscriptionPricePlanModel> subscriptionPricePlanList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(po.getOwnEurope1Prices()))
		{
			po.getOwnEurope1Prices().forEach(priceRow ->
			{
				if (priceRow instanceof SubscriptionPricePlanModel)
				{

					priceRow.setCode("TEST_" + randomIdGenerator.nextInt());
					modelService.save(priceRow);
					subscriptionPricePlanList.add((SubscriptionPricePlanModel) priceRow);
					productIds.add(priceRow.getCode());
				}
			});
		}

		final List<C_UtilitiesProductType> productDatas = getPoToUtilitiesProduct(subscriptionPricePlanList);
		final List<Map<String, Object>> products = getMapOfUtilitiesProduct(productDatas);
		for (final Map<String, Object> product : products)
		{
			upilResponses.add(upilIntegrationClientService.createUpilProduct(product));

		}
		upilResponseList.setUpilResponses(upilResponses);
		return upilResponseList;
	}

	private List<C_UtilitiesProductType> getPoToUtilitiesProduct(final List<SubscriptionPricePlanModel> prices)
	{

		final List<C_UtilitiesProductType> utilsProductList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(prices))
		{
			for (final SubscriptionPricePlanModel price : prices)
			{
				final C_UtilitiesProductType utilsProductData = new C_UtilitiesProductType();
				upilUtilitiesProductPopulator.populate(price, utilsProductData);
				utilsProductList.add(utilsProductData);
			}
		}
		return utilsProductList;
	}

	private List<Map<String, Object>> getMapOfUtilitiesProduct(final List<C_UtilitiesProductType> utilsProductDataList)
	{
		final List<Map<String, Object>> tmaUtilsProductDataMapList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(utilsProductDataList))
		{
			for (final C_UtilitiesProductType utilsProductData : utilsProductDataList)
			{
				tmaUtilsProductDataMapList.add(upilUtilitiesProductDataMapper.getUtilitiesProductDataMap(utilsProductData));
			}
		}
		return tmaUtilsProductDataMapList;
	}

	private boolean checkUpilConnection()
	{
		final String upilUserName = Config.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_USERNAME);
		final String upilPassword = Config.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_PW);
		final String upilServiceUrl = Config.getParameter(UpilintegrationservicesConstants.UPIL_SERVICE_URL_GET_METADATA);
		boolean isConnected = false;
		URL upilUrl;
		HttpURLConnection upilConnection;
		try
		{
			upilUrl = new URL(upilServiceUrl);
			upilConnection = (HttpURLConnection) upilUrl.openConnection();
			upilConnection.setRequestProperty(AUTHORIZATION,
					AUTHORIZATION_TYPE + new String(Base64.encodeBase64((upilUserName + ":" + upilPassword).getBytes()), UTF_8));
			upilConnection.connect();
			isConnected = true;
			upilConnection.disconnect();
		}
		catch (final IOException e)
		{
			LOG.error("Connection failed with UPIL");
		}
		return isConnected;
	}
}

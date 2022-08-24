/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;
import de.hybris.platform.upilintegrationservices.data.UpilResponse;
import de.hybris.platform.upilintegrationservices.data.UpilResponseList;
import de.hybris.platform.upilintegrationservices.datamapper.UpilUtilitiesProductDataMapper;
import de.hybris.platform.upilintegrationservices.service.UpilIntegrationClientService;
import de.hybris.platform.upilintegrationservices.service.UpilUtilitiesProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;


/**
 * Default implementation of the {@link UpilUtilitiesProductService}.
 * 
 * @since 1911
 */
public class DefaultUpilUtilitiesProductServiceImpl implements UpilUtilitiesProductService
{
	private Converter<SubscriptionPricePlanModel, C_UtilitiesProductType> upilUtilitiesProductConverter;
	private UpilUtilitiesProductDataMapper upilUtilitiesProductDataMapper;
	private UpilIntegrationClientService upilIntegrationClientService;

	@Override
	public UpilResponseList createUpilProduct(final List<SubscriptionPricePlanModel> prices)
	{
		final UpilResponseList upilResponseList = new UpilResponseList();
		final List<UpilResponse> upilResponse = new ArrayList<>();
		final List<C_UtilitiesProductType> productDatas = getPoToUtilitiesProduct(prices);
		final List<Map<String, Object>> products = getMapOfUtilitiesProduct(productDatas);
		if (!CollectionUtils.isEmpty(products))
		{
			products.forEach(product -> upilResponse.add(getUpilIntegrationClientService().createUpilProduct(product)));
		}
		upilResponseList.setUpilResponses(upilResponse);
		return upilResponseList;
	}

	private List<C_UtilitiesProductType> getPoToUtilitiesProduct(final List<SubscriptionPricePlanModel> prices)
	{

		final List<C_UtilitiesProductType> utilsProductList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(prices))
		{
			utilsProductList.addAll(getUpilUtilitiesProductConverter().convertAll(prices));
		}
		return utilsProductList;
	}

	private List<Map<String, Object>> getMapOfUtilitiesProduct(final List<C_UtilitiesProductType> utilsProductDataList)
	{
		final List<Map<String, Object>> utilsProductDataMapList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(utilsProductDataList))
		{
			for (final C_UtilitiesProductType utilsProductData : utilsProductDataList)
			{
				utilsProductDataMapList.add(getUpilUtilitiesProductDataMapper().getUtilitiesProductDataMap(utilsProductData));
			}
		}
		return utilsProductDataMapList;
	}

	protected Converter<SubscriptionPricePlanModel, C_UtilitiesProductType> getUpilUtilitiesProductConverter()
	{
		return upilUtilitiesProductConverter;
	}

	@Required
	public void setUpilUtilitiesProductConverter(
			final Converter<SubscriptionPricePlanModel, C_UtilitiesProductType> upilUtilitiesProductConverter)
	{
		this.upilUtilitiesProductConverter = upilUtilitiesProductConverter;
	}

	protected UpilUtilitiesProductDataMapper getUpilUtilitiesProductDataMapper()
	{
		return upilUtilitiesProductDataMapper;
	}

	@Required
	public void setUpilUtilitiesProductDataMapper(final UpilUtilitiesProductDataMapper upilUtilitiesProductDataMapper)
	{
		this.upilUtilitiesProductDataMapper = upilUtilitiesProductDataMapper;
	}


	protected UpilIntegrationClientService getUpilIntegrationClientService()
	{
		return upilIntegrationClientService;
	}

	@Required
	public void setUpilIntegrationClientService(final UpilIntegrationClientService upilIntegrationClientService)
	{
		this.upilIntegrationClientService = upilIntegrationClientService;
	}

}

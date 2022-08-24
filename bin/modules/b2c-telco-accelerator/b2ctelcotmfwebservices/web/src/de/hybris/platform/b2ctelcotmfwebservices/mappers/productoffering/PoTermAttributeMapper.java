/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This attribute Mapper class maps data for poTerm attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoTermAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductOfferingPrices()))
		{
			return;
		}

		Map<String, SubscriptionTermData> subscriptionTermDataMap = new HashMap<>();
		source.getProductOfferingPrices().forEach((SubscriptionPricePlanData priceData) ->
		{
			if (priceData.getSubscriptionTerms() != null)
			{
				priceData.getSubscriptionTerms().forEach((SubscriptionTermData subscriptionTermData) -> {
					if (!subscriptionTermDataMap.containsKey(subscriptionTermData.getId()))
					{
						subscriptionTermDataMap.put(subscriptionTermData.getId(), subscriptionTermData);
					}
				});
			}
		});
		final List<ProductOfferingTerm> productOfferingTermList = new ArrayList<>();

		subscriptionTermDataMap.values().forEach(subscriptionTermData -> {
			final ProductOfferingTerm productOfferingTermWsDto = getMapperFacade()
					.map(subscriptionTermData, ProductOfferingTerm.class, context);
			productOfferingTermList.add(productOfferingTermWsDto);
		});

		target.setProductOfferingTerm(productOfferingTermList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

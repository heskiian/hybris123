/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This attribute Mapper class maps data for poTerm attribute between {@link ProductData} and {@link ProductOffering}
 * Used with Composite Prices.
 *
 * @since 2007
 */
public class PoTermCpAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	private MapperFacade mapperFacade;

	public PoTermCpAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPrices()))
		{
			return;
		}

		Map<String, SubscriptionTermData> subscriptionTermDataMap = new HashMap<>();
		source.getPrices().forEach((PriceData priceData) ->
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
}

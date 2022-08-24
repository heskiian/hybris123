/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for PO term attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceTermAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getSubscriptionTerms()))
		{
			return;
		}

		final List<ProductOfferingTerm> productOfferingTermWsDtoList = new ArrayList<>();
		source.getSubscriptionTerms().forEach(subscriptionTermData ->
		{
			final ProductOfferingTerm productOfferingTermWsDto = getMapperFacade()
					.map(subscriptionTermData, ProductOfferingTerm.class, context);
			productOfferingTermWsDtoList.add(productOfferingTermWsDto);
		});

		target.setProductOfferingTerm(productOfferingTermWsDtoList);
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

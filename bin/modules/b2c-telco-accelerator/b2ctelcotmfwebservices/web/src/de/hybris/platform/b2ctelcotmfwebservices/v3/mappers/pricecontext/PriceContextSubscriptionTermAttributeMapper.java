/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTerm;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for PO term attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PriceContextSubscriptionTermAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	private MapperFacade mapperFacade;

	public PriceContextSubscriptionTermAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getSubscriptionTerms()))
		{
			return;
		}

		final List<ProductOfferingTerm> productOfferingTermWsDtoList = source.getSubscriptionTerms().stream()
				.map(subscriptionTermData -> getMapperFacade().map(subscriptionTermData, ProductOfferingTerm.class, context))
				.collect(Collectors.toList());

		target.setProductOfferingTerm(productOfferingTermWsDtoList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

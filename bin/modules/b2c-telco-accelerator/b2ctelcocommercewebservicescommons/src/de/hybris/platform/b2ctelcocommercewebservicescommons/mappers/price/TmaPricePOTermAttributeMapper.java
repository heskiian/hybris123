/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * The attribute TmaPricePOTermAttributeMapper class maps data for PO term attribute between
 * {@link PriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPricePOTermAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getSubscriptionTerms()))
		{
			return;
		}

		final List<ProductOfferingTermWsDTO> productOfferingTermWsDtoList = new ArrayList<>();
		source.getSubscriptionTerms().forEach(subscriptionTermData ->
		{
			final ProductOfferingTermWsDTO productOfferingTermWsDto = getMapperFacade().map(subscriptionTermData,
					ProductOfferingTermWsDTO.class, context);
			productOfferingTermWsDtoList.add(productOfferingTermWsDto);
		});

		target.setProductOfferingTerm(productOfferingTermWsDtoList);

	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}

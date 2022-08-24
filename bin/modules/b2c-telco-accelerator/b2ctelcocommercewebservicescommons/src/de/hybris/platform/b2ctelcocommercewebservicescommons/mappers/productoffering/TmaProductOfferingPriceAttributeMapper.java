/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * Populates value of productOfferingPrice attribute from {@link ProductData} to {@link ProductWsDTO}
 *
 * @since 2007
 */
public class TmaProductOfferingPriceAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaProductOfferingPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getPrices()))
		{
			return;
		}

		target.setProductOfferingPrice(source.getPrices().stream().map(priceData ->
				getMapperFacade().map(priceData, ProductOfferingPriceWsDTO.class, context)).collect(Collectors.toList()));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

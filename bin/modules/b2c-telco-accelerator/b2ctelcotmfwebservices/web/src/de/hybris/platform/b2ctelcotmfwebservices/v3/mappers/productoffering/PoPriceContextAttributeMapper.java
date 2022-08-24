/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for po price attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoPriceContextAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;

	public PoPriceContextAttributeMapper(final MapperFacade mapperFacade)
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

		final List<PriceContext> priceList = source.getPrices().stream()
				.filter(priceData -> priceData.getProductOfferingPrice() != null)
				.map(priceData -> getMapperFacade().map(priceData, PriceContext.class, context)).collect(Collectors.toList());

		target.setPriceContext(priceList);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

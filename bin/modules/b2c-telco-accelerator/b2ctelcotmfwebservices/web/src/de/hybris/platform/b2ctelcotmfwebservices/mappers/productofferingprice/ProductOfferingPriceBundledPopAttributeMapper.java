/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;


import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundledPop attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class ProductOfferingPriceBundledPopAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;
	private Map<String, Class<ProductOfferingPrice>> productOfferingPriceTypeDtoMap;

	public ProductOfferingPriceBundledPopAttributeMapper(
			final Map<String, Class<ProductOfferingPrice>> productOfferingPriceTypeDtoMap, final MapperFacade mapperFacade)
	{
		this.productOfferingPriceTypeDtoMap = productOfferingPriceTypeDtoMap;
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(PriceData source,
			ProductOfferingPrice target, MappingContext context)
	{
		final TmaProductOfferingPriceData productOfferingPriceData = source.getProductOfferingPrice();
		if (ObjectUtils.isEmpty(productOfferingPriceData))
		{
			return;
		}
		final List<ProductOfferingPrice> bundledPop = new ArrayList<>();
		bundledPop.add(getMapperFacade().map(productOfferingPriceData,
				getProductOfferingPriceTypeDtoMap().get(productOfferingPriceData.getClass().getSimpleName()), context));
		target.setBundledPop(bundledPop);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected Map<String, Class<ProductOfferingPrice>> getProductOfferingPriceTypeDtoMap()
	{
		return productOfferingPriceTypeDtoMap;
	}
}

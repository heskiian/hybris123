/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * The TmaPriceBundledPOPAttributeMapper class maps data for bundledPOP attribute between
 * {@link TmaCompositeProdOfferPriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 2007
 */
public class TmaPopBundledPopAttributeMapper extends TmaAttributeMapper<TmaCompositeProdOfferPriceData, ProductOfferingPriceWsDTO>
{
	private MapperFacade mapperFacade;
	private Map<String, Class<ProductOfferingPriceWsDTO>> priceTypeDtoMap;

	public TmaPopBundledPopAttributeMapper(final MapperFacade mapperFacade,
			final Map<String, Class<ProductOfferingPriceWsDTO>> priceTypeDtoMap)
	{
		this.mapperFacade = mapperFacade;
		this.priceTypeDtoMap = priceTypeDtoMap;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaCompositeProdOfferPriceData source,
			final ProductOfferingPriceWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final Set<TmaProductOfferingPriceData> childrenPrices = source.getChildren();

		if (CollectionUtils.isEmpty(childrenPrices))
		{
			return;
		}

		final List<ProductOfferingPriceWsDTO> productOfferingPrices = childrenPrices.stream()
				.map(tmaAbstractOrderPriceData -> getMapperFacade().map(tmaAbstractOrderPriceData, getPriceTypeDtoMap()
						.get(tmaAbstractOrderPriceData.getClass().getSimpleName()), context)).collect(Collectors.toList());

		target.setBundledPop(productOfferingPrices);
	}

	protected Map<String, Class<ProductOfferingPriceWsDTO>> getPriceTypeDtoMap()
	{
		return priceTypeDtoMap;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

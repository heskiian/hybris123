/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cartPrice attribute between {@link TmaAbstractOrderCompositePriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 1911
 */
public class TmaCartCostChildrenAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderCompositePriceData, CartCostWsDTO>
{
	private MapperFacade mapperFacade;

	private Map<String, Class<CartCostWsDTO>> priceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderCompositePriceData source, final CartCostWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (CollectionUtils.isEmpty(source.getChildPrices()))
		{
			return;
		}

		final List<CartCostWsDTO> outputPrices = new ArrayList<>();

		source.getChildPrices().forEach(tmaAbstractOrderPriceData ->
		{
			if (tmaAbstractOrderPriceData instanceof TmaAbstractOrderAlterationPriceData)
			{
				return;
			}

			final CartCostWsDTO cartPrice = getMapperFacade()
					.map(tmaAbstractOrderPriceData,
							getPriceTypeDtoMap().get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
							context);
			outputPrices.add(cartPrice);
		});

		if (CollectionUtils.isNotEmpty(outputPrices))
		{
			target.setCartPrice(outputPrices);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	protected Map<String, Class<CartCostWsDTO>> getPriceTypeDtoMap()
	{
		return priceTypeDtoMap;
	}

	@Required
	public void setPriceTypeDtoMap(final Map<String, Class<CartCostWsDTO>> priceTypeDtoMap)
	{
		this.priceTypeDtoMap = priceTypeDtoMap;
	}
}

/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps itemPrice attributes between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 1911
 */
public class TmaCartEntriesPriceAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderEntryWsDTO>
{
	private MapperFacade mapperFacade;

	private Map<String, Class<CartCostWsDTO>> priceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderEntryWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getPrice() == null)
		{
			return;
		}
		final TmaAbstractOrderPriceData tmaAbstractOrderPriceData = source.getPrice();
		final CartCostWsDTO cartPrice = getMapperFacade()
				.map(tmaAbstractOrderPriceData, priceTypeDtoMap.get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
						context);
		target.setCartPrice(cartPrice);
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

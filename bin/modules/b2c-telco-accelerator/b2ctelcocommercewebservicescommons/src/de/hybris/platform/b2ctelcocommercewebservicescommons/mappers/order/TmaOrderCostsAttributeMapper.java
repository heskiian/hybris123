/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.order;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for orderCosts attribute between {@link OrderData} and {@link OrderWsDTO}
 *
 * @since 1912
 */
public class TmaOrderCostsAttributeMapper extends TmaAttributeMapper<OrderData, OrderWsDTO>
{
	private final MapperFacade mapperFacade;

	public TmaOrderCostsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final OrderWsDTO target, final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getPrice() == null)
		{
			return;
		}
		final List<CartCostWsDTO> cartPrices = new ArrayList<>();
		cartPrices.add(getMapperFacade().map(source.getPrice(), CartCostWsDTO.class, context));
		target.setOrderCosts(cartPrices);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}

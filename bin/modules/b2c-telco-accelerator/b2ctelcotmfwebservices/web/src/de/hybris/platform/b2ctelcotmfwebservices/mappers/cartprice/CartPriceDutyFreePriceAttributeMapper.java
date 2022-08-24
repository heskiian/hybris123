/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartprice;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Price;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for duty free price attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link CartPrice}
 *
 * @since 1907
 * @deprecated since 2007. Please use {@link CartPriceValueAttributeMapper} instead.
 */
@Deprecated(since = "2007", forRemoval = true)
public class CartPriceDutyFreePriceAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, CartPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final CartPrice target,
			final MappingContext context)
	{
		if (source.getDutyFreeAmount() != null)
		{
			Money money = getMapperFacade().map(source.getDutyFreeAmount(), Money.class);
			if (target.getPrice() == null)
			{
				Price price = new Price();
				target.setPrice(price);
			}
			target.getPrice().setDutyFreeAmount(money);
		}
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



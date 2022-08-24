/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Price;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link OrderPrice}
 *
 * @since 2007
 */
public class OrderPriceValueAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, OrderPrice>
{
	private MapperFacade mapperFacade;

	public OrderPriceValueAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final OrderPrice target,
			final MappingContext context)
	{
		if (source == null)
		{
			return;
		}

		final Price price = new Price();

		price.setTaxIncludedAmount(getMapperFacade().map(source.getTaxIncludedAmount(), Money.class));
		price.setDutyFreeAmount(getMapperFacade().map(source.getDutyFreeAmount(), Money.class));
		price.setAttype(price.getClass().getSimpleName());

		if (source.getTaxRate() != null && source.getTaxRate().getValue() != null)
		{
			price.setTaxRate(source.getTaxRate().getValue().floatValue());
		}

		target.setPrice(price);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

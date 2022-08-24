/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Price;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for tax rate attribute between {@link TmaAbstractOrderChargePriceData} and {@link
 * OrderPrice}
 *
 * @since 1907
 * @deprecated since 2007. Please use {@link OrderPriceValueAttributeMapper} instead.
 */
@Deprecated(since = "2007", forRemoval = true)
public class OrderPriceTaxRateAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, OrderPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final OrderPrice target,
			final MappingContext context)
	{
		if (source.getTaxRate() == null)
		{
			return;
		}

		if (ObjectUtils.isEmpty(target.getPrice()))
		{
			final Price price = new Price();
			target.setPrice(price);
		}

		if (source.getTaxRate().getValue() != null)
		{
			target.getPrice().setTaxRate(source.getTaxRate().getValue().floatValue());
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

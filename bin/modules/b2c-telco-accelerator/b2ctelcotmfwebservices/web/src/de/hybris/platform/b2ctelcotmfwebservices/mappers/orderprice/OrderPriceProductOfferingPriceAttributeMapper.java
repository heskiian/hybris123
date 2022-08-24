/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPriceRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOfferingPrice attribute between {@link TmaAbstractOrderPriceData} and
 * {@link OrderPrice}
 *
 * @since 2102
 */
public class OrderPriceProductOfferingPriceAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderPriceData, OrderPrice>
{
	private MapperFacade mapperFacade;

	public OrderPriceProductOfferingPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source, final OrderPrice target,
			final MappingContext context)
	{
		if (StringUtils.isNotBlank(source.getPriceId()))
		{
			target.setProductOfferingPrice(getMapperFacade().map(source, ProductOfferingPriceRef.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

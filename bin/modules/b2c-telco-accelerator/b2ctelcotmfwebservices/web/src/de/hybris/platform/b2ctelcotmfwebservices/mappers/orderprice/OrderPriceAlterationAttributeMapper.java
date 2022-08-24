/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PriceAlteration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price alteration attribute between {@link TmaAbstractOrderCompositePriceData} and
 * {@link OrderPrice}
 *
 * @since 2007
 */
public class OrderPriceAlterationAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderCompositePriceData, OrderPrice>
{
	private MapperFacade mapperFacade;

	public OrderPriceAlterationAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderCompositePriceData source, final OrderPrice target,
			final MappingContext context)
	{
		if (source.getChildPrices() == null)
		{
			return;
		}

		final List<PriceAlteration> priceAlterations = new ArrayList<>();

		source.getChildPrices().forEach((TmaAbstractOrderPriceData tmaAbstractOrderPriceData) -> {
			if (tmaAbstractOrderPriceData instanceof TmaAbstractOrderAlterationPriceData)
			{
				priceAlterations.add(getMapperFacade()
						.map(tmaAbstractOrderPriceData, PriceAlteration.class, context));
			}
		});

		if (CollectionUtils.isNotEmpty(priceAlterations))

		{
			target.setPriceAlteration(priceAlterations);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

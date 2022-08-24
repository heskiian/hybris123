/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PriceAlterationWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price alteration attribute between {@link TmaAbstractOrderCompositePriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 2007
 */
public class TmaCartCostPriceAlterationAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderCompositePriceData, CartCostWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaCartCostPriceAlterationAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

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

		final List<PriceAlterationWsDTO> priceAlterations = new ArrayList<>();

		source.getChildPrices().forEach(tmaAbstractOrderPriceData ->
		{
			if (tmaAbstractOrderPriceData instanceof TmaAbstractOrderAlterationPriceData)
			{
				priceAlterations.add(getMapperFacade().map(tmaAbstractOrderPriceData, PriceAlterationWsDTO.class, context));
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

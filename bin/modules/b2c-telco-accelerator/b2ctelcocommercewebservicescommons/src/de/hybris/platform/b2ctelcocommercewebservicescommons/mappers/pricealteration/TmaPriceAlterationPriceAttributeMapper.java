/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PriceAlterationWsDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlterationWsDTO}
 *
 * @since 2007
 */
public class TmaPriceAlterationPriceAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlterationWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPriceAlterationPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source,
			final PriceAlterationWsDTO target, final MappingContext context)
	{
		if (source == null)
		{
			return;
		}

		final CartCostWsDTO price = new CartCostWsDTO();

		price.setTaxIncludedAmount(getMapperFacade().map(source.getTaxIncludedAmount(), MoneyWsDTO.class));
		price.setDutyFreeAmount(getMapperFacade().map(source.getDutyFreeAmount(), MoneyWsDTO.class));
		price.setTaxRate(getMapperFacade().map(source.getTaxRate(), MoneyWsDTO.class));
		price.setPercentage(source.getPercentage());

		target.setPrice(price);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

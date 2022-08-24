/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Price;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PriceAlteration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlteration}
 *
 * @since 2007
 */
public class PriceAlterationPriceAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlteration>
{
	private MapperFacade mapperFacade;

	public PriceAlterationPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source, final PriceAlteration target,
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

		if (source.getPercentage() != null)
		{
			price.setPercentage(source.getPercentage().floatValue());
		}

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

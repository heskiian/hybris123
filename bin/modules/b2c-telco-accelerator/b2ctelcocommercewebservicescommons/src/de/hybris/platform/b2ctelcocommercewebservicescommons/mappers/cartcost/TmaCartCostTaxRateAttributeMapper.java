/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for tax rate attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 1911
 */
public class TmaCartCostTaxRateAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, CartCostWsDTO>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final CartCostWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!ObjectUtils.isEmpty(source.getTaxRate()) && source.getTaxRate().getValue() != null)
		{
			final MoneyWsDTO money = getMapperFacade().map(source.getTaxRate(), MoneyWsDTO.class, context);
			target.setTaxRate(money);
		}
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
}

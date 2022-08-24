/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOfferingPrice attribute between {@link TmaAbstractOrderPriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 2102
 */
public class TmaCartCostProductOfferingPriceAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderPriceData, CartCostWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaCartCostProductOfferingPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderPriceData source, final CartCostWsDTO target,
			final MappingContext context)
	{
		if (StringUtils.isNotBlank(source.getPriceId()))
		{
			target.setProductOfferingPrice(getMapperFacade().map(source, ProductOfferingPriceRefWsDTO.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

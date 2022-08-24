/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricealteration;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PriceAlterationWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOfferingPrice attribute between {@link TmaAbstractOrderAlterationPriceData}
 * and {@link PriceAlterationWsDTO}
 *
 * @since 2102
 */
public class TmaPriceAlterationProductOfferingPriceAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlterationWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaPriceAlterationProductOfferingPriceAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source,
			final PriceAlterationWsDTO target, final MappingContext context)
	{
		if (StringUtils.isNotBlank(source.getPriceId()) && isProductOfferingPriceAlteration(source))
		{
			target.setProductOfferingPrice(getMapperFacade().map(source, ProductOfferingPriceRefWsDTO.class, context));
		}
	}

	private boolean isProductOfferingPriceAlteration(final TmaAbstractOrderAlterationPriceData source)
	{
		return !TmaAbstractOrderPriceType.DISCOUNT.equals(source.getTypeOfPrice());
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

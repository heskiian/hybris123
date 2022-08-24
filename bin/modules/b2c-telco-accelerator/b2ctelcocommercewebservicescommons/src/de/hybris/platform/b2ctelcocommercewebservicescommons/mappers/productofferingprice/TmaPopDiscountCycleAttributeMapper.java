/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CycleWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceAlterationWsDTO;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaProdOfferPriceAlterationData} and
 * {@link ProductOfferingPriceAlterationWsDTO}
 *
 * @since 2007
 */
public class TmaPopDiscountCycleAttributeMapper
		extends TmaAttributeMapper<TmaProdOfferPriceAlterationData, ProductOfferingPriceAlterationWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProdOfferPriceAlterationData source,
			final ProductOfferingPriceAlterationWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (ObjectUtils.isEmpty(source.getCycleStart()))
		{
			return;
		}

		final CycleWsDTO cycle = new CycleWsDTO();
		cycle.setCycleStart(source.getCycleStart());
		cycle.setCycleEnd(source.getCycleEnd());
		target.setCycle(cycle);
	}
}
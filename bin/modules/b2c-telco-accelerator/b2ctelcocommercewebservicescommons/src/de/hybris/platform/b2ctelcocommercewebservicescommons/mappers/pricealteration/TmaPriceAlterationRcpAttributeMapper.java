/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PriceAlterationWsDTO;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for recurring charge period attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlterationWsDTO}
 *
 * @since 2007
 */
public class TmaPriceAlterationRcpAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlterationWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source,
			final PriceAlterationWsDTO target, final MappingContext context)
	{
		if (source.getBillingTime() != null)
		{
			target.setRecurringChargePeriod(source.getBillingTime().getCode());
		}
	}
}

/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for chargeType attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 2007
 */
public class TmaCartCostOneTimeChargeTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, CartCostWsDTO>
{
	private static final String ONE_TIME = "oneTime";

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final CartCostWsDTO target,
			final MappingContext context)
	{
		target.setChargeType(ONE_TIME);
	}
}

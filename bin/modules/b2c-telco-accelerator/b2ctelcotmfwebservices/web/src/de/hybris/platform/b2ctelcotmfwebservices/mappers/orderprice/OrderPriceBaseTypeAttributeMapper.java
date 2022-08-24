/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for baseType attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link OrderPrice}
 *
 * @since 1907
 */
public class OrderPriceBaseTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, OrderPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final OrderPrice target,
			final MappingContext context)
	{
		if (target.getClass().getSuperclass() != null)
		{
			target.setAtbaseType(target.getClass().getSuperclass().getSimpleName());
		}
	}
}
